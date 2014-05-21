package world;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import util.Animator;
import core.geom.Vec;

public abstract class Thing {
	
	public static Random random = new Random();
	//Position
	public Vec pos = new Vec();
	public Node worldLink;
	
	//For rendering
	public Animator animator;
	
	public boolean mirrored = false;
	public boolean front = false;
	
	public Thing(Animator ani, Vec pos, Node worldLink){
		this.pos = pos;
		this.worldLink = worldLink;
		this.animator = ani;
	}

	public void update(int dTime){}
	
	/**
	 * World coordinates
	 */
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		beforeRender();
		animator.animate(mirrored);
		afterRender();
		
		GL11.glPopMatrix();
	}
	
	public boolean rightClickAction(){return false;}

	protected void beforeRender(){}

	protected void afterRender(){}
}
