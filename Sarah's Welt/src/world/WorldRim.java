package world;

public class WorldRim {
	
	public float xSector;//if xSector = 0 its the rim between sector 0 and sector 1
	public Node[][] openEndings;//one Node[] for each material, each one inside ordered from highest to lowest y
	public Node baseEnding;
	
}
