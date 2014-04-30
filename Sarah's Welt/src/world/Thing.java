package world;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import util.Animator;

public abstract class Thing {
	
	public static Random random = new Random();
	//Position
	public Point pos = new Point();
	public Node worldLink;
	
	//For rendering
	public Animator animator;
	
	protected boolean mirrored = false;
	public boolean front = false;
	
	public Thing(Animator ani, Point pos, Node worldLink){
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

	protected void beforeRender(){}

	protected void afterRender(){}
}
