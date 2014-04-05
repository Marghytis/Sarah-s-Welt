package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datenbank {

    public Connection db_open(String verzeichnis, String dbname) {
        try {
            if (Class.forName("org.sqlite.JDBC") == null) {
                Class.forName("org.sqlite.JDBC").newInstance();
            }
            return DriverManager.getConnection("jdbc:sqlite:" + verzeichnis + dbname);            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
}
