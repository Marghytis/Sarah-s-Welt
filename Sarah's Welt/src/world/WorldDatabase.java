package world;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.DB_Anfragen;
import db.Hilfsfunktionen;
import util.Datenbank;

public class WorldDatabase extends Datenbank{

	/**
	 * Ein Sektor wird aus der Datenbank ausgelesen.
	 * @param xWert
	 * @return
	 */
	public Sector getSectorAt(int xWert){
		
		Sector sector= new Sector(xWert);
		sector.lines
		
		
		
		
		
		// Liste zum Speichern der Namen aus der DB
        ArrayList<String> liste = new ArrayList<String>();

        // DB-Verbindung herstellen
        db_open(SQLITEVZ, SQLITEDB);
        try {
            // Verbindungsobjekt zur aktuellen DB holen
            conn = this.getConnectionToActualDB();

            // Statement-Objekt  holen
            Statement sql = conn.createStatement();

            // String mit Anfrage formulieren
            sqlAnfrage = "SELECT name, vorname FROM namen ORDER BY name, vorname";

            // Anfrage ausf√ºhren lassen und in Resultset speichern
            ResultSet ergebnis = sql.executeQuery(sqlAnfrage);

            // Ergebnis verarbeiten
            while (ergebnis.next()) {
                liste.add(ergebnis.getString("name") + ", " + ergebnis.getString("vorname"));
            }

            // Verbindungen zur DB schlie√üen
            ergebnis.close();
            conn.close();

        } catch (SQLException ex) {
            // Fehler abfangen
            Hilfsfunktionen.myDebug("Fehler in getNamensliste(): " + ex.getLocalizedMessage());
        }

        // Ergebnisliste zur√ºckgeben
        return liste;
    
	}
	
	/**
	 * Der Sektor mit dem Prim‰rschl¸ssel "xWert" wird eingespeichert.
	 * @param xWert
	 */
	public void saveSectorAt(int xWert){
		
	}
	
	 public boolean createDB(String name) {
	        Connection conn = db_open("worlds/", name + ".sqlite");
            try {
                Statement sql = conn.createStatement();

                String sqlCreate = 
                "DROP TABLE IF EXISTS 'Area';"+
                "CREATE TABLE 'Area' ('a_ID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Material' VARCHAR NOT NULL  DEFAULT AIR);" +
                "DROP TABLE IF EXISTS 'AreaPart';" +
                "CREATE TABLE 'AreaPart' ('a_ID' INTEGER NOT NULL , 'sX' INTEGER NOT NULL , PRIMARY KEY ('a_ID', 'sX'));" +
                "DROP TABLE IF EXISTS 'Contains';" +
                "CREATE TABLE 'Contains' ('a_ID' INTEGER NOT NULL , 'sX' INTEGER NOT NULL , 'p_ID' INTEGER NOT NULL );" +
                "DROP TABLE IF EXISTS 'Point';" +
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
