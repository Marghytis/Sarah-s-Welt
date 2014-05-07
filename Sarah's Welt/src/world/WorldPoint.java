package world;

public class WorldPoint extends Point{

	public static int indexIndex;
	
	public int index;
	
	public WorldPoint(float x, float y){
		super(x, y);
		index = indexIndex++;
	}
	
}
