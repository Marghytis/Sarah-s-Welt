package main;

import resources.StackedTexture;
import resources.Texture;
import util.Quad;

public class Button extends Quad{
	
	//Test again
	
	public static StackedTexture tex = (StackedTexture) Texture.MENU_BUTTON;
	
	public String name;
	public int state = 0;
	
	public Button(String name, float x, float y, float width, float height){
		super(x, y, width, height);
		this.name = name;
	}
	
	public void render(){
		draw(tex, 0, state);
	}
}
