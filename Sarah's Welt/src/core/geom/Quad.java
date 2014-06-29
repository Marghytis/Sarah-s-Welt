package core.geom;

import org.lwjgl.opengl.GL11;

import resources.Texture;

public class Quad extends Vec{

	public Vec size;
	
	public Quad(){
		this(0, 0, 0, 0);
	}
	
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
	
	@Override
	public Quad plus(Vec v){
		return new Quad(x + v.x, y + v.y, size.x, size.y);
	}
	
	@Override
	public Quad scaledBy(float f){
		return new Quad(x * f, y * f, size.x * f, size.y * f);
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

	@Override
	public void draw(){
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, 			y);
			GL11.glVertex2f(x + size.x, y);
			GL11.glVertex2f(x + size.x, y + size.y);
			GL11.glVertex2f(x, 			y + size.y);
		GL11.glEnd();
	}

	public static void draw(float x1, float y1, float x2, float y2){
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x1, y1);
			GL11.glVertex2f(x2, y1);
			GL11.glVertex2f(x2, y2);
			GL11.glVertex2f(x1, y2);
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
	
	public boolean intersects(Quad quad){
		return quad.contains(this) || quad.contains(x, y + size.y) || quad.contains(x + size.x, y + size.y) || quad.contains(x + size.x, y)
				|| contains(quad) || contains(quad.x, quad.y + quad.size.y) || contains(quad.x + quad.size.x, quad.y + quad.size.y) || contains(quad.x + quad.size.x, quad.y);
	}
	
	public boolean contains(Vec v){
		return contains(v.x, v.y);
	}
	
	public boolean contains(float x, float y){
		return x > this.x && x < this.x + size.x && y > this.y && y < this.y + size.y;
	}
	
	@Override
	public String toString(){
		return "Quad[" + x + "|" + y + " -- " + size.x + "|" + size.y + "]";
	}
	
}
