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
	
	public boolean mirrored;
	public boolean front;
	
	public Thing(Animator ani, Vec pos, Node worldLink, boolean front){
		this.pos = pos;
		this.worldLink = worldLink;
		this.animator = ani;
		this.front = front;
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

	
	public void alignWithGround(){
		GL11.glRotatef(worldLink.p.minus(worldLink.next.p).angle()*(180/(float)Math.PI), 0, 0, 1);
	}
	
	public abstract String createMetaString();
}
