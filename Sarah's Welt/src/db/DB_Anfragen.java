package db_beispiel;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Christopher Bräuer
 */
public class DB_Anfragen extends DB_Verbinder {

    private static String SQLITEVZ = "";
    private static final String SQLITEDB = "Namen.sqlite"; // Name der Datenbankdatei
    private static final String CLASSNAME = "GUI.class"; // Hier muss ein Klassenname stehen!
    private static final String PACKAGENAME = "db_beispiel"; // Hier muss der Paketname stehen!
    private static final String DISTNAME = "DB_Beispiel.jar"; // Hier muss der Distributionsname stehen!

// TODO: Hier muss der Pfad entsprechend dem Paketnamen angepasst werden
    static URL url = GUI.class.getResource(CLASSNAME);

    public DB_Anfragen() {
        SQLITEVZ = getVerzeichnis();
    }

    public static String getVerzeichnis() {
        Hilfsfunktionen.myDebug("OS  : " + System.getProperty("os.name"));
        Hilfsfunktionen.myDebug("PATH: " + url.getPath() + "\n\n");

        if (SQLITEVZ.trim().equals("")) {

            Hilfsfunktionen.myDebug("OS  : " + System.getProperty("os.name"));
            Hilfsfunktionen.myDebug("PATH: " + url.getPath());

            if (url.getPath().matches(".*build.*")) {

                SQLITEVZ = url.getPath().replace("/build/classes/" + PACKAGENAME + "/" + CLASSNAME, "").replace("file:", "");
                Hilfsfunktionen.myDebug("DEV");
            } else {
                SQLITEVZ = url.getPath().replace(DISTNAME + "!/" + PACKAGENAME + "/" + CLASSNAME, "").replace("file:", "");
                Hilfsfunktionen.myDebug("PROD");
            }

            if (System.getProperty("os.name").matches(".*Windows.*")) {
                Hilfsfunktionen.myDebug("WINDOWS! Ersetze Leerzeichen in Verzeichnisnamen.");
                SQLITEVZ = SQLITEVZ.replaceAll("%20", " ");
                SQLITEVZ = SQLITEVZ.substring(1);
            } else {
                SQLITEVZ = SQLITEVZ.replaceAll("%20", "\\ ");
                SQLITEVZ += System.getProperty("file.separator");
            }

            SQLITEVZ = SQLITEVZ.replaceAll("%c3%a4", "ä");
            SQLITEVZ = SQLITEVZ.replaceAll("%c3%bc", "ü");
            SQLITEVZ = SQLITEVZ.replaceAll("%c3%b6", "ö");
            SQLITEVZ = SQLITEVZ.replaceAll("%c3%9f", "ß");

            Hilfsfunktionen.myDebug("VZ  : " + SQLITEVZ + "\n");
        }

        return SQLITEVZ;
    }

    /**
     * Holt alle Namen in einer ArrayList
     *
     * @return ArrayList mit Namen
     */
    public ArrayList<String> getNamensliste() {

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

            // Anfrage ausführen lassen und in Resultset speichern
            ResultSet ergebnis = sql.executeQuery(sqlAnfrage);

            // Ergebnis verarbeiten
            while (ergebnis.next()) {
                liste.add(ergebnis.getString("name") + ", " + ergebnis.getString("vorname"));
            }

            // Verbindungen zur DB schließen
            ergebnis.close();
            conn.close();

        } catch (SQLException ex) {
            // Fehler abfangen
            Hilfsfunktionen.myDebug("Fehler in getNamensliste(): " + ex.getLocalizedMessage());
        }

