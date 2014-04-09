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
	
	public void saveSector(Sector sec){
		try {
            PreparedStatement p = conn.prepareStatement("INSERT INTO Point (p_ID, pX, pY) VALUES (?,?,?)");

            for(MatArea a : sec.areas){
            	for(Node c : a.cycles){
		            p.setInt(1, name);
		            p.setString(2, vorname);
		            p.addBatch();
            	}
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
            		"CREATE TABLE 'World' ('xSector' INTEGER NOT NULL DEFAULT 0);"
                +	"CREATE TABLE 'Node' ('Material' VARCHAR NOT NULL  DEFAULT 'AIR' , 'sX' INTEGER NOT NULL , 'p_ID' INTEGER NOT NULL , 'cycleIndex' INTEGER);"
                +	"CREATE TABLE 'Point' ('p_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'pX' FLOAT NOT NULL  DEFAULT 0, 'pY' FLOAT NOT NULL  DEFAULT 0);";
;

                sql.executeUpdate(sqlCreate);
                sql.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	    }
	
}
