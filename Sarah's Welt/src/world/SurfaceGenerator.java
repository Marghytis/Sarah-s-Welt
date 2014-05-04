package world;

import java.util.Random;

import util.Line;
import world.creatures.Snail;
import world.worldGen.WorldGenObject;
import world.worldGen.WorldGenObject.WorldGenObjectType;
import core.geom.Vec;

public class SurfaceGenerator {

	public static float WIDTH;

	public Random random = new Random();
	private GenerationSettings settingsR;
	private GenerationSettings settingsL;

	Line grassT; int gTOffset = 0;
	Line grassB; int gBOffset = -10;
	Line earthT; int eTOffset = -10;
	Line earthB; int eBOffset = -100;
	Line stoneT; int sTOffset = -100;
	Line stoneB; int sBOffset = -1000;
	
	public SurfaceGenerator(){
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
		if(sarahX + (WIDTH/2) > settingsR.base.x){
			shiftRightRim(true, sarahX + (WIDTH/2));
			shiftLeftRim(true, sarahX - (WIDTH/2));
		} else if(sarahX - (WIDTH/2) < settingsL.base.x){
			shiftLeftRim(false, sarahX - (WIDTH/2));
			shiftRightRim(false, sarahX + (WIDTH/2));
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
				
				World.creatures.get(Snail.typeId).add(new Snail(grassT.start.p.minus(grassT.start.next.p).scaledBy(random.nextFloat()), grassT.start));
			}
		} else {
			while(!reachedDestination(grassT.start.next.p.x, destination, false)){
				//TODO SAVE THE DATA!!!!!
				grassT.removeFirst();
				grassB.removeLast();
				earthT.removeFirst();
				earthB.removeLast();
				stoneT.removeFirst();
				stoneB.removeLast();				
			}
		}
		grassB.end.next = grassT.start;
		grassT.start.last = grassB.end;

		earthB.end.next = earthT.start;
		earthT.start.last = earthB.end;

		stoneB.end.next = stoneT.start;
		stoneT.start.last = stoneB.end;
	}
	
	public void shiftLeftRim(boolean right, float destination){
		if(!right){
			while(!reachedDestination(settingsR.base.x, destination, false)){
				
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
				
				World.creatures.get(Snail.typeId).add(new Snail(grassT.end.last.p.minus(grassT.end.p).scaledBy(random.nextFloat()), grassT.end.last));
			}
		} else {
			while(!reachedDestination(grassT.end.last.p.x, destination, true)){
				//TODO SAVE THE DATA!!!!!
				grassT.removeLast();
				grassB.removeFirst();
				earthT.removeLast();
				earthB.removeFirst();
				stoneT.removeLast();
				stoneB.removeFirst();				
			}
		}
		
		grassT.end.next = grassB.start;
		grassB.start.last = grassT.end;
		
		earthT.end.next = earthB.start;
		earthB.start.last = earthT.end;
		
		stoneT.end.next = stoneB.start;
		stoneB.start.last = stoneT.end;
	}
	
	public boolean reachedDestination(float pos, float destination, boolean dir){
		return dir ? pos >= destination: pos <= destination;
	}
	
	public static void createCycle(Line l1, Line l2){
		l1.end.next = l2.start;
		l2.start.last = l1.end;
		
		l2.end.next = l1.start;
		l1.start.last = l2.end;
	}
	
	public class GenerationSettings {

		public WorldGenObject level_1;
		public WorldGenObject level_2;
		public double nextAngle = 0;
		public boolean dir;
		
		public Vec base;
		
		public GenerationSettings(boolean dir, Vec base){
			this.dir = dir;
			this.base = base;
		}
		
		float segmentLength = 10;
		
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
		}
	}
	
}
