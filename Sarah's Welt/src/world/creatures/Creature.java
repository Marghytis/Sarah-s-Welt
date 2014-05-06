package world.creatures;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import resources.Texture;
import util.Animator;
import world.Material;
import world.Node;
import world.Thing;
import world.World;
import core.Settings;
import core.geom.Vec;

public abstract class Creature extends Thing {
	
	public static void renderCreatures(){
		for(ArrayList<Creature> list : World.creatures){
			if(list.size() > 0) {
				list.get(0).animator.tex.bind();
				list.forEach((c) -> c.render());
			}
		}
		Texture.bindNone();
		if(Settings.hitbox){
			for(ArrayList<Creature> list : World.creatures){
				list.forEach((c) -> {
					GL11.glPushMatrix();
					GL11.glTranslatef(c.pos.x, c.pos.y, 0);
					c.animator.tex.box.outline();
					GL11.glPopMatrix();
				});
			}
		}
	}
	
	protected Vec acc = new Vec();
	protected Vec vel = new Vec();
	
	//combat
	public int hit = 0;
	public int hitradius = 100; //only relevant, if its aggressive
	public int health = 20;
	public int punchStrength = 1;
	
	public Creature(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
	
	public void update(int dTime){
		pos.add(vel);
		
		vel.add(acc.scaledBy(dTime).scaledBy(World.measureScale*dTime));
		acc.set(0, 0);
		
		if(hit > 0) hit--;
	}
	
	public boolean hitBy(Creature c){
		if(hit == 0 && c.pos.minus(pos).length() < c.hitradius){
			hit = 40;
			health -= c.punchStrength;
			return true;
		}
		return false;
	}
	
	public boolean rightClickAction(){return false;}
	
	/**
	 * Applies the friction of the specified material to the acceleration
	 * @param mat
	 */
	public void applyFriction(Material mat){
		acc.x -= mat.decelerationFactor*(vel.x*vel.x) * (vel.x > 0 ? 1 : -1);
		acc.y -= mat.decelerationFactor*(vel.y*vel.y) * (vel.y > 0 ? 1 : -1);
	}
	
	@Override
	protected void beforeRender(){
		if(vel.x > 0){
			mirrored = true;
		} else if(vel.x < 0){
			mirrored = false;
		}
	}
}
