package world.worldGen;

import java.util.Random;

import world.Line;
import world.Node;
import world.Point;

public class Surface {

	Line grassTop;
	Line earthTop;
	Line stoneTop;
	Line StoneBottom;
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

	Line[] expand(float xDest){

		float segmentLength = 20;

//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		if(right){
			while(baseEnd.x <= xDest){
				step();
			}
		} else {
			while(baseEnd.x >= xDest){
				step();
			}
		}

//		finalize the lines by adding the way back and closing each to a circle
		grassTop.appendLine(earth, true);
		earth.appendLine(stone, true);
		stone.appendLine(bottom, true);
		
		lines[2].add(grass);
		lines[1].add(earth);
		lines[0].add(stone);
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
		baseEnd.x += (right ? 1 : -1) * (16 + (random.nextInt(5)));
		baseEnd.y += (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);
	}
	
}
