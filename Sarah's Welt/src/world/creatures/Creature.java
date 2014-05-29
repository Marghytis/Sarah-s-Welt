package world.creatures;

import item.DistantWeapon;
import item.Item;
import item.Weapon;

import java.util.List;

import org.lwjgl.opengl.GL11;

import particles.BloodSplash;
import particles.DeathDust;
import resources.TextureFile;
import util.Animator;
import world.Material;
import world.Node;
import world.Thing;
import world.World;
import core.Settings;
import core.geom.Vec;

public abstract class Creature extends Thing {
	
	public static void renderCreatures(){
		for(List<Creature> list : World.creatures){
			if(list.size() > 0) {
				if(list.get(0) instanceof Gnat){
					TextureFile.bindNone();
					GL11.glColor3f(0.2f, 0.2f, 0.2f);
					GL11.glBegin(GL11.GL_POINTS);
						list.forEach((c) -> ((Gnat)c).render());
					GL11.glEnd();
					GL11.glColor3f(1, 1, 1);
				} else {
					list.get(0).animator.tex.file.bind();
					list.forEach((c) -> c.render());
				}
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<Creature> list : World.creatures){
				list.forEach((c) -> {
					if(!(c instanceof Gnat)){
						GL11.glPushMatrix();
						GL11.glTranslatef(c.pos.x, c.pos.y, 0);
						c.animator.tex.texs[0][0].box.outline();
						GL11.glPopMatrix();
					}
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
	public int id;
	
	public Creature(Animator ani, Vec pos, Node worldLink, int id){
		super(ani, pos, worldLink);
		this.id = id;
	}
	
	public void update(int dTime){
		if(health <= 0){
			World.particleEffects.add(new DeathDust(pos.plus(animator.box.middle())));
			World.thingTasks.add(() -> {
				onDeath();
				World.creatures[id].remove(this);
			});
		} else {
			pos.shift(vel);
			
			vel.shift(acc.scaledBy(dTime).scaledBy(World.measureScale*dTime));
			acc.set(0, 0);
			
			if(hit > 0) hit--;
		}
	}
	
	public boolean hitBy(Creature c, Item weapon){
		if(hit == 0 && (c.pos.minus(pos).length() < c.hitradius || weapon instanceof DistantWeapon)){
			hit = 40;
			if((c instanceof Sarah)){
				health -= weapon instanceof DistantWeapon ? ((DistantWeapon)weapon).distPower : ((Weapon)weapon).power;
			} else {
				health -= c.punchStrength;
			}
			World.particleEffects.add(new BloodSplash(pos.plus(animator.box.middle())));
			return true;
		}
		return false;
	}
	
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
	
	protected void onDeath(){}
}
