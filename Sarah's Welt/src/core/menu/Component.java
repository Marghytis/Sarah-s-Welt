package core.menu;

import core.Window;
import core.geom.Quad;

public abstract class Component extends Quad{

	public String name;
	public String text;
	public Runnable action;
	
	public boolean state;
	
	public Component(float x, float y, float width, float height, String name, String text, Runnable action){
		super(x, y, width, height);
		this.name = name;
		this.text = text;
		this.action = action;
	}
	
	public abstract void render();
	
	@Override
	public boolean contains(float x, float y){
		float realX = (this.x*Window.WIDTH) - (size.x/2);
		float realY = (this.y*Window.HEIGHT) - (size.y/2);
		return x > realX && x < realX + size.x && y > realY && y < realY + size.y;
	}

	public abstract boolean mousePressed();
	public abstract boolean mouseReleased();
}
