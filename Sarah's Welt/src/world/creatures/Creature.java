package world.creatures;

import java.util.ArrayList;
import java.util.List;

import resources.Res;
import resources.StackedTexture;
import resources.Texture;
import util.Animator;
import world.Material;
import world.Node;
import world.Point;
import world.Thing;
import world.WorldWindow;
import world.otherThings.Heart;

public abstract class Creature extends Thing {

	public static List<ArrayList<Creature>> creatures = new ArrayList<>();
	public static ArrayList<StackedTexture> creatureTextures = new ArrayList<>();
	
	static {
		int id = 0;
		Snail.typeId = id++; creatures.add(new ArrayList<>()); creatureTextures.add(Res.SNAIL);
		Butterfly.typeId = id++; creatures.add(new ArrayList<>()); creatureTextures.add(Res.BUTTERFLY);
		Heart.typeId = id++; creatures.add(new ArrayList<>()); creatureTextures.add(Res.HEART);
		Rabbit.typeId = id++; creatures.add(new ArrayList<>()); creatureTextures.add(Res.RABBIT);
	}
	
	public static void renderCreatures(){
		for(int i = 0; i < creatures.size(); i++){
			creatureTextures.get(i).bind();
			creatures.get(i).forEach((c) -> c.render());
		}
		Texture.bindNone();
	}
	
	public static void updateCreatures(int delta){
		for(List<Creature> list : creatures)
			list.forEach((c) -> c.update(delta));
	}
	
	protected Point acc = new Point();
	protected Point vel = new Point();
	
	//combat
	public int hit = 0;
	public int hitradius = 100; //only relevant, if its aggressive
	public int health = 20;
	public int punchStrength = 1;
	
	public Creature(Animator ani, Point pos, Node worldLink){
		super(ani, pos, worldLink);
	}
	
	public void update(int dTime){
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
