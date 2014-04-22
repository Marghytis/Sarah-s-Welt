package world.creatures;

import resources.StackedTexture;
import world.Material;
import world.Node;
import world.Point;


public class Butterfly extends Creature{

	public static StackedTexture BROUN  = new StackedTexture("butterfly1", 5, 1, -0.5f, -0.5f);
	public static StackedTexture BLUE  = new StackedTexture("butterfly2", 5, 1, -0.5f, -0.5f);
	
	static int[] flap = {0, 1, 2, 3, 3, 2, 1}; int cFlap = 0;
	
	public Butterfly(int type, Point p, Node worldLink){
		super(type == 0 ? BROUN : BLUE, p, worldLink);
		front = true;
		health = 5;
	}
	
	public void tick(float dTime){
		acc.add((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
		applyFriction(Material.AIR);
		super.tick(dTime);
	}
	
	protected void howToRender(){
		super.howToRender();
		
		frameX = flap[cFlap/5];
		
		cFlap++;
		if(cFlap/5 >= flap.length){
			cFlap = 0;
		}
		if(hit > 0)hit--;
	}
}
