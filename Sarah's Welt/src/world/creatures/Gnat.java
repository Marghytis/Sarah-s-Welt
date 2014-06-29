package world.creatures;

import org.lwjgl.opengl.GL11;

import util.Animator;
import world.Material;
import world.Node;
import core.geom.Vec;


public class Gnat extends WalkingCreature{
		
	public Gnat(Vec p, Node worldLink){
		super(new Animator(null), p, worldLink, true, CreatureType.GNAT);
		health = 5;
	}
	
	@Override
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
		
		if(!g) collision(false);
		
		super.update(dTime);
	}
	
	@Override
	public void render(){
		GL11.glVertex2f(pos.x-1, pos.y-1);
		GL11.glVertex2f(pos.x, pos.y-1);
		GL11.glVertex2f(pos.x-1, pos.y);
		GL11.glVertex2f(pos.x, pos.y);
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Gnat g = new Gnat(new Vec(x, y), worldLink);
		g.vel.set(vX, vY);
		g.health = health;
		return g;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
