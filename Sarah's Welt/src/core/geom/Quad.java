package core.geom;

import org.lwjgl.opengl.GL11;

import resources.Texture;

public class Quad extends Vec{

	public Vec size;
	
	public Quad(float x, float y, float width, float height){
		super(x, y);
		size = new Vec(width, height);
	}

	public Quad(Vec offset, Vec vec) {
		super(offset);
		size = new Vec(vec);
	}

	public void set(float x, float y, float width, float height){
		set(x, y);
		size.set(width, height);
	}
	
	public Quad plus(Vec v){
		return new Quad(x + v.x, y + v.y, size.x, size.y);
	}

	/**
	 * Doesn't bind the texture
	 * @param tex
	 */
	public void drawTex(Texture tex){
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tex.quad.x, 		tex.quad.size.y); 	GL11.glVertex2f(x, 			y);//texs coords are right like this
			GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.size.y); 	GL11.glVertex2f(x + size.x, y);
			GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.y); 		GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glTexCoord2f(tex.quad.x, 		tex.quad.y); 		GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	/**
	 * Doesn't bind the texture
	 * @param tex
	 */
	public void drawTexMirrored(Texture tex){
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.size.y); 	GL11.glVertex2f(x, 			y);//texs coords are right like this
			GL11.glTexCoord2f(tex.quad.x, 		tex.quad.size.y); 	GL11.glVertex2f(x + size.x, y);
			GL11.glTexCoord2f(tex.quad.x, 		tex.quad.y); 		GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.y); 		GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	/**
	 * Doesn't bind the texture
	 * @param tex
	 */
	public void drawTexFlipped(Texture tex){
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(tex.quad.x, 		tex.quad.y); 	GL11.glVertex2f(x, 			y);//texs coords are right like this
		GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.y); 	GL11.glVertex2f(x + size.x, y);
		GL11.glTexCoord2f(tex.quad.size.x, 	tex.quad.size.y); 		GL11.glVertex2f(x + size.x, y + size.y);
		GL11.glTexCoord2f(tex.quad.x, 		tex.quad.size.y); 		GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	public void draw(){
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, 			y);
			GL11.glVertex2f(x + size.x, y);
			GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}
	
	/**
	 * Doesn't unbind the texture
	 */
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
