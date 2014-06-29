package world;

import item.Inventory;
import item.Item;
import item.ItemStack;
import item.WorldItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.Database;
import world.Zone.ZoneType;
import world.creatures.Creature;
import world.creatures.Creature.CreatureType;
import world.creatures.Sarah;
import world.generation.Generator;
import world.generation.Generator.Structure;
import world.generation.Layer;
import world.generation.Layer.AimLayer;
import world.worldObjects.WorldObject;
import world.worldObjects.WorldObject.ObjectType;
import core.geom.Vec;

public class World {

	public static float measureScale = 50;
	public static String name;

	public static Generator rightGenerator, leftGenerator;
	
	public static Sarah sarah;
	public static List<Node>[] nodes;
	public static List<Creature>[] creatures;
	public static List<WorldObject>[] worldObjects;
	public static List<WorldItem>[] items;
	public static List<Zone> zones;
	
	public static Database db;
	
	public static void updateContours(){
		int widthHalf = 700;
		WorldView.rimR = (int) (World.sarah.pos.x + widthHalf);
		while(rightGenerator.pos.x < WorldView.rimR){
			rightGenerator.shift();
		}
		WorldView.rimL = (int) (sarah.pos.x - widthHalf);
		while(leftGenerator.pos.x > WorldView.rimL){
			leftGenerator.shift();
		}
		WorldView.rimR += 25;
		WorldView.rimL -= 25;
	}
	