        // Ergebnisliste zurückgeben
        return liste;
    }

    /**
     * BEISPIEL für INSERT Fügt einen neuen Datensatz ein.
     *
     * @param name - Der Nachname
     * @param vorname - Der Vorname
     * @return true, wenn erfolgreich, false, wenn nicht
     */
    public boolean addName(String name, String vorname) {
        db_open(SQLITEVZ, SQLITEDB);
        try {
            conn = getConnectionToActualDB();

            // PreparedStatement braucht man, wenn man Datensätze einfügen will
            PreparedStatement p = conn.prepareStatement("INSERT INTO namen (name, vorname) VALUES (?,?)");

            p.setString(1, name);
            p.setString(2, vorname);
            p.addBatch();

            conn.setAutoCommit(false);
            p.executeBatch(); // Daten an DB senden
            conn.setAutoCommit(true);

            conn.close();

        } catch (SQLException ex) {
            Hilfsfunktionen.myDebug("Fehler in addName(): " + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * BEISPIEL für DELETE Löscht den übergebenen Personennamen
     *
     * @param name - Der zu löschende Name
     */
    public void delName(String name) {

        // den Namen am Komma in Vor- und Nachname aufsplitten
        // und in ein Array schreiben
        // person[0] --> Nachname
        // person[1] --> Vorname
        String[] person = name.split(", ");

        // TODO: Hier den Code reinschreiben
    }

    /**
     * BEISPIEL für UPDATE Ändert den Personennamen
     *
     * @param person_alt - Array mit altem Vor- und Nachname
     * @param person - Array mit neuem Vor- und Nachname
     */
    public void updateName(String[] person_alt, String[] person) {
        System.out.println("Aktualisiere: " + person[0] + ", " + person[1]);

        db_open(SQLITEVZ, SQLITEDB);

        try {
            conn = this.getConnectionToActualDB();
            Statement sql = conn.createStatement();

            String sqlUpdate = "UPDATE namen SET name='" + person[0] + "', vorname='" + person[1] + "' WHERE name='" + person_alt[0] + "' AND vorname='" + person_alt[1] + "'";

            sql.execute(sqlUpdate);

            conn.close();
        } catch (SQLException ex) {
            Hilfsfunktionen.myDebug("Fehler in updateName(): " + ex.getLocalizedMessage());
        }
    }

    /**
     * BEISPIEL für das Erstellen einer neuen Datenbank.
     *
     * KOMMT HIER NICHT ZUM EINSATZ!
     *
     * @return true, falls erfolgreich; false, falls nicht erfolgreich *
     */
    public boolean createDB() {
        if (actualConnection != null) {
            try {
                Hilfsfunktionen.myDebug("\n--->>> createDBneu <<<---\n");

                Statement sql = conn.createStatement();

                String sqlCreate = "DROP TABLE IF EXISTS \"Blockzeiten\";"
                        + "CREATE TABLE \"Blockzeiten\" (\"lNr\" integer NOT NULL,\"tNr\" integer NOT NULL,\"zNr\" integer NOT NULL);"
                        + "DROP TABLE IF EXISTS \"BlockzeitenALLE\";"
                        + "CREATE TABLE \"BlockzeitenALLE\" (    \"tNr\" integer NOT NULL,    \"zNr\" integer NOT NULL);"
                        + "DROP TABLE IF EXISTS \"BlockzeitenSuS\";"
                        + "CREATE TABLE \"BlockzeitenSuS\" (    \"prNR\" integer NOT NULL,    \"tNr\" integer NOT NULL,    \"zNr\" integer NOT NULL);"
                        + "DROP TABLE IF EXISTS \"BlockzeitenStundenplan\";"
                        + "CREATE TABLE \"BlockzeitenStundenplan\" (\"lNr\" integer NOT NULL, \"tNr\" integer NOT NULL, \"zNr\" integer NOT NULL);"
                        + "DROP TABLE IF EXISTS \"Fach\";"
                        + "CREATE TABLE \"Fach\" (\"fk\" character varying(10) primary key NOT NULL ,\"bezeichnung\" character varying(50),\"afNR\" integer);"
                        + "INSERT INTO \"Fach\" VALUES('BIO','Biologie',3);"
                        + "INSERT INTO \"Fach\" VALUES('CH','Chemie',3);"
                        + "INSERT INTO \"Fach\" VALUES('D','Deutsch',1);"
                        + "INSERT INTO \"Fach\" VALUES('DS','Darst.Spiel',1);"
                        + "INSERT INTO \"Fach\" VALUES('DSP','Darst.Spiel',1);"
                        + "INSERT INTO \"Fach\" VALUES('E','Englisch',1);"
                        + "INSERT INTO \"Fach\" VALUES('EK','Erdkunde',2);"
                        + "INSERT INTO \"Fach\" VALUES('ETHI','Ethik',2);"
                        + "DROP TABLE IF EXISTS \"Konfiguration\";"
                        + "CREATE TABLE \"Konfiguration\" ( name character varying(20) NOT NULL primary key, wert character varying(20) NOT NULL);"
                        + "DROP TABLE IF EXISTS \"Lehrer\";"
                        + "CREATE TABLE \"Lehrer\" (   kuerzel character varying(10) NOT NULL,    name character varying(50) NOT NULL,    \"istVorsitz\" smallint DEFAULT 0 NOT NULL,    \"lNr\" integer NOT NULL primary key AUTOINCREMENT);"
                        + "DROP TABLE IF EXISTS \"PGKommentare\";"
                        + "CREATE TABLE \"PGKommentare\" (pgruppe integer NOT NULL primary key, kommentar character varying(200) NOT NULL );"
                        + "DROP TABLE IF EXISTS \"Pruefung\";"
                        + "CREATE TABLE \"Pruefung\" (    pruefling character varying(50) NOT NULL,    fk character varying(10) NOT NULL,    pruefer character varying(10) NOT NULL,    vorsitz character varying(10),    protokoll character varying(10),    zeit character varying(10),    tag character varying(30),    raum character varying(30),    pgruppe integer DEFAULT (-1),    reihenfolge integer DEFAULT 0,    \"prNR\" integer PRIMARY KEY  AUTOINCREMENT,    part character varying(5),     zp smallint NOT NULL DEFAULT 0,     tutor character varying(10));"
                        + "DROP TABLE IF EXISTS \"Raum\";"
                        + "CREATE TABLE \"Raum\" (    \"rNr\" integer NOT NULL primary key autoincrement,    raum character varying(30) NOT NULL);"
                        + "DROP TABLE IF EXISTS \"Tage\";"
                        + "CREATE TABLE \"Tage\" (    \"tNr\" integer NOT NULL primary key autoincrement,    tag character varying(30) NOT NULL,     tiw integer DEFAULT 0 NOT NULL );"
                        + "DROP TABLE IF EXISTS \"Zeiten\";"
                        + "CREATE TABLE \"Zeiten\" (    zeit character varying(30) NOT NULL,    \"zNr\" integer NOT NULL primary key autoincrement);"
                        + "DROP TABLE IF EXISTS \"Zusatzzeit\";"
                        + "CREATE TABLE \"Zusatzzeit\" (\"prNR\" integer NOT NULL PRIMARY KEY,  zusatzzeit integer NOT NULL );"
                        + "DROP TABLE IF EXISTS \"kopplungen\";"
                        + "CREATE TABLE kopplungen  (\"pgNR1\" integer NOT NULL, \"pgNR2\" integer NOT NULL,  CONSTRAINT ps PRIMARY KEY (\"pgNR1\", \"pgNR2\"),  CONSTRAINT eind UNIQUE (\"pgNR1\", \"pgNR2\") );"
                        + "DROP TABLE IF EXISTS \"raeumeTage\";"
                        + "CREATE TABLE \"raeumeTage\" (    \"rNr\" integer NOT NULL,    \"tNr\" integer NOT NULL);"
                        + "DROP TABLE IF EXISTS \"unterrichtet\";"
                        + "CREATE TABLE unterrichtet (    \"lNr\" integer NOT NULL,    fk character varying(30) NOT NULL);"
                        + "CREATE UNIQUE INDEX \"bz\" ON \"Blockzeiten\" (\"lNr\" ASC, \"tNr\" ASC, \"zNr\" ASC);"
                        + "CREATE UNIQUE INDEX \"bzalle\" ON \"BlockzeitenALLE\" (\"tNr\" ASC, \"zNr\" ASC);"
                        + "CREATE UNIQUE INDEX \"bzsus\" ON \"BlockzeitenSuS\" (\"prNR\" ASC, \"tNr\" ASC, \"zNr\" ASC);"
                        + "CREATE UNIQUE INDEX \"bzstpl\" ON \"BlockzeitenStundenplan\" (\"lNR\" ASC, \"tNr\" ASC, \"zNr\" ASC);"
                        + "CREATE UNIQUE INDEX \"prNR\" ON \"Pruefung\" (\"prNR\" ASC);"
                        + "CREATE UNIQUE INDEX \"rt\" ON \"raeumeTage\" (\"rNr\" ASC, \"tNr\" ASC);"
                        + "CREATE UNIQUE INDEX \"unterrichtet_lnr_fk\" ON \"unterrichtet\" (\"lNr\" ASC, \"fk\" ASC);";

                try {
                    sql.executeUpdate(sqlCreate);
                } catch (SQLException ex) {
                    Hilfsfunktionen.myDebug("Fehler in createDB() 1: " + ex.getLocalizedMessage());
                    return false;
                }

                try {
                    conn.close();
                } catch (SQLException ex) {
                    Hilfsfunktionen.myDebug("Fehler in createDB(): " + ex.getLocalizedMessage());
                }

            } catch (SQLException ex) {
                Logger.getLogger(DB_Anfragen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

}
