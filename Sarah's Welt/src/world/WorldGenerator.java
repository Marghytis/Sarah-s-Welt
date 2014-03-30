package world;

import java.util.List;

import world.worldGen.Surface;

public class WorldGenerator {

	//boolean doingAHill;
	
//	public int rim;
//	public Node baseRight;//always three sectors long
//	public Node baseLeft;//always three sectors long
//	public List<Line> openLines = new ArrayList<>();
//	public Random random = new Random();

	public Surface surface;
	int xSector;
	
	public WorldGenerator(boolean right){
		this.surface = new Surface(right);
	}
	
	
	public Sector generateRight(){
		Sector output = new Sector();
		surface.expandRight(output, (xSector+1)*Sector.WIDTH);
		return output;
	}
	
	public class Sector {
		public static final int WIDTH = 1000;
		public List<Line>[] lines;
		public Node[] openEndingsRight;
		public boolean[]inOutRight;
		public Node[] openEndingsLeft;
		public boolean[]inOutLeft;
		
	}
}
