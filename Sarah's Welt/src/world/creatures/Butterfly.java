package world.creatures;

import world.Material;
import world.Node;
import world.Point;


public class Butterfly extends Creature{
	
	static int[] flap = {0, 1, 2, 1}; int cFlap = 0;
	
	public Butterfly(int type, Point p, Node worldLink){
		super(type == 0 ? Creature.BUTTERFLY1 : Creature.BUTTERFLY2, p, worldLink);
		front = true;
	}
	
	public void tick(float dTime){
		acc.add((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
		applyFriction(Material.AIR);
		super.tick(dTime);
	}
	
	protected void howToRender(){
		super.howToRender();
		
		frameX = cFlap/10;
		
		cFlap++;
		if(cFlap/10 >= flap.length){
			cFlap = 0;
		}
	}
}
