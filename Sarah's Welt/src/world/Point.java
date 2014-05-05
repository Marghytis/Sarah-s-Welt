package world;

import org.lwjgl.opengl.GL11;

public class Point {

	public float x, y;
	
	public Point(){
		this(0, 0);
	}
	
	public Point(Point p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Point p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point plus(Point p){
		return new Point(x + p.x, y + p.y);
	}
	
	public Point minus(Point p){
		return new Point(x - p.x, y - p.y);
	}
	
	public Point scaledBy(float f){
		return new Point(x *f, y * f);
	}
	
	public Point add(Point p){
		x += p.x;
		y += p.y;
		return this;
	}
	
	public Point add(float x, float y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Point subtract(Point p){
		x -= p.x;
		y -= p.y;
		return this;
	}
	
	public Point scale(float f){
		x *= f;
		y *= f;
		return this;
	}
	
	public float cross(Point p){
		return x*p.y - y*p.x;
	}
	
	public float length(){
		return (float)Math.sqrt((x*x) + (y*y));
	}
	
	public float lengthSqare(){
		return (x*x) + (y*y);
	}
	
	public float slope(){
		return y/x;
	}
	
	public void draw(){
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x+1, y+1);
		GL11.glVertex2f(x+1, y-1);
		GL11.glVertex2f(x-1, y-1);
		GL11.glVertex2f(x-1, y+1);
		GL11.glEnd();
	}
	
	public String toString(){
		return "( " + x + " | " + y + " )";
	}
}
