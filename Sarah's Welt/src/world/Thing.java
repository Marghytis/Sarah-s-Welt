package world;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import core.Settings;
import resources.StackedTexture;
import util.Animation;
import util.Animator;
import world.creatures.Creature;
import world.structures.Structure;

public abstract class Thing {
	
	public static Random random = new Random();
	//Position
	public Point pos = new Point();
	public Node worldLink;
	
	//For rendering
	public StackedTexture tex;
	public Animator animator;
	
	protected boolean mirrored = false;
	public boolean front = false;
	
	public Thing(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		this.tex = tex;
		this.pos = pos;
		this.worldLink = worldLink;

		animator = new Animator(tex.box);
		animator.setAnimation(defaultAni);
	}

	public void tick(float dTime){}
	
	/**
	 * World coordinates
	 */
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		beforeRender();
		
		animator.animate(tex, mirrored);
		
		if(Settings.hitbox){
			if(this instanceof Structure){
				GL11.glColor3f(0, 1, 1);
				tex.box.outline();
			} else if(this instanceof Creature){
				GL11.glColor3f(1, 0, 0);
				tex.box.outline();
			}
			GL11.glColor3f(1, 1, 1);
		}
		afterRender();
		
		GL11.glPopMatrix();
	}

	protected void beforeRender(){}

	protected void afterRender(){}
}
