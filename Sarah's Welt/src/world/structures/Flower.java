package world.structures;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import core.Settings;
import core.Window;
import resources.Shader;
import resources.StackedTexture;
import resources.Texture;
import util.Animation;
import util.Color;
import world.Node;
import world.Point;
import world.WorldWindow;

public class Flower extends Structure {
	
	public static List<Flower> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		FLOWER.bind();
			l_i_s_t.forEach((b) -> b.render());
			FLOWER.release();
	}
	
	public int type;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	public static StackedTexture FLOWER = new StackedTexture("structures/Flower", 3, 1, -0.5f, 0f);
	public static Texture FLOWER_LIGHT = new Texture("Light_dimmed");
	
	public Flower(int type, Point pos, Node worldLink){
		super(FLOWER, new Animation(type, 1), pos, worldLink);
		this.type = type;
	}
	
	public void afterRender(){
		GL11.glLoadIdentity();
		GL11.glTranslatef(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2 - FLOWER_LIGHT.width/2, -(pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 - FLOWER_LIGHT.height/2 - 20, 0);
		if(Settings.shader){
			colors[type].set();
			WorldWindow.light.bind();
				Shader.Test.bind();
					FLOWER_LIGHT.box.draw(FLOWER_LIGHT);
				Shader.Test.release();
			WorldWindow.light.release();
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
}
