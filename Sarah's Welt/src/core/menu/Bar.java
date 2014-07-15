package core.menu;

import org.lwjgl.opengl.GL11;

import util.Color;
import core.geom.Quad;

public class Bar extends Component{

	public GetInt getValue;
	public Color color;
	public int maxValue;
	public boolean upright;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param thickness
	 * @param length
	 * @param dir true --> straight
	 * @param name
	 * @param text
	 * @param getInt
	 */
	public Bar(float x, float y, float width, float height, boolean upright, String name, GetInt getValue, int maxValue, Color color) {
		super(x, y, width, height, name, "", null);
		this.getValue = getValue;
		this.color = color;
		this.maxValue = maxValue;
		this.upright = upright;
	}

	
	public static interface GetInt{
		public abstract int get();
	}


	public void render() {
//Quad
		GL11.glColor4f(1, 1, 1, 0.2f);
		draw();
//Value
		color.set();
		Quad.draw(x, y, x + (upright ? size.x : (float)getValue.get()/maxValue*size.x), y + (upright ? (float)getValue.get()/maxValue*size.y : size.y));
//Outline
		GL11.glColor4f(0.7f, 0.7f, 0.7f, 1);
		outline();
	}


	public boolean mousePressed() {
		return false;
	}


	public boolean mouseReleased() {
		return false;
	}
}
