package world.worldGen;

import world.Connection;
import world.Line;
import world.Material;
import world.Sector;

public class SurfaceGenR extends Surface {

	public SurfaceGenR(Connection[] lastConns){
		this.lastConns = lastConns;
		System.out.println(lastConns[0].test);
	}
	
	public Sector expand(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		
		grassT = new Line();
		grassB = new Line();
		earthT = new Line();
		earthB = new Line();
		stoneT = new Line();
		stoneB = new Line();
		
		while(baseEnd.x <= xDest){
			step();
		}

		//Ober- und Unterseite der Materialien verbinden
		createCycle(grassT, grassB);
		createCycle(earthT, earthB);
		createCycle(stoneT, stoneB);
		
		//die Materialienfelder zum Sektor hinzufügen
		output.areas[Material.GRASS.ordinal()].cycles.add(grassT.end);
		output.areas[Material.EARTH.ordinal()].cycles.add(earthT.end);
		output.areas[Material.STONE.ordinal()].cycles.add(stoneT.end);

		//die letzte (linke) Verbindung vervollständigen und in den Sektor hinzufügen
		lastConns[0].nodeR = grassT.end;	output.connsL.add(lastConns[0]);
		lastConns[1].nodeR = earthT.end;	output.connsL.add(lastConns[1]);
		lastConns[2].nodeR = stoneT.end;	output.connsL.add(lastConns[2]);

		//letzte verbindungen an den rechten Rand verschieben und schonmal mit dem linken Knoten füllen
		lastConns[0] = new Connection(grassB.end, null);
		lastConns[1] = new Connection(earthB.end, null);
		lastConns[2] = new Connection(stoneB.end, null);

		//Die noch unvollständigen rechten Verbindungen zum sektor hinzufügen
		output.connsR.add(lastConns[0]);
		output.connsR.add(lastConns[1]);
		output.connsR.add(lastConns[2]);
		
		plantTrees(output);
		plantGrass(output);
		spreadAnimals(output);
		placeClouds(output);
		
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
