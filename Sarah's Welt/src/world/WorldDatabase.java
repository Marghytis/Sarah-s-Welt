package world;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Datenbank;
import core.geom.Vec;

public class WorldDatabase extends Datenbank{
	
	public WorldDatabase(String worldName){
		super("worlds", worldName);
		if(fresh){
			createDB();
		}
	}
	
	public void loadWorld(){
		try {
            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT Name, x FROM World");//TODO
            
            ergebnis.next();
            	World.load(ergebnis.getString("Name"), ergebnis.getInt("x"));
            ergebnis.close();
            sql.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	public void save(Node[] nodes, String mat){
		try {
            PreparedStatement p = conn.prepareStatement("INSERT INTO Node (n_ID, Material, next_ID, last_ID, p_x, p_y) VALUES (?,?,?,?,?)");
            
            for(Node node : nodes){
	            p.setInt(1, node.index);
	            p.setString(2, mat);
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
	
	public void load(Node last, int amount, Node next){
		if(last.nextIndex != -1){
			(new Exception("[WorldDatabase]: Can't load a Node twice!!!!!")).printStackTrace();
			return;
		}
		try {
          Statement sql = conn.createStatement();

          
          Node n = last;
          
          for(int i = 0; i < amount; i++) {
              ResultSet ergebnis = sql.executeQuery("SELECT * FROM Node N WHERE n_ID = " + last.nextIndex);//TODO
              
        	  Node node = new Node(ergebnis.getInt("n_ID"), new Vec(ergebnis.getFloat("pX"), ergebnis.getFloat("pY")), n);
        	  n.setNext(node);
        	  n = node;

              ergebnis.close();
          }
          
          if(n.nextIndex == next.index){
        	  n.setNext(next);
        	  next.setLast(n);
          }

      } catch (SQLException ex) {
          ex.printStackTrace();
          return;
      }
	}
	
	 public void createDB(){
        try {
            Statement sql = conn.createStatement();

            String sqlCreate = 
        		"CREATE TABLE 'World' ('x' FLOAT NOT NULL DEFAULT 0);"
            +	"CREATE TABLE 'Node' ('n_ID' INTEGER PRIMARY KEY NOT NULL, 'Material' VARCHAR NOT NULL DEFAULT 'AIR', 'next_ID' INTEGER, 'last_ID' INTEGER, 'pX' FLOAT DEFAULT 0, 'pY' FLOAT DEFAULT 0);"
            ;

            sql.executeUpdate(sqlCreate);
            sql.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	
}
