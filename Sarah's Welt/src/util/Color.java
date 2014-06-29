package util;

import org.lwjgl.opengl.GL11;

public class Color {

	public float r, g, b, a;
	
	public Color(){
		this(1, 1, 1, 1);
	}
	
	public Color(float r, float g, float b){
		this(r, g, b, 1);
	}
	
	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void set(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void set(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void set(Color c){
		set(c.r, c.g, c.b, c.a);
	}
	
	public void set(){
		GL11.glColor4f(r, g, b, a);
	}
	
	@Override
	public String toString(){
		return "Color[" + r + ", " + g + ", " + b + ", " + a + "]";
	}
}
