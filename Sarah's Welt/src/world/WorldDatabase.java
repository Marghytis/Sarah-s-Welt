package world;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import util.Datenbank;

public class WorldDatabase extends Datenbank{
	
	public WorldDatabase(String worldName){
		super("worlds", worldName);
		if(fresh){
			createDB();
		}
	}
	
	public boolean didFirstSave;
	
	public void loadWorld(){
		try {
            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT nodeAmount FROM World");//TODO
            
            ergebnis.next();
            	Node.indexIndex = ergebnis.getInt("nodeAmount");
            ergebnis.close();
            sql.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
		try {
            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT n_ID, Material, next_ID, last_ID, p_x, p_y FROM Node");
            
            Node[] allNodes = new Node[Node.indexIndex];
            
            while(ergebnis.next()){
            	int n_ID = ergebnis.getInt("n_ID");
            	String mat = ergebnis.getString("Material");
            	int next_ID = ergebnis.getInt("next_ID");
            	int last_ID = ergebnis.getInt("last_ID");
            	int p_x = ergebnis.getInt("p_x");
            	int p_y = ergebnis.getInt("p_y");
            	
            	Node node = new Node(p_x, p_y);
            	node.index = n_ID;
            	node.nextIndex = next_ID;
            	node.lastIndex = last_ID;
            	
            	WorldView.contours[Material.valueOf(mat).ordinal()].add(node);
            	allNodes[n_ID] = node;
            }
            
            for(Node n : allNodes){
            	n.setLast(allNodes[n.lastIndex]);
            	n.setNext(allNodes[n.nextIndex]);
            }
            ergebnis.close();
            sql.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	public void saveChanged(List<Node>[] contours){
		for(int mat = 0; mat < contours.length; mat++){
			List<Node> nodes = contours[mat];
			try {
	            PreparedStatement p = conn.prepareStatement("UPDATE Node SET next_ID = ?, last_ID = ?, p_x = ?, p_y = ? WHERE n_ID = ?");
	            
	            for(Node node : nodes){
		            p.setInt(3, node.nextIndex);
		            p.setInt(4, node.lastIndex);
		            p.setFloat(5, node.getPoint().x);
		            p.setFloat(6, node.getPoint().y);
		            p.setFloat(7, node.index);
		            p.addBatch();
	            }
	
	            conn.setAutoCommit(false);
	            p.executeBatch(); // Daten an DB senden
	            conn.setAutoCommit(true);
	
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	public void saveNew(List<Node>[] contours){
		for(int mat = 0; mat < contours.length; mat++){
			List<Node> nodes = contours[mat];
			String material = Material.values()[mat].name;
			try {
	            PreparedStatement p = conn.prepareStatement("INSERT INTO Node (n_ID, Material, next_ID, last_ID, p_x, p_y) VALUES (?,?,?,?,?)");
	            
	            for(Node node : nodes){
		            p.setInt(1, node.index);
		            p.setString(2, material);
		            p.setInt(3, node.nextIndex);
		            p.setInt(4, node.lastIndex);
		            p.setFloat(5, node.getPoint().x);
		            p.setFloat(6, node.getPoint().y);
		            p.addBatch();
	            }
	
	            conn.setAutoCommit(false);
	            p.executeBatch(); // Daten an DB senden
	            conn.setAutoCommit(true);
	
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	public void firstSave(List<Node>[] contours){

        try {
        	Statement sql = conn.createStatement();
			sql.execute("DROP TABLE IF EXISTS 'Node'");
			sql.execute("CREATE TABLE 'Node' ('n_ID' INTEGER PRIMARY KEY NOT NULL, 'Material' VARCHAR NOT NULL DEFAULT 'AIR', 'next_ID' INTEGER, 'last_ID' INTEGER, 'pX' FLOAT DEFAULT 0, 'pY' FLOAT DEFAULT 0)");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("COULDN'T SAVE WORLD DATA!!!!");
			return;
		}
		
		for(int mat = 0; mat < contours.length; mat++){
			List<Node> nodes = contours[mat];
			String material = Material.values()[mat].name;
			try {
	            PreparedStatement p = conn.prepareStatement("INSERT INTO Node (n_ID, Material, next_ID, last_ID, p_x, p_y) VALUES (?,?,?,?,?)");
	            
	            for(Node node : nodes){
		            p.setInt(1, node.index);
		            p.setString(2, material);
		            p.setInt(3, node.nextIndex);
		            p.setInt(4, node.lastIndex);
		            p.setFloat(5, node.getPoint().x);
		            p.setFloat(6, node.getPoint().y);
		            p.addBatch();
	            }
	
	            conn.setAutoCommit(false);
	            p.executeBatch(); // Daten an DB senden
	            conn.setAutoCommit(true);
	
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	 public void createDB(){
        try {
            Statement sql = conn.createStatement();

            String sqlCreate = 
        		"CREATE TABLE 'World' ('nodeAmount' INT NOT NULL DEFAULT 0);"
            +	"CREATE TABLE 'Node' ('n_ID' INTEGER PRIMARY KEY NOT NULL, 'Material' VARCHAR NOT NULL DEFAULT 'AIR', 'next_ID' INTEGER, 'last_ID' INTEGER, 'pX' FLOAT DEFAULT 0, 'pY' FLOAT DEFAULT 0);"
            ;

            sql.executeUpdate(sqlCreate);
            sql.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	
}
