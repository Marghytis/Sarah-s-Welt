package world.worldGen;

import world.Material;
import world.WorldGenerator.Sector;

public class SurfaceGenR extends Surface {
	
	public Sector expand(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
//		output.openEndingsLeft = new Node[]{grassT.end, earthT.end, stoneT.end, stoneB.end};
		
		while(baseEnd.x <= xDest){
			step();
		}
		System.out.println(stoneB.start.p + "" + stoneB.end.p);
//		output.openEndingsRight = new Node[]{grassT.end.last, earthT.end.last, stoneT.end.last, stoneB.end.last};
		output.lines[Material.GRASS.ordinal()].add(grassT);
		output.lines[Material.GRASS.ordinal()].add(grassB);
		
		output.lines[Material.EARTH.ordinal()].add(earthT);
		output.lines[Material.EARTH.ordinal()].add(earthB);
		
		output.lines[Material.STONE.ordinal()].add(stoneT);
		output.lines[Material.STONE.ordinal()].add(stoneB);

		grassT.start = null;
		grassT.end = null;

		return output;
	}
	
	void step(){
		shiftBaseEnd(true);

		grassT.addPointBack	(baseEnd.x, baseEnd.y + gTOffset);//Grass top
		grassB.addPoint		(baseEnd.x, baseEnd.y + gBOffset);
		earthT.addPointBack	(baseEnd.x, baseEnd.y + eTOffset);//Earth top
		grassB.addPoint		(baseEnd.x, baseEnd.y + eBOffset);
		stoneT.addPointBack	(baseEnd.x, baseEnd.y + sTOffset);//Stone top
		stoneB.addPoint		(baseEnd.x, baseEnd.y + sBOffset);//Bottom
	}

	
}
