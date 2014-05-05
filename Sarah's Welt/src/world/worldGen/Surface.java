package world.worldGen;

import java.util.Random;

import util.Line;
import world.Node;
import world.Point;
import world.Sector;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Rabbit;
import world.creatures.Snail;
import world.structures.Bamboo;
import world.structures.Bush;
import world.structures.Cloud;
import world.structures.Crack;
import world.structures.Flower;
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
	
	public void spreadThings(Sector sector, Runnable yOffset, Runnable method){
//		Runnable test = () -> System.out.println("Hello world two!");
	}
	
	public void placeCracks(Sector sector){
		for(int i = 0; i < 50; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, - (300 + random.nextInt(1000)));//- random.nextInt(100)
			
			float sizeMultiplier = 0.1f + (0.75f*random.nextFloat());
			
			Crack crack = new Crack(random.nextInt(4), intersection, link, sizeMultiplier, sizeMultiplier, random.nextFloat()*(float)(2*Math.PI));
			
			sector.structures.add(crack);
		}
	}
		
	public void placeClouds(Sector sector){
		int count = random.nextInt(3);
		for(int i = 0; i < count; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 300 + random.nextInt(200));
			
			Cloud cloud = new Cloud(intersection, link, 0.5f + random.nextFloat(), 0.5f + random.nextFloat());
			
			sector.structures.add(cloud);
		}
	}
	
	public void spreadButterflies(Sector sector){
		for(int i = 0; i < 3; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 20);
			
			Butterfly but = new Butterfly(random.nextInt(2), intersection, link, random.nextInt(Butterfly.flap1.sequence.length));
			
			sector.creatures.add(but);
		}
	}
	
	public void spreadBirds(Sector sector){
		for(int i = 0; i < 3; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 20);
			
			Bird but = new Bird(random.nextInt(2), intersection, link, random.nextInt(Butterfly.flap1.sequence.length));
			
			sector.creatures.add(but);
		}
	}
	
	public void spreadRabbits(Sector sector){
		for(int i = 0; i < 1; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 5);
			
			Rabbit rab = new Rabbit(intersection, link);
			
			sector.creatures.add(rab);
		}
	}
	
	public void spreadSnails(Sector sector){
		for(int i = 0; i < 1; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 5);
			
			Snail snail = new Snail(intersection, link);
			
			sector.creatures.add(snail);
		}
	}
	
	public void plantTrees(Sector sector){
		for(int i = 0; i < 10; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 0);
			
			Tree tree = new Tree(random.nextInt(3), intersection, link);
			
			sector.structures.add(tree);
		}
	}
	
	public void plantGrass(Sector sector){
		for(int i = 0; i < 100; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 0);
			
			Grass_tuft tuft = new Grass_tuft(intersection, link, random.nextInt(Grass_tuft.wave.sequence.length));
			
			sector.structures.add(tuft);
		}
	}
	
	public void plantFlowers(Sector sector){
		for(int i = 0; i < 25; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 0);
			
			Flower flower = new Flower(random.nextInt(3), intersection, link);
			
			sector.structures.add(flower);
		}
	}
	
	public void plantBushes(Sector sector){
		for(int i = 0; i < 10; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 0);
			
			Bush bush = new Bush(random.nextInt(2), intersection, link, random.nextBoolean());
			
			sector.structures.add(bush);
		}
	}
	
	public void plantBamboo(Sector sector){
		for(int i = 0; i < 10; i++){
			int x = sector.getGoodSpawnPos(random);
			Point intersection = new Point(x, 0);
			Node link = sector.findGrassPointAt(x, intersection, 0);
			
			Bamboo bush = new Bamboo(random.nextInt(2), intersection, link, random.nextBoolean());
			
			sector.structures.add(bush);
		}
	}
	
	float segmentLength = 10;
	double angle = 0;
	
	WorldGenObject level_1 = null;
	WorldGenObject level_2 = null;

	void shiftBaseEnd(boolean right){
		double dx = segmentLength*Math.cos(angle);
		double dy = segmentLength*Math.sin(angle);
		
		//new angle
		
		angle = (right ? 0 : Math.PI);
		
//		if(level_1 == null){
//			if(random.nextInt(100) < 4)	level_1 = WorldGenObject.RAISING;
//		}
//		try {
//			 angle += level_1.next(random);
//		} catch (Exception e) {
//			level_1 = null;
//		}
		
		if(level_2 == null){
			 if(random.nextInt(100) < 10)level_2 = random.nextBoolean() ? WorldGenObject.UP : WorldGenObject.DOWN;
		}
		try {
			 angle += level_2.next(random);
		} catch (Exception e) {
			level_2 = null;
		}
		
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
