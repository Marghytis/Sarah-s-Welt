package world.worldGen;

import java.util.Random;

public class WorldGenObject {
	
	WorldGenObjectType type;

	public int pos = 0;
	
	public WorldGenObject(WorldGenObjectType type){
		this.type = type;
	}
	
	public double next(Random random) throws Exception {
		pos++;
		if(pos >= type.steps.length){
			pos = 0;
			throw new Exception();
		} else {
			return (type.steps[pos] - 2 + random.nextInt(5))*type.step;
		}
	}
	
	public enum WorldGenObjectType {
		RAISING(Math.PI/160, 0,1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0),
		HUBBEL(Math.PI/20, 10,9,6,3,0,-3,-6,-9,-10),
		UP(Math.PI/60, 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0),
		DOWN(Math.PI/60, 0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12,-13,-14,-15,-14,-13,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0);
		
		double step = Math.PI/160;
		public int[] steps;
		
		WorldGenObjectType(double step, int... steps){
			this.step = step;
			this.steps = steps;
		}
	}
}
