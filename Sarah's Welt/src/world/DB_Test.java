package world;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Datenbank;

public class DB_Test {

	static Datenbank db;
	
	public static void main(String[] args){
		db = new Datenbank("", "huhu");
//		if(db.fresh){
			createDB();
//		}

		add("TESTTEST1", 400);
		add("TESTTEST2", 800);
		add("TESTTEST3", 1200);
		
		System.out.println(getX("TESTTEST2"));
	}
	
	public static void add(String name, float x){
		try {
            Statement sql = db.conn.createStatement();

            sql.execute("INSERT INTO Data (name, x) VALUES ('" + name + "'," + x + ");");//TODO
            
            sql.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	public static float getX(String name){
		try {
            Statement sql = db.conn.createStatement();

            ResultSet ergebnis = sql.executeQuery("SELECT x FROM Data WHERE name = '" + name + "';");//TODO
            
            ergebnis.next();
            	float out = ergebnis.getFloat("x");
            ergebnis.close();
            sql.close();
            return out;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
		return 0;
	}
	
	public static void createDB() {
        try {
            Statement sql = db.conn.createStatement();

            String sqlCreate = "CREATE TABLE 'Data' ('name' VARCHAR, 'x' FLOAT);";

            sql.executeUpdate(sqlCreate);
            sql.close();

        } catch (SQLException ex) {
//            ex.printStackTrace();
        }
    }
	
}
