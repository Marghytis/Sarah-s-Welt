package util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import resources.Texture;
import world.Point;

public class QuadOld {

	public float x, y, width, height;
	
	public QuadOld(){
		this(0, 0);
	}
	
	public QuadOld(float width, float height){
		this(0, 0, width, height);
	}
	
	public QuadOld(QuadOld quad){
		this(quad.x, quad.y, quad.width, quad.height);
	}
	
	public QuadOld(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void set(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static void draw(float x, float y, float width, float height){
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
	}
	
	public Point randomPoint(Random random){
		return new Point(random.nextFloat()*width + x, random.nextFloat()*height + y);
	}
	
	public static void drawTex(Texture texture, float x, float y, float width, float height){
		texture.bind();
				
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
	
	public void draw(float x, float y){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public void draw(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void draw2(){
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
	}
	
	public void outline(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + width, y);
			GL11.glVertex2f(x + width, y + height);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void drawTopDown(Texture texture){
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void draw(StackedTexture texture, int xPart, int yPart){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.handle);
		
		float xOffset = xPart*texture.widthT;
		float yOffset = yPart*texture.heightT;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset, yOffset + texture.heightT);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(xOffset + texture.widthT, yOffset + texture.heightT);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(xOffset + texture.widthT, yOffset);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(xOffset, yOffset);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	public void drawMirrored(StackedTexture texture, int xPart, int yPart){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.handle);
		
		float xOffset = xPart*texture.widthT;
		float yOffset = yPart*texture.heightT;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xOffset + texture.widthT, yOffset + texture.heightT);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(xOffset, yOffset + texture.heightT);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(xOffset, yOffset);
			GL11.glVertex2f(x + width, y + height);
			GL11.glTexCoord2f(xOffset + texture.widthT, yOffset);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
	}
	
	public String toString(){
		return "Quad[" + x + "|" + y + " -- " + width + "|" + height + "]";
	}
}
