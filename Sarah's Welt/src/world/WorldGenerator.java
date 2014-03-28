package world;

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
	
	public WorldGenerator(){
		this.surfaceRight = new Surface(true);
		this.surfaceRight = new Surface(false);
	}
	
	
	public void generateRight(){

			rim++;
	}
}
