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

	public void drawTex(Texture tex){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.handle);
		GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x, 			y);
		GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + size.x, 	y);
		GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + size.x, 	y + size.y);
		GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	public void drawTex(StackedTexture tex, int x, int y){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.handle);
		
		float xOffset = x*tex.widthT;
		float yOffset = y*tex.heightT;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset, 				yOffset + tex.heightT);	GL11.glVertex2f(x, 			y);
			GL11.glTexCoord2f(xOffset + tex.widthT, yOffset + tex.heightT);	GL11.glVertex2f(x + size.x, y);
			GL11.glTexCoord2f(xOffset + tex.widthT, yOffset);				GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glTexCoord2f(xOffset, 				yOffset);				GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + size.x && y > this.y && y < this.y + size.y;
	}
	
	public String toString(){
		return "Quad[" + x + "|" + y + " -- " + size.x + "|" + size.y + "]";
	}
	
}
