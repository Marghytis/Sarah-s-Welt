package world.worldGen;

import java.util.List;
import java.util.Random;

import util.Line;
import world.Material;
import world.Node;
import world.World;
import world.worldGen.WorldGenObject.WorldGenObjectType;
import core.geom.Vec;

public class SurfaceGenerator {

	public float WIDTH = 1000;

	public Random random = new Random();
	
	private GenerationSettings settingsR;
	private GenerationSettings settingsL;

	public Line grassT; int gTOffset = 0;
	Line grassB; int gBOffset = -10;
	Line earthT; int eTOffset = -10;
	Line earthB; int eBOffset = -50;
	Line stoneT; int sTOffset = -50;
	Line stoneB; int sBOffset = -10000;
	
	/**
	 * IMPORTANT: The "contours" must be completely empty!!
	 */
	public SurfaceGenerator(int width){
		this.WIDTH = width;

		World.contours[Material.GRASS.ordinal()].add(null);
		World.contours[Material.EARTH.ordinal()].add(null);
		World.contours[Material.STONE.ordinal()].add(null);
		
		settingsR = new GenerationSettings(true, new Vec(0, 400));
		settingsL = new GenerationSettings(false, new Vec(0, 400));

		grassT = new Line(settingsR.base.x, settingsR.base.y + gTOffset);
		grassB = new Line(settingsR.base.x, settingsR.base.y + gBOffset);
		earthT = new Line(settingsR.base.x, settingsR.base.y + eTOffset);
		earthB = new Line(settingsR.base.x, settingsR.base.y + eBOffset);
		stoneT = new Line(settingsR.base.x, settingsR.base.y + sTOffset);
		stoneB = new Line(settingsR.base.x, settingsR.base.y + sBOffset);
	}
	
	public void gen(float sarahX){
		
		boolean changedSth = false;
		
		if(sarahX + (WIDTH/2) > settingsR.base.x){
			shiftRightRim(true, sarahX + (WIDTH/2));
			changedSth = true;
		} else if(sarahX + (WIDTH/2) < settingsR.base.x){
			shiftRightRim(false, sarahX + (WIDTH/2));
			changedSth = true;
		}
		if(sarahX - (WIDTH/2) > settingsL.base.x){
			shiftLeftRim(true, sarahX - (WIDTH/2));
			changedSth = true;
		} else if(sarahX - (WIDTH/2) < settingsL.base.x){
			shiftLeftRim(false, sarahX - (WIDTH/2));
			changedSth = true;
		}
		
		if(changedSth){
			createCycle(grassT, grassB);
			createCycle(earthT, earthB);
			createCycle(stoneT, stoneB);
			
			refreshWorldNodes();
		}
	}
	
	public void shiftRightRim(boolean right, float destination){
		if(right){
			while(!reachedDestination(settingsR.base.x, destination, true)){
				
				settingsR.shiftBase();
				
				Vec newGras = new Vec(settingsR.base.plus(0, gTOffset));
				Vec newEarth = new Vec(settingsR.base.plus(0, eTOffset));
				Vec newStone = new Vec(settingsR.base.plus(0, sTOffset));
				Vec newStoneBottom = new Vec(settingsR.base.plus(0, sBOffset));
				
				grassT.addVecBack(newGras);
				grassB.addVec(newEarth);
				earthT.addVecBack(newEarth);//Earth top
				earthB.addVec(newStone);
				stoneT.addVecBack(newStone);//Stone top
				stoneB.addVec(newStoneBottom);//Stone bottom
				
				settingsR.biome.spawnThings(grassT.start);
			}
		} else {
			while(!reachedDestination(grassT.start.getNext().getPoint().x, destination, false)){
				//TODO SAVE THE DATA!!!!!
				grassT.removeFirst();
				grassB.removeLast();
				earthT.removeFirst();
				earthB.removeLast();
				stoneT.removeFirst();
				stoneB.removeLast();				
			}
		}
	}
	
	public void shiftLeftRim(boolean right, float destination){
		if(!right){
			while(!reachedDestination(settingsL.base.x, destination, false)){
				
				settingsL.shiftBase();
				
				Vec newGras = new Vec(settingsL.base.plus(0, gTOffset));
				Vec newEarth = new Vec(settingsL.base.plus(0, eTOffset));
				Vec newStone = new Vec(settingsL.base.plus(0, sTOffset));
				Vec newStoneBottom = new Vec(settingsL.base.plus(0, sBOffset));
				
								grassT.addVec(newGras);
				grassB.addVecBack(newEarth);
				earthT.addVec(newEarth);//Earth top
				earthB.addVecBack(newStone);
				stoneT.addVec(newStone);//Stone top
				stoneB.addVecBack(newStoneBottom);//Stone bottom

				settingsL.biome.spawnThings(grassT.end.getLast());
			}
		} else {
			while(!reachedDestination(grassT.end.getLast().getPoint().x, destination, true)){
				//TODO SAVE THE DATA!!!!!
				grassT.removeLast();
				grassB.removeFirst();
				earthT.removeLast();
				earthB.removeFirst();
				stoneT.removeLast();
				stoneB.removeFirst();				
			}
		}
	}
	
	public void refreshWorldNodes(){
		World.contours[Material.GRASS.ordinal()].set(0, grassT.start);
		World.contours[Material.EARTH.ordinal()].set(0, earthT.start);
		World.contours[Material.STONE.ordinal()].set(0, stoneT.start);
	}
	
	public boolean reachedDestination(float pos, float destination, boolean dir){
		return dir ? pos >= destination: pos <= destination;
	}
	
	public static void createCycle(Line l1, Line l2){
		l1.end.setNext(l2.start);
		l2.start.setLast(l1.end);

		l2.end.setNext(l1.start);
		l1.start.setLast(l2.end);
	}
	
	public class GenerationSettings {

		public WorldGenObject level_1;
		public WorldGenObject level_2;
		public double nextAngle = 0;
		public boolean dir;
		public Biome biome = Biome.CANDY;
		public Landscape landscape = Landscape.SOMEHILLS;
		
		List<AreaOpening> openings;
		
		public Vec base;
		
		public GenerationSettings(boolean dir, Vec base){
			this.dir = dir;
			this.base = base;
		}
		
		float segmentLength = 20;
		
		public void shiftBase(){
			
			double dx = segmentLength*Math.cos(nextAngle);
			double dy = segmentLength*Math.sin(nextAngle);
			
			base.x += dx;
			base.y += dy;
			
			//new angle -----------------------------------------
			
			nextAngle = (dir ? 0 : Math.PI);
			
//			if(level_1 == null){
//				if(random.nextInt(100) < 4)	level_1 = WorldGenObject.RAISING;
//			}
//			try {
//				 angle += level_1.next(random);
//			} catch (Exception e) {
//				level_1 = null;
//			}
			
			if(level_2 == null){
				 if(random.nextInt(100) < 10)level_2 = random.nextBoolean() ? new WorldGenObject(WorldGenObjectType.UP) : new WorldGenObject(WorldGenObjectType.DOWN);
			}
			try {
				nextAngle += level_2.next(random);
			} catch (Exception e) {
				level_2 = null;
			}
			if(random.nextInt(50) == 0){
				biome = Biome.values()[random.nextInt(Biome.values().length)];
			}
		}
		
		public class AreaOpening {
			public Node node;
			
			public AreaOpening(Node n){
				this.node = n;
			}
		}
	}
	
}
