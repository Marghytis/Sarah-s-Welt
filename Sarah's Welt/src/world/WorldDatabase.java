package world;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	
}
