package main;

import resources.ResLoader;
import resources.StackedTexture;
import resources.Texture;
import util.Quad;

public class Button extends Quad{
	
	public static StackedTexture tex = (StackedTexture) Texture.MENU_BUTTON;
	
	public String name;
	public int state = 0;
	
	public Button(String name, float x, float y, float width, float height){
		super(x, y, width, height);
		this.name = name;
	}
	
	public void render(){
		draw(tex, 0, state);
		float xMiddle = x + (width/2);
		float yMiddle = y + (height/2);
		ResLoader.font.drawString(xMiddle - (ResLoader.font.getWidth(name)/3), yMiddle - (ResLoader.font.getHeight()/2), name, 1, 1);
	}
}
