package world.worldGen;

import java.util.Random;

import world.worldGen.WorldGenObject.WorldGenObjectType;

public enum Landscape {
	FLAT(), HILLY(), STAIRS(new WorldGenObjectType[]{WorldGenObjectType.UP, WorldGenObjectType.DOWN}){

		public WorldGenObject newObjectPlease(Random random, int level){
			return new WorldGenObject(possibleLevelObjects[level][random.nextInt(possibleLevelObjects[level].length)]);
		}
	};
	
	WorldGenObjectType[][] possibleLevelObjects;
	
	Landscape(WorldGenObjectType[]... possibleLevelObjects){
		this.possibleLevelObjects = possibleLevelObjects;
	}
	
	public WorldGenObject newObjectPlease(Random random, int level){return null;}
}
