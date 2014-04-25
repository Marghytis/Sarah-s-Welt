package world;

import java.util.Random;

import main.Settings;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Animation;
import util.Animator;
import util.Quad;
import world.creatures.Creature;
import world.structures.Structure;

public abstract class Thing {
	
	public static Random random = new Random();
	//Position
	public Point pos = new Point();
	public Node worldLink;
	
	//For rendering
	public Quad box;
	public StackedTexture tex;
	public Animator animator;
	
	protected boolean mirrored = false;
	public boolean front = false;
	
	public Thing(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		this.tex = tex;
		this.pos = pos;
		this.worldLink = worldLink;
		
		this.box = new Quad(tex.xOffset*tex.widthS, tex.yOffset*tex.heightS, tex.widthS, tex.heightS);
		animator = new Animator(box);
		animator.setAnimation(defaultAni);
	}

	public void tick(float dTime){}
	
	/**
	 * World coordinates
	 */
	public void render(){
		howToRender();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		animator.animate(tex, mirrored);
		
		if(Settings.hitbox){
			if(this instanceof Structure){
				GL11.glColor3f(0, 1, 1);
				box.outline();
			} else if(this instanceof Creature){
				GL11.glColor3f(1, 0, 0);
				box.outline();
			}
			GL11.glColor3f(1, 1, 1);
		}
		GL11.glPopMatrix();
	}

	protected void howToRender(){}
}
