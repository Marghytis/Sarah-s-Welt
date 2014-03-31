package world.worldGen;

import java.util.Random;

import world.Line;
import world.Material;
import world.Node;
import world.Point;
import world.WorldGenerator.Sector;

public class Surface {

	Random random = new Random();
	
	boolean right;
	
	public Surface(boolean rightOrLeft){
		right = rightOrLeft;
	}
	
	Point baseEnd = new Point(0, 300);

	Line grassT = new Line();
	Line grassB = new Line();
	Line earthT = new Line();
	Line earthB = new Line();
	Line stoneT = new Line();
	Line stoneB = new Line();

	public Sector expandRight(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		step();
		output.openEndingsLeft = new Node[]{grassT.end, earthT.end, stoneT.end, stoneB.end};
		
		while(baseEnd.x <= xDest){
			step();
		}

		Node e = earthT.end;
		Node s = stoneT.end;
		grassB.addPoint(e.p);
		earthB.addPoint(s.p);
		while(e != earthT.start){
			e = e.last;
			grassB.addPoint(e.p);
			earthB.addPoint(s.p);
		}

		output.lines[Material.GRASS.ordinal()].add(grassT);
		output.lines[Material.GRASS.ordinal()].add(grassB);
		output.lines[Material.EARTH.ordinal()].add(earthT);
		output.lines[Material.GRASS.ordinal()].add(earthB);
		output.lines[Material.STONE.ordinal()].add(stoneT);
		output.lines[Material.STONE.ordinal()].add(stoneB);

		return output;
	}

	public Sector expandLeft(Sector output, float xDest){
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		output.openEndingsRight = new Node[]{grassT.end, earthT.end, stoneT.end, stoneB.end};
		
		while(baseEnd.x >= xDest){
			step();
		}
		output.openEndingsRight = new Node[]{grassT.end.last, earthT.end.last, stoneT.end.last, stoneB.end.last};
		
		output.lines[Material.GRASS.ordinal()].add(grassT);
		output.lines[Material.EARTH.ordinal()].add(earthT);
		output.lines[Material.STONE.ordinal()].add(stoneT);
		output.lines[Material.STONE.ordinal()].add(stoneB);

		return output;
	}
	
	void step(){
		shiftBaseEnd();

		grassT.addPoint(baseEnd.x, baseEnd.y - 0);//Grass top
		earthT.addPoint(baseEnd.x, baseEnd.y - 10);//Earth top
		stoneT.addPoint(baseEnd.x, baseEnd.y - 100);//Stone top
		stoneB.addPoint(baseEnd.x, baseEnd.y - 1000);//Bottom
	}

	void shiftBaseEnd(){
		float segmentLength = 20;
		float dx = (right ? 1 : -1) * (16 + (random.nextInt(5)));
		float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);
		baseEnd.x += dx;
		baseEnd.y += dy;
	}
	
}
