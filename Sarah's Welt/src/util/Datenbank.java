package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datenbank {
	
	public Connection conn;
	public boolean fresh = true;

    public Datenbank(String verzeichnis, String dbname) {
        try {
            if (Class.forName("org.sqlite.JDBC") == null) {
                Class.forName("org.sqlite.JDBC").newInstance();
                fresh = true;
            } else {
            	fresh = false;
            }
            conn = DriverManager.getConnection("jdbc:sqlite:" + verzeichnis + "\\" + dbname);            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}
