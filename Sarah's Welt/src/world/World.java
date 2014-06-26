package world;

import item.Inventory;
import item.Item;
import item.ItemStack;
import item.WorldItem;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import resources.Lightmap;
import resources.TextureFile;
import util.Database;
import util.Tessellator;
import world.BasePoint.AimLayer;
import world.BasePoint.Layer;
import world.BasePoint.Structure;
import world.BasePoint.ZoneType;
import world.WorldView.Zone;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Cow;
import world.creatures.Creature;
import world.creatures.Gnat;
import world.creatures.Heart;
import world.creatures.Panda;
import world.creatures.Rabbit;
import world.creatures.Sarah;
import world.creatures.Scorpion;
import world.creatures.Snail;
import world.creatures.Trex;
import world.creatures.Unicorn;
import world.worldObjects.Bamboo;
import world.worldObjects.Bush;
import world.worldObjects.Cactus;
import world.worldObjects.CandyBush;
import world.worldObjects.CandyFlower;
import world.worldObjects.CandyTree;
import world.worldObjects.Cloud;
import world.worldObjects.Crack;
import world.worldObjects.Flower;
import world.worldObjects.Fossil;
import world.worldObjects.Grass_tuft;
import world.worldObjects.PalmTree;
import world.worldObjects.Tree;
import world.worldObjects.WorldObject;
import core.Window;
import core.geom.Vec;

public class World {

	public static float measureScale = 50;
	public static String name;

	public static BasePoint rightGenerator, leftGenerator;
	
	public static Sarah sarah;
	public static List<Node> nodes;
	public static List<Creature>[] creatures; public static List<Class<? extends Creature>> creatureTypes;
	public static List<WorldObject>[] worldObjects; public static List<Class<? extends WorldObject>> objectTypes;
	public static List<WorldItem> items;
	public static List<Zone> zones;
	
	public static Database db;
	
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
		Inventory.reset();
			//items
		items = new ArrayList<>();
			//worldObjects
		objectTypes = Arrays.asList(new Class[]{
				Tree.class,
				PalmTree.class,
				CandyTree.class,
				Bush.class,
				CandyBush.class,
				Cactus.class,
				Flower.class, 
				CandyFlower.class,
				Bamboo.class,
				Grass_tuft.class,
				Cloud.class,
				Crack.class,
				Fossil.class
		});
		
		worldObjects = (List<WorldObject>[]) new List<?>[objectTypes.size()];
		for(int i = 0; i < worldObjects.length; i++){
			worldObjects[i] = new ArrayList<>();
		}
			//creatures
		creatureTypes = Arrays.asList(new Class[]{
				Snail.class,
				Butterfly.class,
				Heart.class,
				Rabbit.class,
				Bird.class,
				Panda.class, 
				Cow.class,
				Gnat.class,
				Unicorn.class,
				Scorpion.class,
				Trex.class
		});
		
		creatures = (List<Creature>[]) new List<?>[creatureTypes.size()];
		for(int i = 0; i < creatures.length; i++){
			creatures[i] = new ArrayList<>();
		}
		//setup world and generators
		zones = new ArrayList<>();
		db = new Database("world/", name);
		boolean fresh = !loadGeneralInformation();
		if(fresh){
			rightGenerator = new BasePoint(true, new Vec(0, 0));
			leftGenerator = rightGenerator.setupLayers();
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
		s.execute("TRUNCATE TABLE 'World'");
		s.close();
		s = db.conn.createStatement();
		s.execute("INSERT INTO 'World' (wName, nodeAmount, invSelectedItem) VALUES (" + name + "," + Node.indexIndex + "," + Inventory.selectedItem + ")");
		s.close();
	}

