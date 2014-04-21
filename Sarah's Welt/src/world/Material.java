package world;

import resources.Texture;


public enum Material {

	AIR("", 0.0000005f, false),
	STONE("Stone_g", 2f, true),
	EARTH("Earth", 3f, true),
	GRASS("Grass", 3f, true),
	SOIL("Soil", 3f, true);
	
	public String name;
	public Texture texture;
	
	public float decelerationFactor;//density or friction; this is determined by the 'solid' boolean
	public boolean solid = true;
	
	
	Material(String name, float decelerationFactor, boolean solid){
		texture = new Texture(name);
		this.name = name;
		
		this.decelerationFactor = decelerationFactor;
		this.solid = solid;
	}
}
