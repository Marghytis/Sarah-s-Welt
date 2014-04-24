package util;

public class Color {

	public float r, g, b, a;
	
	public Color(){
		this(1, 1, 1, 1);
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
	
	public String toString(){
		return "Color[" + r + ", " + g + ", " + b + ", " + a + "]";
	}
}
