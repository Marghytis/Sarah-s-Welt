package world;

import java.util.ArrayList;
import java.util.List;

import world.worldGen.Surface;

public class WorldGenerator {

	//boolean doingAHill;
	
//	public int rim;
//	public Node baseRight;//always three sectors long
//	public Node baseLeft;//always three sectors long
//	public List<Line> openLines = new ArrayList<>();
//	public Random random = new Random();

	public Surface surfaceRight;
	public Surface surfaceLeft;
	int xSector;
	
	public WorldGenerator(){
		this.surfaceRight = new Surface(true);
		this.surfaceLeft = new Surface(false);
	}
	
	public Sector generateRight(){
		Sector output = new Sector();
		surfaceRight.expandRight(output, (xSector+1)*Sector.WIDTH);
		return output;
	}
	
	public Sector generateLeft(){
		Sector output = new Sector();
		surfaceLeft.expandLeft(output, (xSector+1)*Sector.WIDTH);
		return output;
	}
	
	public class Sector {
		public static final int WIDTH = 1000;
		@SuppressWarnings("unchecked")
		public List<Line>[] lines = (List<Line>[]) new ArrayList<?>[Material.values().length];
		public Node[] openEndingsRight;
		public boolean[]inOutRight;
		public Node[] openEndingsLeft;
		public boolean[]inOutLeft;
		
		public Sector(){
			for(int i = 0; i < lines.length; i++){
				lines[i] = new ArrayList<>();
			}
		}
	}
}
