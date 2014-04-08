package world.worldGen;

import world.Line;
import world.Material;
import world.Sector;

public class SurfaceGenL extends Surface {
	
	public Sector expand(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
//		output.openEndingsRight = new Node[]{grassT.end, earthT.end, stoneT.end, stoneB.end};

		grassT = new Line();
		grassB = new Line();
		earthT = new Line();
		earthB = new Line();
		stoneT = new Line();
		stoneB = new Line();
		
		while(baseEnd.x >= xDest){
			step();
		}
//		output.openEndingsLeft = new Node[]{grassT.end.last, earthT.end.last, stoneT.end.last, stoneB.end.last};
	
		output.lines[Material.GRASS.ordinal()].add(grassT);
		output.lines[Material.GRASS.ordinal()].add(grassB);
		
		output.lines[Material.EARTH.ordinal()].add(earthT);
		output.lines[Material.EARTH.ordinal()].add(earthB);
		
		output.lines[Material.STONE.ordinal()].add(stoneT);
		output.lines[Material.STONE.ordinal()].add(stoneB);

		return output;
	}
	
	void step(){
		shiftBaseEnd(false);

		grassT.addPoint		(baseEnd.x, baseEnd.y + gTOffset);//Grass top
		grassB.addPointBack	(baseEnd.x, baseEnd.y + gBOffset);
		earthT.addPoint		(baseEnd.x, baseEnd.y + eTOffset);//Earth top
		earthB.addPointBack	(baseEnd.x, baseEnd.y + eBOffset);
		stoneT.addPoint		(baseEnd.x, baseEnd.y + sTOffset);//Stone top
		stoneB.addPointBack	(baseEnd.x, baseEnd.y + sBOffset);//Bottom
	}
}