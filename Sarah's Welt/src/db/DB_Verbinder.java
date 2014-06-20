package db_beispiel;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Christopher Bräuer
 */
public class DB_Verbinder {

    // protected = nur in dieser Klasse und in allen erbenden Klassen sichtbar
    protected Connection conn = null;
    protected String sqlAnfrage = "";
    protected Connection actualConnection;

    /**
     * Öffnet eine Verbindung zur Database. Falls die DB noch nicht erstellt,
     * wird sie angelegt.
     *
     * @param verzeichnis - Verzeichnis, in dem die DB-Datei gespeichert ist.
     * @param dbname - Name der Database
     * @return wahr oder falsch
     */
    public boolean db_open(String verzeichnis, String dbname) {
        System.out.println("Öffne Database: " + verzeichnis + dbname);

        boolean dbtest = false;
        try {
            if (Class.forName("org.sqlite.JDBC") == null) {
                // falls noch nicht existent, dann erstellen
                Class.forName("org.sqlite.JDBC").newInstance();
            }
            conn = DriverManager.getConnection("jdbc:sqlite:" + verzeichnis + dbname);            
            
            actualConnection = conn;
            dbtest = true;
        } catch (Exception e) {
            System.out.println("Datenbankfehler in db_open(): " + e.getLocalizedMessage());
            dbtest = false;
        }
        return dbtest;
    }

    /**
     * Schließt die Verbindung zur DB
     */
    public void db_close() {
        try {
            if (conn != null || !conn.isClosed()) {
                conn.close();
            }
            System.out.println("DB-Verbindung geschlossen.");
        } catch (Exception ex) {
            System.out.println("######## DB-Verbindung konnte NICHT geschlossen werden! ########\n" + ex.getLocalizedMessage());
        }
    }

    /**
     * Gibt die aktuelle Verbindung zurück.
     *
     * @return actualConnection - aktuelle Verbindung
     */
    protected Connection getConnectionToActualDB() {
        return actualConnection;
    }
}
