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
	
	Point baseEnd;

	Node grassEnd;
	Line grassT = new Line();
	Line earthT = new Line();
	Line stoneT = new Line();
	Line bottom = new Line();

	public Sector expandRight(Sector output, float xDest){

//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		step();
		output.openEndingsLeft = new Node[]{grassT.end, earthT.end, stoneT.end, bottom.end};
		while(baseEnd.x <= xDest){
			step();
		}
		
		output.lines = new Line[Material.values().length][];
		output.lines[Material.GRASS.ordinal()] = new Line[]{grassT};
		output.lines[Material.EARTH.ordinal()] = new Line[]{earthT};
		output.lines[Material.STONE.ordinal()] = new Line[]{stoneT};
		output.lines[Material.STONE.ordinal()] = new Line[]{bottom};

//		finalize the lines by adding the way back and closing each to a circle
//		grassTop.appendLine(earthTop, true);
//		earthTop.appendLine(stoneTop, true);
//		stoneTop.appendLine(bottom, true);
		return output;
	}
	
	void step(){
		shiftBaseEnd();

		grassEnd.next = new Node(new Point(baseEnd), grassEnd);
		grassT.addPoints(baseEnd.x, baseEnd.y - 0);//Grass top
		earthT.addPoints(baseEnd.x, baseEnd.y - 10);//Earth top
		stoneT.addPoints(baseEnd.x, baseEnd.y - 100);//Stone top
		bottom.addPoints(baseEnd.x, baseEnd.y - 1000);//Bottom
	}

	void shiftBaseEnd(){
		float segmentLength = 20;
		float dx = (right ? 1 : -1) * (16 + (random.nextInt(5)));
		float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);
		baseEnd.x += dx;
		baseEnd.y += dy;
	}
	
}
