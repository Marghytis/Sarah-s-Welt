package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	public Connection conn;

    public Database(String verzeichnis, String dbname) {
        try {
            if (Class.forName("org.sqlite.JDBC") == null) {
                Class.forName("org.sqlite.JDBC").newInstance();
            }
            conn = DriverManager.getConnection("jdbc:sqlite:" + verzeichnis + "\\" + dbname);            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}
