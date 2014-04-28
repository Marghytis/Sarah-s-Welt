package world.creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import resources.StackedTexture;
import util.Animation;
import world.Material;
import world.Node;
import world.Point;
import world.Thing;
import world.WorldWindow;
import world.otherThings.Heart;

public abstract class Creature extends Thing {

	public static List<Runnable> render = new ArrayList<>();
	public static List<Consumer<Integer>> update = new ArrayList<>();
	
	public static void updateEveryCreature(int dTime){
		Butterfly.updateAll(dTime);
		Rabbit.updateAll(dTime);
		Snail.updateAll(dTime);
		Sarah.updateSarah(dTime);
		Heart.updateAll(dTime);
		try {
			creatureTypes.get(0).getDeclaredMethod("updateAll");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void renderEveryCreature(){
		Butterfly.renderAll();
		Rabbit.renderAll();
		Snail.renderAll();
		Sarah.renderSarah();
		Heart.renderAll();
	}
	
	public static void forEach(Consumer<Creature> consumer){
		Butterfly.l_i_s_t.forEach(consumer);
		Rabbit.l_i_s_t.forEach(consumer);
		Snail.l_i_s_t.forEach(consumer);
		consumer.accept(Sarah.sarah);
		Heart.l_i_s_t.forEach(consumer);
	}
	
	protected Point acc = new Point();
	protected Point vel = new Point();
	
	//combat
	public int hit = 0;
	public int hitradius = 100; //only relevant, if its aggressive
	public int health = 20;
	public int punchStrength = 1;
	
	public Creature(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		super(tex, defaultAni, pos, worldLink);
	}
	
	public void tick(int dTime){
		pos.add(vel);
		
		vel.add(acc.scaledBy(dTime).scaledBy(WorldWindow.measureScale*dTime));
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