	private static boolean loadGeneralInformation(){
		try {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT wName, nodeAmount, invSelectedItem FROM World");

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
		s.execute("TRUNCATE TABLE 'Node'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Node (n_ID, mat, next_ID, last_ID, x, y) VALUES (?,?,?,?,?)");
        
        for(Node node : nodes){
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
        p.closeOnCompletion();
	}
	static Node[] allNodes;
	private static void loadNodes() throws SQLException{
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT n_ID, mat, next_ID, last_ID, x, y FROM Node");
        
        allNodes = new Node[Node.indexIndex];
        int[] nextNodes = new int[Node.indexIndex];
        int[] lastNodes = new int[Node.indexIndex];
        
        while(ergebnis.next()){
        	int n_ID = ergebnis.getInt("n_ID");
        	String mat = ergebnis.getString("mat");
        	int next_ID = ergebnis.getInt("next_ID");
        	int last_ID = ergebnis.getInt("last_ID");
        	int x = ergebnis.getInt("x");
        	int y = ergebnis.getInt("y");
        	
        	Node node = new Node(x, y, Material.valueOf(mat));
        	node._id = n_ID;
        	nextNodes[n_ID] = next_ID;
        	lastNodes[n_ID] = last_ID;

        	nodes.add(node);
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
		s.execute("TRUNCATE TABLE 'Creature'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Creature (type, pX, pY, vX, vY, health, worldLink, front, metaString) VALUES (?,?,?,?,?,?,?,?,?)");
        
        for(int type = 0; type < creatures.length; type++) for(Creature creature : creatures[type]){
        	int i = 1;
            p.setInt(i++, type);
            p.setFloat(i++, creature.pos.x);
            p.setFloat(i++, creature.pos.y);
            p.setFloat(i++, creature.vel.x);
            p.setFloat(i++, creature.vel.y);
            p.setInt(i++, creature.health);
            p.setInt(i++, creature.worldLink._id);
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
        p.setInt(i++, sarah.worldLink._id);
        p.setBoolean(i++, sarah.front);
        p.setString(i++, sarah.createMetaString());
        p.addBatch();

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.closeOnCompletion();
	}

	private static void loadCreatures() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, pX, pY, vX, vY, health, worldLink, front, metaString FROM Creature");
        
        while(ergebnis.next()){
        	int type = ergebnis.getInt("type");
        	float pX = ergebnis.getFloat("pX");
        	float pY = ergebnis.getFloat("pY");
        	float vX = ergebnis.getFloat("vX");
        	float vY = ergebnis.getFloat("vY");
        	int health = ergebnis.getInt("health");
        	Node worldLink = allNodes[ergebnis.getInt("worldLink")];
        	boolean front = ergebnis.getBoolean("front");
        	String metaString = ergebnis.getString("metaString");

			try {
				Class<? extends Creature> cl = type != -1 ? creatureTypes.get(type) : Sarah.class;
				Constructor<? extends Creature> cons = cl.getDeclaredConstructor(Integer.class, Float.class, Float.class, Float.class, Float.class, Integer.class, Node.class, Boolean.class, String.class);
				Creature c = cons.newInstance(pX, pY, vX, vY, health, worldLink, front, metaString);
				
				if(!(c instanceof Sarah)){
					creatures[type].add(c);
				} else {
					sarah = (Sarah) c;
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
		s.execute("TRUNCATE TABLE 'WorldObject'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO WorldObject (type, x, y, worldLink, front, metaString) VALUES (?,?,?,?,?,?)");
        
        for(int type = 0; type < worldObjects.length; type++) for(WorldObject object : worldObjects[type]){
        	int i = 1;
            p.setInt(i++, type);
            p.setFloat(i++, object.pos.x);
            p.setFloat(i++, object.pos.y);
            p.setInt(i++, object.worldLink._id);
            p.setBoolean(i++, object.front);
            p.setString(i++, object.createMetaString());
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.closeOnCompletion();
	}

	private static void loadWorldObjects() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, x, y, worldLink, front, metaString FROM WorldObject");
        
        while(ergebnis.next()){
        	int type = ergebnis.getInt("type");
        	float x = ergebnis.getFloat("x");
        	float y = ergebnis.getFloat("y");
        	Node worldLink = allNodes[ergebnis.getInt("worldLink")];
        	boolean front = ergebnis.getBoolean("front");
        	String metaString = ergebnis.getString("metaString");

			try {
				Class<? extends WorldObject> cl = objectTypes.get(type);
				Constructor<? extends WorldObject> cons = cl.getDeclaredConstructor(Integer.class, Float.class, Float.class, Float.class, Float.class, Integer.class, Node.class, Boolean.class, String.class);
				WorldObject o = cons.newInstance(x, y, worldLink, front, metaString);

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
		s.execute("TRUNCATE TABLE 'WorldItem'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO WorldItem (item, x, y, front, worldLink) VALUES (?,?,?,?,?)");
        
        for(WorldItem item : items){
        	int i = 1;
            p.setInt(i++, item.item.id);
            p.setFloat(i++, item.pos.x);
            p.setFloat(i++, item.pos.y);
            p.setBoolean(i++, item.front);
            p.setInt(i++, item.worldLink._id);
            p.addBatch();
        }

        db.conn.setAutoCommit(false);
        p.executeBatch(); // Daten an DB senden
        db.conn.setAutoCommit(true);
        p.closeOnCompletion();
	}

	private static void loadItems() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT item, x, y, front, worldLink FROM WorldItem");
        
        while(ergebnis.next()){
        	Item item = Item.list.get(ergebnis.getInt("type"));
        	float x = ergebnis.getFloat("x");
        	float y = ergebnis.getFloat("y");
        	boolean front = ergebnis.getBoolean("front");
        	Node worldLink = allNodes[ergebnis.getInt("worldLink")];

			WorldItem wItem = new WorldItem(item, new Vec(x, y), worldLink);
			wItem.front = front;
			items.add(wItem);
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveZones() throws SQLException {
		Statement s = db.conn.createStatement();
		s.execute("TRUNCATE TABLE 'Zone'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Zone (type, start, end) VALUES (?,?,?)");
        
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
        p.closeOnCompletion();
	}

	private static void loadZones() throws SQLException {
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT type, start, end FROM Zone");
        
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
		s.execute("TRUNCATE TABLE 'Generator'");
		s.close();
		
        PreparedStatement p = db.conn.prepareStatement("INSERT INTO Generator (right, x, y, zone) VALUES (?,?,?,?)");
        
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
        p.closeOnCompletion();
		
//		Structures
		s = db.conn.createStatement();
		s.execute("TRUNCATE TABLE 'Structure'");
		s.close();
		
        p = db.conn.prepareStatement("INSERT INTO Structure (generator, level, index, stepPos) VALUES (?,?,?,?,?,?)");
        
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
        p.closeOnCompletion();
		
//		Layers
		s = db.conn.createStatement();
		s.execute("TRUNCATE TABLE 'Layer'");
		s.close();
		
        p = db.conn.prepareStatement("INSERT INTO Layer (generator, material, thickness, aimThickness, resizeStep, priority, endNodeT, endNodeB, reachedAim, shrink) VALUES (?,?,?,?,?,?,?,?,?,?)");
        
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
            p.setString(i++, layer.aim.material.name);
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
        p.closeOnCompletion();
	}

	private static void loadGenerators() throws SQLException {
//		Generators
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT right, x, y, zone FROM Generator");
        
        while(ergebnis.next()){
        	boolean right = ergebnis.getBoolean("right");
        	Vec pos = new Vec(ergebnis.getFloat("x"), ergebnis.getFloat("y"));
        	BasePoint bp = new BasePoint(right, pos, ZoneType.values()[ergebnis.getInt("zone")]);
        	
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

        ergebnis = sql.executeQuery("SELECT generator, level, index, stepPos FROM Structure");
        
        while(ergebnis.next()){
        	boolean right = ergebnis.getBoolean("generator");
        	int level = ergebnis.getInt("level");
        	int index = ergebnis.getInt("index");
        	int stepPos = ergebnis.getInt("stepPos");
        	
        	if(right){
        		rightGenerator.levels[level].index = index;
        		rightGenerator.levels[level].stepPos = stepPos;
        	} else {
        		leftGenerator.levels[level].index = index;
        		leftGenerator.levels[level].stepPos = stepPos;
        	}
        }
        ergebnis.close();
        sql.close();
        
//        Layers
        sql = db.conn.createStatement();

        ergebnis = sql.executeQuery("SELECT generator, material, thickness, aimThickness, resizeStep, priority, endNodeT, endNodeB, reachedAim, shrink FROM Layer");
        
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
        	
    		Layer layer = new Layer(new AimLayer(mat, aimThickness, resizeStep, priority), thickness, endNodeT, endNodeB);
    		layer.shrimp = shrink;
    		layer.reachedAim = reachedAim;
    		
        	if(right){
        		rightGenerator.layers.add(layer);
        	} else {
        		leftGenerator.layers.add(layer);
        	}
        }
        ergebnis.close();
        sql.close();
	}
	
	public static void saveInventory() throws SQLException {
		try {
			Statement s = db.conn.createStatement();
			s.execute("TRUNCATE TABLE 'ItemStack'");
			s.close();
			
            PreparedStatement p = db.conn.prepareStatement("INSERT INTO ItemStack (slot, item, count) VALUES (?,?,?)");
            
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
            p.closeOnCompletion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}

	private static void loadInventory() throws SQLException{
		Statement sql = db.conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT slot, item, count FROM Zone");
        
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
}
