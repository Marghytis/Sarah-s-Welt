package util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Color {
	
	public static Color BLACK = new Color(0, 0, 0);

	public float r, g, b, a;
	
	public Color(Random random, float minimum){
		this(minimum + (random.nextFloat()*(1-minimum)), minimum + (random.nextFloat()*(1-minimum)), minimum + (random.nextFloat()*(1-minimum)));
	}
	
	public Color(){
		this(1, 1, 1, 1);
	}
	
	public Color(String string){
		String[] args = string.split("_");
		r = Float.parseFloat(args[0]);
		g = Float.parseFloat(args[1]);
		b = Float.parseFloat(args[2]);
		a = Float.parseFloat(args[3]);
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
	
	public void setGL(){
		GL11.glColor4f(r, g, b, a);
	}
	
	public static void setGL(float r, float g, float b, float a){
		GL11.glColor4f(r, g, b, a);
	}
	
	@Override
	public String toString(){
		return "Color[" + r + ", " + g + ", " + b + ", " + a + "]";
	}
	
	public String toString2(){
		return r + "_" + g + "_" + b + "_" + a;
	}
}
