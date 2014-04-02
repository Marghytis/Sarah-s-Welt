package world.worldGen;

import world.Line;
import world.Material;
import world.WorldGenerator.Sector;

public class SurfaceGenR extends Surface {
	
	public Sector expand(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
//		output.openEndingsLeft = new Node[]{grassT.end, earthT.end, stoneT.end, stoneB.end};

		grassT = new Line();
		grassB = new Line();
		earthT = new Line();
		earthB = new Line();
		stoneT = new Line();
		stoneB = new Line();
		
		while(baseEnd.x <= xDest){
			step();
		}
//		output.openEndingsRight = new Node[]{grassT.end.last, earthT.end.last, stoneT.end.last, stoneB.end.last};
		grassT.end.next = grassB.start;
		grassB.start.last = grassT.end;
		grassT.end = grassB.end;
		//make a circle
		grassT.end.next = grassT.start;
		grassT.start.last = grassT.end;
		output.lines[Material.GRASS.ordinal()].add(grassT);
//		output.lines[Material.GRASS.ordinal()].add(grassB);

		earthT.end.next = earthB.start;
		earthB.start.last = earthT.end;
		earthT.end = earthB.end;
		
		earthT.end.next = earthT.start;
		earthT.start.last = earthT.end;
		output.lines[Material.EARTH.ordinal()].add(earthT);
//		output.lines[Material.EARTH.ordinal()].add(earthB);

		stoneT.end.next = stoneB.start;
		stoneB.start.last = stoneT.end;
		stoneT.end = stoneB.end;
		
		stoneT.end.next = stoneT.start;
		stoneT.start.last = stoneT.end;
		output.lines[Material.STONE.ordinal()].add(stoneT);
//		output.lines[Material.STONE.ordinal()].add(stoneB);

		return output;
	}
	
	void step(){
		grassT.addPointBack	(baseEnd.x, baseEnd.y + gTOffset);//Grass top
		grassB.addPoint		(baseEnd.x, baseEnd.y + gBOffset);
		earthT.addPointBack	(baseEnd.x, baseEnd.y + eTOffset);//Earth top
		earthB.addPoint		(baseEnd.x, baseEnd.y + eBOffset);
		stoneT.addPointBack	(baseEnd.x, baseEnd.y + sTOffset);//Stone top
		stoneB.addPoint		(baseEnd.x, baseEnd.y + sBOffset);//Stone bottom
		
		shiftBaseEnd(true);
	}

	
}
