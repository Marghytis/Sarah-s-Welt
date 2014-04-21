package world.worldGen;

import java.util.Random;

import world.Connection;
import world.Line;
import world.Node;
import world.Point;
import world.Sector;
import world.creatures.Butterfly;
import world.structures.Cloud;
import world.structures.Grass_tuft;
import world.structures.Tree;

public abstract class Surface {

	Random random = new Random();
	
	public Connection[] lastConns;
	Point baseEnd = new Point(0, 300);

	Line grassT = new Line(); int gTOffset = 0;
	Line grassB = new Line(); int gBOffset = -10;
	Line earthT = new Line(); int eTOffset = -10;
	Line earthB = new Line(); int eBOffset = -100;
	Line stoneT = new Line(); int sTOffset = -100;
	Line stoneB = new Line(); int sBOffset = -1000;
	
	public void placeClouds(Sector sector){
		for(int i = 0; i < 1; i++){
			float x = (sector.x + random.nextFloat())*Sector.WIDTH;
			Point intersection = new Point();
			Node link = sector.findGrassPointAt(x, intersection);
			intersection.y += 300 + random.nextInt(200);
			
			Cloud cloud = new Cloud(intersection);
			cloud.worldLink = link;
			
			sector.structures.add(cloud);
		}
	}
	
	public void spreadAnimals(Sector sector){
		for(int i = 0; i < 3; i++){
			float x = (sector.x + random.nextFloat())*Sector.WIDTH;
			Point intersection = new Point();
			Node link = sector.findGrassPointAt(x, intersection);
			intersection.y += 20;
			
			Butterfly but = new Butterfly(intersection);
			but.worldLink = link;
			
			sector.creatures.add(but);
		}
	}
	
	public void plantTrees(Sector sector){
		for(int i = 0; i < 10; i++){
			float x = (sector.x + random.nextFloat())*Sector.WIDTH;
			Point intersection = new Point();
			Node link = sector.findGrassPointAt(x, intersection);
			
			Tree tree = new Tree(random.nextInt(3), intersection);
			tree.worldLink = link;
			
			sector.structures.add(tree);
		}
	}
	
	public void plantGrass(Sector sector){
		for(int i = 0; i < 100; i++){
			float x = (sector.x + random.nextFloat())*Sector.WIDTH;
			Point intersection = new Point();
			Node link = sector.findGrassPointAt(x, intersection);
			
			Grass_tuft tuft = new Grass_tuft(intersection);
			tuft.worldLink = link;
			
			sector.structures.add(tuft);
		}
	}
	
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
