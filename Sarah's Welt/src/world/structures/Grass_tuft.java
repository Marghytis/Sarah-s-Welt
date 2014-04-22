package world.structures;

import world.Node;
import world.Point;

public class Grass_tuft extends Structure{
	
	int[] wave = {0, 1, 2, 3, 2, 1}; int cWave = 0;

	public Grass_tuft(Point pos, Node worldLink){
		super(Structure.GRASS_TUFT, pos, worldLink);
	}
	
	protected void howToRender(){
		frameX = cWave/14;
		
		cWave++;
		if(cWave/14 >= wave.length){
			cWave = 0;
		}
	}
	
}
