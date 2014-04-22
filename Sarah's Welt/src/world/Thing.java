package world;

import java.util.Random;

import main.Settings;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Quad;
import world.creatures.Creature;
import world.structures.Structure;

public abstract class Thing {
	
	public static Random random = new Random();
	//Position
	public Point pos = new Point();
	public Node worldLink;
	
	//For rendering
	protected StackedTexture tex;
	public Quad box;
	protected int frameX = 0;
	protected int frameY = 0;
	
	protected boolean mirrored = false;
	public boolean front = false;
	
	public Thing(Point pos, Node worldLink, StackedTexture tex, Quad box){
		this.pos = pos;
		this.worldLink = worldLink;
		
		this.tex = tex;
		this.box = box;
	}

	public void tick(float dTime){}
	
	/**
	 * World coordinates
	 */
	public void render(){
		howToRender();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		if(mirrored){
			box.drawMirrored(tex, frameX, frameY);
		} else {
			box.draw(tex, frameX, frameY);
		}
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
