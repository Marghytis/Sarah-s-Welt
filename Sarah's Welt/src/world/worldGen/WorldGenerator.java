package world.worldGen;

import world.Sector;
import world.WorldWindow;

public class WorldGenerator {

	//boolean doingAHill;
	
//	public int rim;
//	public Node baseRight;//always three sectors long
//	public Node baseLeft;//always three sectors long
//	public List<Line> openLines = new ArrayList<>();
//	public Random random = new Random();

	public SurfaceGenR surfaceRight;
	public SurfaceGenL surfaceLeft;
	public int rimR = 0;
	public int rimL = 0;
	
	public WorldGenerator(){
		if(/*the rims are at zero*/true){
			Connection[] firstConns = {new Connection(null, null), new Connection(null, null), new Connection(null, null)};
			
			surfaceLeft = new SurfaceGenL(new Connection[]{firstConns[0], firstConns[1], firstConns[2]});
				WorldWindow.sectors[0] = generateLeft();
			
			surfaceRight = new SurfaceGenR(new Connection[]{firstConns[0], firstConns[1], firstConns[2]});
				WorldWindow.sectors[1] = generateRight();
				WorldWindow.sectors[2] = generateRight();
			
		}/* else {
			//TODO load the rims from the database
		}*/
	}
	
	public Sector generateRight(){
		Sector output = new Sector(rimR);
		rimR++;
		
		surfaceRight.expand(output, rimR*Sector.WIDTH); 
		
		return output;
	}
	
	public Sector generateLeft(){
		Sector output = new Sector(rimL-1);
		rimL--;
		
		surfaceLeft.expand(output, rimL*Sector.WIDTH);
		
		return output;
	}
	
	
}