	public static void save(){
		try {
			saveGeneralInformation();
			saveNodes();
			saveCreatures();
			saveWorldObjects();
			saveItems();
			saveZones();
			saveGenerators();
			saveInventory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void load(String name){
		World.name = name;
			//setup things
		sarah = new Sarah(new Vec(0, 5), null);
		
		nodes = (List<Node>[]) new List<?>[Material.values().length];
		for(int i = 0; i < nodes.length; i++){
			nodes[i] = new ArrayList<>();
		}
		
		items = (List<WorldItem>[]) new ArrayList<?>[Item.list.size()];
		for(int i = 0; i < items.length; i++){
			items[i] = new ArrayList<>();
		}
		
		worldObjects = (List<WorldObject>[]) new List<?>[ObjectType.values().length];
		for(int i = 0; i < worldObjects.length; i++){
			worldObjects[i] = new ArrayList<>();
		}
		
		creatures = (List<Creature>[]) new List<?>[CreatureType.values().length];
		for(int i = 0; i < creatures.length; i++){
			creatures[i] = new ArrayList<>();
		}

		Inventory.reset();
		//setup world and generators
		zones = new ArrayList<>();
		db = new Database("worlds", name);
		boolean fresh = !loadGeneralInformation();
		if(fresh){
			try {
				createDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Generator[] gens = Generator.createStartGenerators(new Vec(0, 0));
			rightGenerator = gens[0];
			leftGenerator = gens[1];
		} else {
			try {
				loadNodes();
				loadCreatures();
				loadWorldObjects();
				loadItems();
				loadZones();
				loadGenerators();
				loadInventory();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveGeneralInformation() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'World';");
		s.close();
		s = db.conn.createStatement();
		s.execute("INSERT INTO 'World' (wName, nodeAmount, invSelectedItem) VALUES ('" + name + "', '" + Node.indexIndex + "', '" + Inventory.selectedItem + "');");
		s.close();
	}

	private static boolean loadGeneralInformation(){
		try {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT wName, nodeAmount, invSelectedItem FROM World;");

        ergebnis.next(); 
        name = ergebnis.getString("wName");
        Node.indexIndex = ergebnis.getInt("nodeAmount");
        Inventory.selectedItem = ergebnis.getInt("invSelectedItem");
        
        ergebnis.close();
        sql.close();
        return true;
		} catch (SQLException ex){
			return false;
		}
	}
	
	public static void saveNodes() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'Node';");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Node (n_ID, mat, next_ID, last_ID, x, y) VALUES (?,?,?,?,?,?);");
        
        for(List<Node> list : nodes) for (Node node : list){
        	int i = 1;
            p.setInt(i++, node._id);
            p.setString(i++, node.mat.name());
            p.setInt(i++, node.next._id);
            p.setInt(i++, node.last._id);
            p.setFloat(i++, node.p.x);
            p.setFloat(i++, node.p.y);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}
	static Node[] allNodes;
	private static void loadNodes() throws SQLException{
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT n_ID, mat, next_ID, last_ID, x, y FROM Node;");
        
        allNodes = new Node[Node.indexIndex];
        int[] nextNodes = new int[Node.indexIndex];
        int[] lastNodes = new int[Node.indexIndex];
        
        while(ergebnis.next()){
        	int n_ID = ergebnis.getInt("n_ID");
        	Material mat = Material.valueOf(ergebnis.getString("mat"));
        	int next_ID = ergebnis.getInt("next_ID");
        	int last_ID = ergebnis.getInt("last_ID");
        	int x = ergebnis.getInt("x");
        	int y = ergebnis.getInt("y");
        	
        	Node node = new Node(n_ID, new Vec(x, y), mat);
        	nextNodes[n_ID] = next_ID;
        	lastNodes[n_ID] = last_ID;

        	nodes[mat.ordinal()].add(node);
        	allNodes[n_ID] = node;
        }
        
        for(Node n : allNodes){
        	n.last = allNodes[lastNodes[n._id]];
        	n.next = allNodes[nextNodes[n._id]];
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveCreatures() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'Creature'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Creature (type, pX, pY, vX, vY, health, worldLink, front, metaString) VALUES (?,?,?,?,?,?,?,?,?);");
        
        for(int type = 0; type < creatures.length; type++) for(Creature creature : creatures[type]){
        	int i = 1;
            p.setInt(i++, type);
            p.setFloat(i++, creature.pos.x);
            p.setFloat(i++, creature.pos.y);
            p.setFloat(i++, creature.vel.x);
            p.setFloat(i++, creature.vel.y);
            p.setInt(i++, creature.health);
            p.setInt(i++, creature.worldLink == null ? -1 : creature.worldLink._id);
            p.setBoolean(i++, creature.front);
            p.setString(i++, creature.createMetaString());
            p.addBatch();
        }
        int i = 1;
        p.setInt(i++, -1);
        p.setFloat(i++, sarah.pos.x);
        p.setFloat(i++, sarah.pos.y);
        p.setFloat(i++, sarah.vel.x);
        p.setFloat(i++, sarah.vel.y);
        p.setInt(i++, sarah.health);
        p.setInt(i++, sarah.worldLink == null ? -1 : sarah.worldLink._id);
        p.setBoolean(i++, sarah.front);
        p.setString(i++, sarah.createMetaString());
        p.addBatch();

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadCreatures() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, pX, pY, vX, vY, health, worldLink, front, metaString FROM Creature;");
        
        while(ergebnis.next()){
        	int type = ergebnis.getInt("type");
        	float pX = ergebnis.getFloat("pX");
        	float pY = ergebnis.getFloat("pY");
        	float vX = ergebnis.getFloat("vX");
        	float vY = ergebnis.getFloat("vY");
        	int health = ergebnis.getInt("health");
        	int linkIndex = ergebnis.getInt("worldLink");
        	Node worldLink = linkIndex == -1 ? null : allNodes[linkIndex];
        	boolean front = ergebnis.getBoolean("front");
        	String metaString = ergebnis.getString("metaString");

			try {
				if(type == -1){
					sarah = Sarah.createNewSarah(pX, pY, vX, vY, health, worldLink, metaString);
				} else {
					CreatureType cType = CreatureType.values()[type];
					Creature c = cType.create.create(pX, pY, vX, vY, health, worldLink, front, metaString);
					creatures[type].add(c);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveWorldObjects() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'WorldObject'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO WorldObject (type, x, y, worldLink, front, metaString) VALUES (?,?,?,?,?,?);");
        
        for(int type = 0; type < worldObjects.length; type++) for(WorldObject object : worldObjects[type]){
        	int i = 1;
            p.setInt(i++, type);
            p.setFloat(i++, object.pos.x);
            p.setFloat(i++, object.pos.y);
            p.setInt(i++, object.worldLink == null ? -1 : object.worldLink._id);
            p.setBoolean(i++, object.front);
            p.setString(i++, object.createMetaString());
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadWorldObjects() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, x, y, worldLink, front, metaString FROM WorldObject;");
        
        while(ergebnis.next()){
        	int type = ergebnis.getInt("type");
        	float x = ergebnis.getFloat("x");
        	float y = ergebnis.getFloat("y");
        	int linkIndex = ergebnis.getInt("worldLink");
        	Node worldLink = linkIndex == -1 ? null : allNodes[linkIndex];
        	boolean front = ergebnis.getBoolean("front");
        	String metaString = ergebnis.getString("metaString");

			try {
				ObjectType oType = ObjectType.values()[type];
				WorldObject o = oType.create.create(x, y, worldLink, front, metaString);

				worldObjects[type].add(o);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveItems() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'WorldItem'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO WorldItem (item, x, y, front, worldLink) VALUES (?,?,?,?,?);");
        
        for(List<WorldItem> list : World.items) for(WorldItem item : list){
        	int i = 1;
            p.setInt(i++, item.item.id);
            p.setFloat(i++, item.pos.x);
            p.setFloat(i++, item.pos.y);
            p.setBoolean(i++, item.front);
            p.setInt(i++, item.worldLink == null ? -1 : item.worldLink._id);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadItems() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT item, x, y, front, worldLink FROM WorldItem;");
        
        while(ergebnis.next()){
        	Item item = Item.list.get(ergebnis.getInt("item"));
        	float x = ergebnis.getFloat("x");
        	float y = ergebnis.getFloat("y");
        	boolean front = ergebnis.getBoolean("front");
        	int linkIndex = ergebnis.getInt("worldLink");
        	Node worldLink = linkIndex == -1 ? null : allNodes[linkIndex];

			WorldItem wItem = new WorldItem(item, new Vec(x, y), worldLink);
			wItem.front = front;
			items[item.id].add(wItem);
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveZones() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'Zone'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Zone (type, start, end) VALUES (?,?,?);");
        
        for(Zone zone : zones){
        	int i = 1;
            p.setInt(i++, zone.type.ordinal());
            p.setFloat(i++, zone.start);
            p.setFloat(i++, zone.end);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadZones() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, start, end FROM Zone;");
        
        while(ergebnis.next()){
        	int type = ergebnis.getInt("type");
        	float start = ergebnis.getFloat("start");
        	float end = ergebnis.getFloat("end");

			Zone zone = new Zone(ZoneType.values()[type]);
			zone.start = start;
			zone.end = end;
			zones.add(zone);
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveGenerators() throws SQLException {
//		Generators
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'Generator'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Generator (right, x, y, zone) VALUES (?,?,?,?);");
        
        int i = 1;
        p.setBoolean(i++, true);
        p.setFloat(i++, rightGenerator.pos.x);
        p.setFloat(i++, rightGenerator.pos.y);
        p.setInt(i++, zones.indexOf(rightGenerator.zone));
        p.addBatch();

        i = 1;
        p.setBoolean(i++, false);
        p.setFloat(i++, leftGenerator.pos.x);
        p.setFloat(i++, leftGenerator.pos.y);
        p.setInt(i++, zones.indexOf(leftGenerator.zone));
        p.addBatch();

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
		
//		Structures
		s = db.conn.createStatement();
		s.execute("DELETE FROM 'Structure'");
		s.close();
		
        p = db.conn.prepareStatement("INSERT INTO Structure (generator, level, ind, stepPos) VALUES (?,?,?,?);");
        
        for(Structure structure : rightGenerator.levels){
        	i = 1;
            p.setBoolean(i++, true);
            p.setInt(i++, structure.level);
            p.setInt(i++, structure.index);
            p.setInt(i++, structure.stepPos);
            p.addBatch();
        }
        for(Structure structure : leftGenerator.levels){
        	i = 1;
            p.setBoolean(i++, false);
            p.setInt(i++, structure.level);
            p.setInt(i++, structure.index);
            p.setInt(i++, structure.stepPos);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
		
//		Layers
		s = db.conn.createStatement();
		s.execute("DELETE FROM 'Layer'");
		s.close();
		
        p = db.conn.prepareStatement("INSERT INTO Layer (generator, material, thickness, aimThickness, resizeStep, priority, endNodeT, endNodeB, reachedAim, shrink) VALUES (?,?,?,?,?,?,?,?,?,?);");
        
        for(Layer layer : rightGenerator.layers){
        	i = 1;
            p.setBoolean(i++, true);
            p.setString(i++, layer.aim.material.name());
            p.setFloat(i++, layer.thickness);
            p.setFloat(i++, layer.aim.thickness);
            p.setFloat(i++, layer.aim.resizeStep);
            p.setInt(i++, layer.aim.priority);
            p.setInt(i++, layer.endNodeTop._id);
            p.setInt(i++, layer.endNodeBot._id);
            p.setBoolean(i++, layer.reachedAim);
            p.setBoolean(i++, layer.shrimp);
            p.addBatch();
        }
        for(Layer layer : leftGenerator.layers){
        	i = 1;
            p.setBoolean(i++, false);
            p.setString(i++, layer.aim.material.name());
            p.setFloat(i++, layer.thickness);
            p.setFloat(i++, layer.aim.thickness);
            p.setFloat(i++, layer.aim.resizeStep);
            p.setInt(i++, layer.aim.priority);
            p.setInt(i++, layer.endNodeTop._id);
            p.setInt(i++, layer.endNodeBot._id);
            p.setBoolean(i++, layer.reachedAim);
            p.setBoolean(i++, layer.shrimp);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadGenerators() throws SQLException {
//		Generators
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT right, x, y, zone FROM Generator;");
        
        while(ergebnis.next()){
        	boolean right = ergebnis.getBoolean("right");
        	Zone zone = zones.get(ergebnis.getInt("zone"));
        	Vec pos = new Vec(ergebnis.getFloat("x"), ergebnis.getFloat("y"));
        	Generator bp = new Generator(right, pos, zone.type);
        	bp.zone = zone;
        	
        	if(right){
        		rightGenerator = bp;
        	} else {
        		leftGenerator = bp;
        	}
        }
        ergebnis.close();
        sql.close();
        
//        Structures
        sql = db.conn.createStatement();

        ergebnis = sql.executeQuery("SELECT generator, level, ind, stepPos FROM Structure;");
        
        while(ergebnis.next()){
        	boolean right = ergebnis.getBoolean("generator");
        	int level = ergebnis.getInt("level");
        	int index = ergebnis.getInt("ind");
        	int stepPos = ergebnis.getInt("stepPos");
        	
        	if(right){
        		rightGenerator.levels[level].index = index;
        		rightGenerator.levels[level].stepPos = stepPos;
        		rightGenerator.levels[level].type = rightGenerator.zone.type.possibleStructures[level][index];
        	} else {
        		leftGenerator.levels[level].index = index;
        		leftGenerator.levels[level].stepPos = stepPos;
        		leftGenerator.levels[level].type = leftGenerator.zone.type.possibleStructures[level][index];
        	}
        }
        ergebnis.close();
        sql.close();
        
//        Layers
        sql = db.conn.createStatement();

        ergebnis = sql.executeQuery("SELECT generator, material, thickness, aimThickness, resizeStep, priority, endNodeT, endNodeB, reachedAim, shrink FROM Layer;");
        
        while(ergebnis.next()){
        	boolean right = ergebnis.getBoolean("generator");
        	Material mat = Material.valueOf(ergebnis.getString("material"));
        	float thickness = ergebnis.getFloat("thickness");
        	float aimThickness = ergebnis.getFloat("aimThickness");
        	float resizeStep = ergebnis.getFloat("resizeStep");
        	int priority = ergebnis.getInt("priority");
        	Node endNodeT = allNodes[ergebnis.getInt("endNodeT")];
        	Node endNodeB = allNodes[ergebnis.getInt("endNodeB")];
        	boolean reachedAim = ergebnis.getBoolean("reachedAim");
        	boolean shrink = ergebnis.getBoolean("shrink");
        	
    		
        	if(right){
	    		Layer layer = new Layer(new AimLayer(mat, aimThickness, resizeStep, priority), thickness, endNodeT, endNodeB, true);
	    		layer.shrimp = shrink;
	    		layer.reachedAim = reachedAim;
        		rightGenerator.layers.add(layer);
        	} else {
	    		Layer layer = new Layer(new AimLayer(mat, aimThickness, resizeStep, priority), thickness, endNodeT, endNodeB, false);
	    		layer.shrimp = shrink;
	    		layer.reachedAim = reachedAim;
        		leftGenerator.layers.add(layer);
        	}
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveInventory() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("DELETE FROM 'ItemStack'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO ItemStack (slot, item, count) VALUES (?,?,?);");
        
        for(ItemStack stack : Inventory.stacks){
        	int i = 1;
            p.setInt(i++, stack.slot);
            p.setInt(i++, stack.item.id);
            p.setInt(i++, stack.count);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.close();
	}

	private static void loadInventory() throws SQLException{
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT slot, item, count FROM ItemStack");
        
        while(ergebnis.next()){
        	int slot = ergebnis.getInt("slot");
        	int item = ergebnis.getInt("item");
        	int count = ergebnis.getInt("count");

			Inventory.stacks[slot].item = Item.list.get(item);
			Inventory.stacks[slot].count = count;
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void createDatabase() throws SQLException{
		Statement s = db.conn.createStatement();
		s.executeUpdate(
				  "DROP TABLE IF EXISTS 'World';"
				+ "CREATE TABLE 'World' (wName VARCHAR, nodeAmount INTEGER, invSelectedItem INTEGER);"
				+ "DROP TABLE IF EXISTS 'Node';"
				+ "CREATE TABLE 'Node' (n_ID INTEGER, mat VARCHAR, next_ID INTEGER, last_ID INTEGER, x FLOAT, y FLOAT);"
				+ "DROP TABLE IF EXISTS 'Creature';"
				+ "CREATE TABLE 'Creature' (type INTEGER, pX FLOAT, pY FLOAT, vX FLOAT, vY FLOAT, health INTEGER, worldLink INTEGER, front BOOLEAN, metaString VARCHAR);"
				+ "DROP TABLE IF EXISTS 'WorldObject';"
				+ "CREATE TABLE 'WorldObject' (type INTEGER, x FLOAT, y FLOAT, worldLink INTEGER, front BOOLEAN, metaString VARCHAR);"
				+ "DROP TABLE IF EXISTS 'WorldItem';"
				+ "CREATE TABLE 'WorldItem' (item INTEGER, x FLOAT, y FLOAT, front BOOLEAN, worldLink INTEGER);"
				+ "DROP TABLE IF EXISTS 'Zone';"
				+ "CREATE TABLE 'Zone' (type INTEGER, start FLOAT, end FLOAT);"
				+ "DROP TABLE IF EXISTS 'Generator';"
				+ "CREATE TABLE 'Generator' (right BOOLEAN, x FLOAT, y FLOAT, zone INTEGER);"
				+ "DROP TABLE IF EXISTS 'Structure';"
				+ "CREATE TABLE 'Structure' (generator BOOLEAN, level INTEGER, ind INTEGER, stepPos INTEGER);"
				+ "DROP TABLE IF EXISTS 'Layer';"
				+ "CREATE TABLE 'Layer' (generator BOOLEAN, material VARCHAR, thickness FLOAT, aimThickness FLOAT, resizeStep FLOAT, priority INTEGER, endNodeT INTEGER, endNodeB INTEGER, reachedAim BOOLEAN, shrink BOOLEAN);"
				+ "DROP TABLE IF EXISTS 'ItemStack';"
				+ "CREATE TABLE 'ItemStack' (slot INTEGER, item INTEGER, count INTEGER);"
				);
		s.close();
	}
}
