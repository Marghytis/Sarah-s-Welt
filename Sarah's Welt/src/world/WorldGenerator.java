package world;

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
		if(/*the rims are at zero*/true){
			Connection[] firstConns = {new Connection(null, null), new Connection(null, null), new Connection(null, null)};
			
			firstConns[0].test = "Huhu";
			
			surfaceLeft = new SurfaceGenL(new Connection[]{firstConns[0], firstConns[1], firstConns[2]});
				Sector l = new Sector();
				surfaceLeft.expand(l, -Sector.WIDTH);
				rimL = -1;
			
			surfaceRight = new SurfaceGenR(new Connection[]{firstConns[0], firstConns[1], firstConns[2]});
				Sector r1 = new Sector();
				Sector r2 = new Sector();
				surfaceRight.expand(r1, Sector.WIDTH);
				surfaceRight.expand(r2, Sector.WIDTH*2);
				rimR = 2;

			WorldWindow.sectors[0] = l;
			WorldWindow.sectors[1] = r1;
			WorldWindow.sectors[2] = r2;
			
		}/* else {
			//TODO load the rims from the database
		}*/
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
	
	
}
