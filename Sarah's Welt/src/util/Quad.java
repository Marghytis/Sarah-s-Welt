package util;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import resources.Texture;

public class Quad {

	public float x, y, width, height;
	
	public Quad(float width, float height){
		this(0, 0, width, height);
	}
	
	public Quad(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(float x, float y){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public void draw(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public void draw(Texture texture){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.handle);
				
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public void draw(StackedTexture texture, int xPart, int yPart){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.handle);
		
		float yOffset = yPart*texture.heightP;
		float xOffset = xPart*texture.widthP;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset, yOffset + texture.heightP);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(xOffset + texture.widthP, yOffset + texture.heightP);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(xOffset + texture.widthP, yOffset);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(xOffset, yOffset);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	public void drawMirrored(StackedTexture texture, int xPart, int yPart){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.handle);
		
		float yOffset = yPart*texture.heightP;
		float xOffset = xPart*texture.widthP;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset + texture.widthP, yOffset + texture.heightP);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(xOffset, yOffset + texture.heightP);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(xOffset , yOffset);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(xOffset + texture.widthP, yOffset);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public void outline(){
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
	}
}
