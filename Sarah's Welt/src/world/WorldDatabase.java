package world;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import util.Database;

public class WorldDatabase extends Database{
	
	public boolean fresh;
	
	public WorldDatabase(String worldName){
		super("worlds", worldName);
		fresh = loadWorld();
	}
	
	public boolean didFirstSave;
	
	public boolean loadWorld(){
		try {
			loadWorldMetaData();
            
            
            return false;
        } catch (SQLException ex) {
    		createDB();
    		return loadWorld();
        }
	}
	
	public void loadWorldMetaData() throws SQLException {
		Statement sql = conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT nodeAmount FROM World");//TODO
        ergebnis.next();
        	Node.indexIndex = ergebnis.getInt("nodeAmount");
        ergebnis.close();
        sql.close();
	}
	
	public void loadWorldContours() throws SQLException {
		Statement sql = conn.createStatement();

        ResultSet ergebnis = sql.executeQuery("SELECT n_ID, Material, next_ID, last_ID, pX, pY FROM Node");
        
        Node[] allNodes = new Node[Node.indexIndex];
        
        while(ergebnis.next()){
        	int n_ID = ergebnis.getInt("n_ID");
        	String mat = ergebnis.getString("Material");
        	int next_ID = ergebnis.getInt("next_ID");
        	int last_ID = ergebnis.getInt("last_ID");
        	int pX = ergebnis.getInt("pX");
        	int pY = ergebnis.getInt("pY");
        	
        	Node node = new Node(pX, pY);
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
	}
	
	public void saveChanged(List<Node>[] contours){
		for(int mat = 0; mat < contours.length; mat++){
			List<Node> nodes = contours[mat];
			try {
	            PreparedStatement p = conn.prepareStatement("UPDATE Node SET next_ID = ?, last_ID = ?, pX = ?, pY = ? WHERE n_ID = ?");
	            
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
	            PreparedStatement p = conn.prepareStatement("INSERT INTO Node (n_ID, Material, next_ID, last_ID, pX, pY) VALUES (?,?,?,?,?)");
	            
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
	
	public void saveGenerators(BasePoint p1, BasePoint p2){
		try {
			Statement s = conn.createStatement();
			
			s.execute("UPDATE Generators SET )
			
			s.close();
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	 public void createDB(){
        try {
            Statement sql = conn.createStatement();

            String sqlCreate =
            	"CREATE TABLE 'World' ('nodeAmount' INT NOT NULL DEFAULT 0);"
            +   "INSERT INTO World (nodeAmount) VALUES (0);"
            			
	    	+	"CREATE TABLE 'Generators' ('right' BOOLEAN PRIMARY KEY, 'Zone' FLOAT, 'xPos' FLOAT, 'yPos' FLOAT, );"
	        +   "INSERT INTO Generators (nodeAmount) VALUES (0);"
	        +   "INSERT INTO Generators (nodeAmount) VALUES (0);"
	        
	        +	"CREATE TABLE 'Zones' ('start' FLOAT PRIMARY KEY, 'Type' VARCHAR, 'end' FLOAT);"
            +	"CREATE TABLE 'Node' ('n_ID' INTEGER PRIMARY KEY NOT NULL, 'Material' VARCHAR NOT NULL DEFAULT 'AIR', 'next_ID' INTEGER, 'last_ID' INTEGER, 'pX' FLOAT DEFAULT 0, 'pY' FLOAT DEFAULT 0);"
            ;

            sql.executeUpdate(sqlCreate);
            sql.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	
}
