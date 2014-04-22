package world;

import java.util.ArrayList;
import java.util.List;

import world.creatures.Creature;
import world.structures.Structure;
import world.worldGen.Connection;

public class Sector{
	/**The width of one sector, always the same*/
	public static final int WIDTH = 1000;
	
	public int x;
	public MatArea[] areas = new MatArea[Material.values().length];
	public List<Structure> structures = new ArrayList<>();
	public List<Creature> creatures = new ArrayList<>();
	
	public List<Connection> connsR = new ArrayList<>(); public int indexR = 0;
	public List<Connection> connsL = new ArrayList<>(); public int indexL = 0;
	
	public Sector(int x){
		this.x = x;
		for(int i = 0; i < Material.values().length; i++){
			areas[i] = new MatArea();
		}
	}
	
	public Node findGrassPointAt(float x, Point intersection, float yOffset){
		for(Node n : areas[Material.GRASS.ordinal()].cycles){
			Node h = n;
			do {
				if(h.p.x > x && h.next.p.x < x){
					float slope = h.next.p.minus(h.p).slope();
					intersection.set(x, h.next.p.y + (slope*(x - h.next.p.x)) + yOffset);
					return h;
				}
				h = h.next;
			} while(h != n);
		}
		return null;
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
	
//	public Random random;
//	
//	public int randomnr(int min , int max){
//		int n = max - min + 1;
//		int i = min + random.nextInt(n);
//		return i;
//	}
//	
//	public Sector(int x){
//		this.x = x;
//		random = new Random();
//		for(int i = 0; i < lines.length; i++) lines[i] = new ArrayList<>();
//
//		
//		int breite = randomnr(130, 250);
//		int hoehe = breite - randomnr(-20, 80);
//		int cx = randomnr(x*Sector.WIDTH, (x+1)*Sector.WIDTH);
//		int cy = randomnr(330, 500);
//		
//		quad = new Quad(cx, cy, breite, hoehe);
//	}
}
