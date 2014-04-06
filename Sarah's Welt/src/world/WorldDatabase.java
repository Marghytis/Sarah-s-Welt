package world;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.Game;
import util.Datenbank;
import world.Area.AreaPart;

public class WorldDatabase extends Datenbank{

	public String pathAndName = "";
	
	public WorldDatabase(String worldName){
		pathAndName = "worlds/" + worldName;
	}
	
	/**
	 * Ein AreaPart wird aus der Datenbank ausgelesen.
	 * @param xWert
	 * @return
	 */
	public AreaPart getAreaPart(Area area, int sX){
		
		if(!false/*Not there yet*/){
			WorldWindow.generator.generateRight();
		}
		
		AreaPart part = area.new AreaPart();
		
		Connection conn = db_open("worlds/", WorldWindow.worldName);
		
        try {

            Statement sql = conn.createStatement();

            String sqlAnfrage = "SELECT pX, pY FROM Point P INNER JOIN Contains C ON P.p_ID = C.p_ID WHERE a_ID = " + area.id + " AND sX = " + sX;

            ResultSet ergebnis = sql.executeQuery(sqlAnfrage);

            while (ergebnis.next()) {
            	part.cycles[ergebnis.getInt("cycleIndex")].add(new Point(ergebnis.getFloat("pX"), ergebnis.getFloat("pY")));
            }

            ergebnis.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return part;
    
	}
	
	
	public void saveSectorAt(int xWert){
		
	}
	
	 public boolean createDB(String name) {
	        Connection conn = db_open("worlds/", name + ".sqlite");
            try {
                Statement sql = conn.createStatement();

                String sqlCreate = 
                "CREATE TABLE 'Area' ('a_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Material' VARCHAR NOT NULL  DEFAULT 'AIR');" +
                "CREATE TABLE 'Contains' ('a_ID' INTEGER NOT NULL , 'sX' INTEGER NOT NULL , 'p_ID' INTEGER NOT NULL , 'cycleIndex' INTEGER);" +
                "CREATE TABLE 'Point' ('p_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'pX' FLOAT NOT NULL  DEFAULT 0, 'pY' FLOAT NOT NULL  DEFAULT 0);";
;

                sql.executeUpdate(sqlCreate);
                conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	        return true;
	    }
	
}
