package world.structures;

import org.lwjgl.opengl.GL11;

import core.Window;
import main.Settings;
import resources.Shader;
import resources.StackedTexture;
import resources.Texture;
import util.Animation;
import util.Color;
import world.Node;
import world.Point;
import world.WorldWindow;

public class Flower extends Structure {
	
	public int type;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	public static StackedTexture[] FLOWER = {	new StackedTexture("structures/Flower1", 1, 1, -0.5f, 0f),
												new StackedTexture("structures/Flower2", 1, 1, -0.5f, 0f),
												new StackedTexture("structures/Flower3", 1, 1, -0.5f, 0f)};
	public static Texture light = new Texture("Light_dimmed");
	
	public Flower(int type, Point pos, Node worldLink){
		super(FLOWER[type], new Animation(1, 1), pos, worldLink);
		this.type = type;
	}
	
	public void afterRender(){
		GL11.glLoadIdentity();
		GL11.glTranslatef(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2 - light.width/2, -(pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 - light.height/2 - 20, 0);
		if(Settings.shader){
			colors[type].set();
			WorldWindow.light.bind();
				Shader.Test.bind();
					light.box.draw(light);
				Shader.Test.release();
			WorldWindow.light.release();
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
}
