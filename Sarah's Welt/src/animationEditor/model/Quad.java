package animationEditor.model;

import org.lwjgl.opengl.GL11;

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
	
	public void drawTexBox(Quad texBox){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(texBox.x, 				texBox.y + texBox.size.y); 	GL11.glVertex2f(x, 			y);
			GL11.glTexCoord2f(texBox.x + texBox.size.x, texBox.y + texBox.size.y); 	GL11.glVertex2f(x + size.x, y);
			GL11.glTexCoord2f(texBox.x + texBox.size.x, texBox.y); 					GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glTexCoord2f(texBox.x, 				texBox.y); 					GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
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
		tex.release();
	}
	
	public void outline(){
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + size.x, y);
			GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glVertex2f(x, y + size.y);
		GL11.glEnd();
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + size.x && y > this.y && y < this.y + size.y;
	}
	
	public String toString(){
		return "Quad[" + x + "|" + y + " -- " + size.x + "|" + size.y + "]";
	}
	
}
