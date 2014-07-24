package world;

import resources.TextureFile;


public enum Material {

	AIR("", 0.0000005f, false),
	STONE("materials/Stone", 2f, true),
	EARTH("materials/Earth", 3f, true),
	GRASS("materials/Grass", 3f, true),
	SOIL("materials/Soil", 3f, true),
	WATER("materials/Water", 2f, false),
	CANDY("materials/Candy", 3f, true),
	GROUND("materials/Ground", 3f, true),
	SAND("materials/Sand", 3f, true),
	SANDSTONE("materials/SandStone", 3f, true),;
	
	public String name;
	public TextureFile textureFile;
	
	public float decelerationFactor;//density or friction; this is determined by the 'solid' boolean
	public boolean solid = true;
	
	public int highestNodeIndex;
	
	Material(String name, float decelerationFactor, boolean solid){
		textureFile = new TextureFile(name);
		this.name = name;
		
		this.decelerationFactor = decelerationFactor;
		this.solid = solid;
	}
}
