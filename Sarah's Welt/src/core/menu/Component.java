package core.menu;

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
	public abstract boolean mousePressed();
	public abstract boolean mouseReleased();
}
