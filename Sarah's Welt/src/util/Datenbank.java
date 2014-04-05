package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Datenbank {
    protected Connection conn = null;
    protected String sqlAnfrage = "";
    protected Connection actualConnection;

    public boolean db_open(String verzeichnis, String dbname) {

        boolean dbtest = false;
        try {
            if (Class.forName("org.sqlite.JDBC") == null) {
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
    
    protected Connection getConnectionToActualDB() {
        return actualConnection;
    }
}
