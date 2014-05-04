package core.geom;

import org.lwjgl.opengl.GL11;

public class Vec {

	public float x, y;
	
	public Vec(){
		this(0, 0);
	}
	
	public Vec(Vec base){
		this.x = base.x;
		this.y = base.y;
	}
	
	public Vec(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Vec p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Vec plus(Vec p){
		return plus(p.x, p.y);
	}
	
	public Vec plus(float x, float y){
		return new Vec(this.x + x, this.y + y);
	}
	
	public Vec minus(Vec p){
		return new Vec(x - p.x, y - p.y);
	}
	
	public Vec scaledBy(float f){
		return new Vec(x *f, y * f);
	}
	
	public Vec add(Vec p){
		x += p.x;
		y += p.y;
		return this;
	}
	
	public Vec add(float x, float y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vec subtract(Vec p){
		x -= p.x;
		y -= p.y;
		return this;
	}
	
	public Vec scale(float f){
		x *= f;
		y *= f;
		return this;
	}
	
	public float cross(Vec p){
		return x*p.y - y*p.x;
	}
	
	public float length(){
		return (float)Math.sqrt((x*x) + (y*y));
	}
	
	public float slope(){
		return y/x;
	}
	
	public void draw(){
		GL11.glBegin(GL11.GL_LINE);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}
	
	public String toString(){
		return "Vec[ " + x + " | " + y + " ]";
	}
}
