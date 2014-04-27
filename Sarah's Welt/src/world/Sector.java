package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	public List<Connection> connsR = new ArrayList<>();
	public List<Connection> connsL = new ArrayList<>();
	
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
				if(h.p.x >= x && h.next.p.x <= x){
					float slope = h.next.p.minus(h.p).slope();
					intersection.set(x, h.next.p.y + (slope*(x - h.next.p.x)) + yOffset);
					return h;
				}
				h = h.next;
			} while(h != n);
		}
		return null;
	}
	
	public int getGoodSpawnPos(Random r){
		return (x*WIDTH) + r.nextInt(WIDTH - 1) + 1;//ones because it shouldn't spawn at the border
	}
	
	public void connectTo(Sector other){
		if(other == null){
			(new Exception("Can't connect to the specified Sector! (null)")).printStackTrace();
		} else if(other.x == x + 1){
			connsR.forEach( c -> c.link());
		} else if(other.x == x - 1){
			System.out.println("test");
			connsL.forEach( c -> c.link());
		} else {
			(new Exception("Can't connect to the specified Sector!")).printStackTrace();
		}
	}
	
	public void partiateFrom(Sector other){
		if(other == null){
			(new Exception("Can't partiate from the specified Sector! (null)")).printStackTrace();
		} else if(other.x == x + 1){
			connsR.forEach( c -> c.partiate());
		} else if(other.x == x - 1){
			connsL.forEach( c -> c.partiate());
		} else {
			(new Exception("Can't partiate from the specified Sector!")).printStackTrace();
		}
	}
}
