package world;

import java.util.ArrayList;
import java.util.List;

import world.worldGen.SurfaceGenL;
import world.worldGen.SurfaceGenR;

public class WorldGenerator {

	//boolean doingAHill;
	
//	public int rim;
//	public Node baseRight;//always three sectors long
//	public Node baseLeft;//always three sectors long
//	public List<Line> openLines = new ArrayList<>();
//	public Random random = new Random();

	public SurfaceGenR surfaceRight;
	public SurfaceGenL surfaceLeft;
	int rimR = 0, rimL = 0;
	
	public WorldGenerator(){
		surfaceRight = new SurfaceGenR();
		surfaceLeft = new SurfaceGenL();
	}
	
	public Sector generateRight(){
		rimR++;
		Sector output = new Sector();
		surfaceRight.expand(output, rimR*Sector.WIDTH); 
		return output;
	}
	
	public Sector generateLeft(){
		rimL--;
		Sector output = new Sector();
		surfaceLeft.expand(output, rimL*Sector.WIDTH);
		return output;
	}
	
	public class Sector {
		public static final int WIDTH = 200;
		public List<MaterialCycle> areas = new ArrayList<>();
		
		public Node[] openEndingsRight;
		public Node[] openEndingsLeft;
		
		public Node[] insideCircles;
		
	}
}
