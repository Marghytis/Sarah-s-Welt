package world;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Quad;

public abstract class Thing {
	//Position
	public Point pos = new Point();
	public Node worldLink;
	
	//For rendering
	protected StackedTexture tex;
	protected Quad box;
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
		pos.draw();
		GL11.glPopMatrix();
	}

	protected void howToRender(){}
}
