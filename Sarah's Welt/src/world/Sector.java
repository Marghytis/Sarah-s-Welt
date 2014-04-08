package world;

import java.util.ArrayList;
import java.util.List;

public class Sector {
	public static final int WIDTH = 200;
	public MatArea[] areas = new MatArea[Material.values().length];

	public List<Connection> connsR = new ArrayList<>(); public int indexR = 0;
	public List<Connection> connsL = new ArrayList<>(); public int indexL = 0;
	
	public Sector(){
		for(int i = 0; i < Material.values().length; i++){
			areas[i] = new MatArea();
		}
	}
	
	public void switchConnection(Sector other, boolean right){
		if(right){
			for(Connection c : connsR){
				c.switchConnection();
			}
		} else {
			for(Connection c : connsL){
				c.switchConnection();
			}
		}
	}
}