package world;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Datenbank;

public class WorldDatabase extends Datenbank{
	
	public WorldDatabase(String worldName){
		super("worlds/", worldName);
		if(fresh){
			createDB();
		}
	}
	
	public void loadWorld(){
		try {
            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT xSector FROM World");//TODO
            
            ergebnis.next();
            WorldWindow.xSector = ergebnis.getInt("xSector");
            
            ergebnis.close();
            sql.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	public Sector loadSector(int x){
		return null;
	}
	
//	public MatArea loadAreasAt(int xSector){
//		try {
//            Statement sql = conn.createStatement();
//
//            ResultSet ergebnis = sql.executeQuery("SELECT pX, pY FROM Point P INNER JOIN Contains C ON P.p_ID = C.p_ID WHERE a_ID = " + area.id + " AND sX = " + sX);//TODO
//            
//            while (list.next()) {
//            	part.cycles[list.getInt("cycleIndex")].add(new Point(list.getFloat("pX"), list.getFloat("pY")));
//            }
//            
//            ergebnis.close();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//	}
	
	public void save(Node[] nodes, String mat){
		try {
            PreparedStatement p = conn.prepareStatement("INSERT INTO Node (n_ID, next_ID, last_ID, Material, p_x, p_y) VALUES (?,?,?,?,?,?)");
            
            for(Node node : nodes){
	            p.setInt(1, node.index);
	            p.setInt(2, node.nextIndex);
	            p.setInt(3, node.lastIndex);
	            p.setString(4, mat);
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
	
	 public void createDB() {
            try {
                Statement sql = conn.createStatement();

                String sqlCreate = 
            		"CREATE TABLE 'World' ('x' FLOAT NOT NULL DEFAULT 0);"
                +	"CREATE TABLE 'Node' ('n_ID' INTEGER PRIMARY KEY NOT NULL, 'n_ID' INTEGER PRIMARY KEY NOT NULL, 'next_ID' INTEGER PRIMARY KEY, 'last_ID' INTEGER PRIMARY KEY , 'pX' FLOAT NOT NULL  DEFAULT 0, 'pY' FLOAT NOT NULL  DEFAULT 0, 'cycleIndex' INTEGER);"
                +   "CREATE TABLE 'Cycle' ('c_ID' INTEGER PRIMARY KEY NOT NULL, 'Material' VARCHAR NOT NULL DEFAULT 'AIR')"
                ;

                sql.executeUpdate(sqlCreate);
                sql.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	    }
	
}
