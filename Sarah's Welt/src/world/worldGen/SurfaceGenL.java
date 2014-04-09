package world.worldGen;

import world.Connection;
import world.Line;
import world.Material;
import world.Sector;

public class SurfaceGenL extends Surface {
	
	public Sector expand(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		
		grassT = new Line();
		grassB = new Line();
		earthT = new Line();
		earthB = new Line();
		stoneT = new Line();
		stoneB = new Line();
		
		while(baseEnd.x >= xDest){
			step();
		}

		createCycle(grassT, grassB);
		createCycle(earthT, earthB);
		createCycle(stoneT, stoneB);
		
		output.areas[Material.GRASS.ordinal()].cycles.add(grassT.end);
		output.areas[Material.EARTH.ordinal()].cycles.add(earthT.end);
		output.areas[Material.STONE.ordinal()].cycles.add(stoneT.end);
		
		lastConns[0].nodeL = grassB.end;	output.connsR.add(lastConns[0]);
		lastConns[1].nodeL = earthB.end;	output.connsR.add(lastConns[1]);
		lastConns[2].nodeL = stoneB.end;	output.connsR.add(lastConns[2]);
		
		lastConns[0] = new Connection(null, grassT.end);
		lastConns[1] = new Connection(null, earthT.end);
		lastConns[2] = new Connection(null, stoneT.end);

		output.connsL.add(lastConns[0]);
		output.connsL.add(lastConns[1]);
		output.connsL.add(lastConns[2]);
		
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