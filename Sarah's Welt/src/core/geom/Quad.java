package core.geom;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import resources.Texture;

public class Quad extends Vec{

	public Vec size;
	
	public Quad(float x, float y, float width, float height){
		super(x, y);
		size = new Vec(width, height);
	}

	public void set(float x, float y, float width, float height){
		set(x, y);
		size.set(width, height);
	}
	
	public Quad plus(Vec v){
		return new Quad(x + v.x, y + v.y, size.x, size.y);
	}

	public void drawTex(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x, 			y);
			GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + size.x, 	y);
			GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + size.x, 	y + size.y);
			GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	public void drawTex(Texture tex){
		tex.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x, 			y);
		GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + size.x, 	y);
		GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + size.x, 	y + size.y);
		GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
		Texture.bindNone();
	}

	public void drawTex(Texture tex, Quad quad){
		tex.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(quad.x/tex.width, 				(quad.y + quad.size.y)/tex.height); GL11.glVertex2f(x, 			y);
		GL11.glTexCoord2f((quad.x + quad.size.x)/tex.width, (quad.y + quad.size.y)/tex.height); GL11.glVertex2f(x + size.x, y);
		GL11.glTexCoord2f((quad.x + quad.size.x)/tex.width, quad.y/tex.height); 				GL11.glVertex2f(x + size.x, y + size.y);
		GL11.glTexCoord2f(quad.x/tex.width, 				quad.y/tex.height); 				GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
		Texture.bindNone();
	}

	public void drawTex(StackedTexture tex, int xTex, int yTex){
		
		tex.bind();
		
		float xOffset = xTex*tex.widthT;
		float yOffset = yTex*tex.heightT;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset, 				yOffset + tex.heightT);	GL11.glVertex2f(x, 			y);
			GL11.glTexCoord2f(xOffset + tex.widthT, yOffset + tex.heightT);	GL11.glVertex2f(x + size.x, y);
			GL11.glTexCoord2f(xOffset + tex.widthT, yOffset);				GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glTexCoord2f(xOffset, 				yOffset);				GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();

		tex.release();
	}

	public void drawTexNotBind(StackedTexture tex, int xTex, int yTex, boolean mirrored){
		
		float xOffset = xTex*tex.widthT;
		float yOffset = yTex*tex.heightT;
		if(!mirrored){
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(xOffset, 				yOffset + tex.heightT);	GL11.glVertex2f(x, 			y);
				GL11.glTexCoord2f(xOffset + tex.widthT, yOffset + tex.heightT);	GL11.glVertex2f(x + size.x, y);
				GL11.glTexCoord2f(xOffset + tex.widthT, yOffset);				GL11.glVertex2f(x + size.x, y + size.y);
				GL11.glTexCoord2f(xOffset, 				yOffset);				GL11.glVertex2f(x, 			y + size.y);
			GL11.glEnd();
		} else {
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(xOffset + tex.widthT, yOffset + tex.heightT);	GL11.glVertex2f(x, 			y);
				GL11.glTexCoord2f(xOffset, 				yOffset + tex.heightT);	GL11.glVertex2f(x + size.x, y);
				GL11.glTexCoord2f(xOffset, 				yOffset);				GL11.glVertex2f(x + size.x, y + size.y);
				GL11.glTexCoord2f(xOffset + tex.widthT, yOffset);				GL11.glVertex2f(x, 			y + size.y);
			GL11.glEnd();
		}
	}
	
	public void outline(){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + size.x, y);
			GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glVertex2f(x, y + size.y);
		GL11.glEnd();
	}
	
	public Vec middle(){
		return new Vec(x + (size.x/2), y + (size.y/2));
	}
	
	public boolean contains(Vec v){
		return contains(v.x, v.y);
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + size.x && y > this.y && y < this.y + size.y;
	}
	
	public String toString(){
		return "Quad[" + x + "|" + y + " -- " + size.x + "|" + size.y + "]";
	}
	
}
