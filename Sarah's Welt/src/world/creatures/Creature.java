package world.creatures;

import java.util.Random;

import resources.StackedTexture;
import util.Quad;
import world.Material;
import world.Node;
import world.Point;
import world.Thing;
import world.WorldWindow;

public abstract class Creature extends Thing {
	
	public static Random random = new Random();
	
	public static StackedTexture SARAH  = new StackedTexture("Sarah", 11, 1, -0.5f, -0.5f);

	public static StackedTexture BUTTERFLY1  = new StackedTexture("butterfly1", 3, 1, -0.5f, -0.5f);
	public static StackedTexture BUTTERFLY2  = new StackedTexture("butterfly2", 3, 1, -0.5f, -0.5f);
	
	protected Point acc = new Point();
	protected Point vel = new Point();
	
	public Creature(StackedTexture tex, Point pos, Node worldLink){
		super(pos, worldLink, tex, new Quad(tex.xOffset*tex.widthS, tex.yOffset*tex.heightS, tex.widthS, tex.heightS));
	}
	
	public void tick(float dTime){
		pos.add(vel);
		
		vel.add(acc.scaledBy(dTime).scaledBy(WorldWindow.measureScale*dTime));
		acc.set(0, 0);
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
	protected void howToRender(){
		if(vel.x > 0){
			mirrored = true;
		} else if(vel.x < 0){
			mirrored = false;
		}
	}
}
