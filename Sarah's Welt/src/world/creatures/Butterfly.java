package world.creatures;

import world.Point;


public class Butterfly extends Creature{
	
	static int[] flap = {0, 1, 2, 1};int cFlap = 0;
	
	public Butterfly(Point p){
		super(Creature.BUTTERFLY, p);
	}
	
	public Butterfly(float x, float y){
		super(Creature.BUTTERFLY, x, y);
	}
	
	public void howToRender(){
		if(vel.x > 0){
			right = true;
		} else if(vel.x < 0){
			right = false;
		}
		
		frame = cFlap/10;
		
		cFlap++;
		if(cFlap/10 >= flap.length){
			cFlap = 0;
		}
	}
}
