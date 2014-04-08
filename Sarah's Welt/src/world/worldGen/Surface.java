package world.worldGen;

import java.util.Random;

import world.Connection;
import world.Line;
import world.Point;

public abstract class Surface {

	Random random = new Random();
	
	public Connection[] lastConns = {new Connection(null, null), new Connection(null, null), new Connection(null, null)};
	Point baseEnd = new Point(0, 300);

	Line grassT = new Line(); int gTOffset = 0;
	Line grassB = new Line(); int gBOffset = -10;
	Line earthT = new Line(); int eTOffset = -10;
	Line earthB = new Line(); int eBOffset = -100;
	Line stoneT = new Line(); int sTOffset = -100;
	Line stoneB = new Line(); int sBOffset = -1000;

	void shiftBaseEnd(boolean right){
		float segmentLength = 20;
		float dx = (right ? 1 : -1) * (16 + (random.nextInt(5)));
		float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);
		baseEnd.x += dx;
		baseEnd.y += dy;
	}
	
	public static void createCycle(Line l1, Line l2){
		l1.end.next = l2.start;
		l2.start.last = l1.end;
		
		l2.end.next = l1.start;
		l1.start.last = l2.end;
	}
	
}
