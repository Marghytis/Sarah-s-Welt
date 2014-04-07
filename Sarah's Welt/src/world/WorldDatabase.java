package world;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Datenbank;
import world.Area.AreaPart;

public class WorldDatabase extends Datenbank{
	
	public WorldDatabase(String worldName){
		super("worlds/", worldName);
		if(fresh){
			createDB();
		}
	}
	
	public void loadWorld(){
		try {
        	Connection conn = db_open("worlds/", WorldWindow.worldName);

            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT xSector FROM World");//TODO
            
            ergebnis.next();
            WorldWindow.xSector = ergebnis.getInt("xSector");
            
            ergebnis.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	public AreaPart[] loadAreasAt(int xSector){
		try {
        	Connection conn = db_open("worlds/", WorldWindow.worldName);

            Statement sql = conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT pX, pY FROM Point P INNER JOIN Contains C ON P.p_ID = C.p_ID WHERE a_ID = " + area.id + " AND sX = " + sX);//TODO
            
            while (list.next()) {
            	part.cycles[list.getInt("cycleIndex")].add(new Point(list.getFloat("pX"), list.getFloat("pY")));
            }
            
            ergebnis.close();
            conn.close();
            return ergebnis;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
	}	
	
	public void saveSectorAt(int xWert){
		
	}
	
	 public void createDB() {
            try {
                Statement sql = conn.createStatement();

                String sqlCreate = 
                "CREATE TABLE 'Area' ('a_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Material' VARCHAR NOT NULL  DEFAULT 'AIR');" +
                "CREATE TABLE 'Contains' ('a_ID' INTEGER NOT NULL , 'sX' INTEGER NOT NULL , 'p_ID' INTEGER NOT NULL , 'cycleIndex' INTEGER);" +
                "CREATE TABLE 'Point' ('p_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'pX' FLOAT NOT NULL  DEFAULT 0, 'pY' FLOAT NOT NULL  DEFAULT 0);" +
                "CREATE TABLE 'World' ('xSector' INTEGER NOT NULL DEFAULT 0);";
;

                sql.executeUpdate(sqlCreate);
                conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	    }
	
}
