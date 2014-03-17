package world;

import resources.Texture;


public enum Material {

	AIR("", 00.8f, false),
	STONE("Stone", 2f, true),
	EARTH("Earth", 3f, true),
	GRASS("Grass", 3f, true),
	WOOD("Wood", 1.2f, true),
	SNOW("Snow", 1.2f, false);
	
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
