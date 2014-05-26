package world.creatures;

import org.lwjgl.opengl.GL11;

import world.Material;
import world.Node;
import core.geom.Vec;


public class Gnat extends WalkingCreature{

	public static int typeId;
		
	public Gnat(Vec p, Node worldLink){
		super(null, p, worldLink);
		front = true;
		health = 5;
	}
	
	public void update(int dTime){
		if(!g){
			acc.shift((0.5f - random.nextFloat())*0.00003f, (0.49f - random.nextFloat())*0.00003f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(100) == 0){
				pos.y++;
				accelerateFromGround(new Vec(0, 0.0001f));
			}
		}
		
		if(!g) collision();
		
		super.update(dTime);
	}
	
	public void render(){
		GL11.glVertex2f(pos.x-1, pos.y-1);
		GL11.glVertex2f(pos.x, pos.y-1);
		GL11.glVertex2f(pos.x-1, pos.y);
		GL11.glVertex2f(pos.x, pos.y);
	}
}
