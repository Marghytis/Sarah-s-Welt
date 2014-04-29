package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Shader;
import resources.Texture;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.Point;
import world.WorldWindow;
import core.Settings;
import core.Window;

public class Flower extends Structure {

	public static int typeId;
	
	public int type;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	public static Texture FLOWER_LIGHT = new Texture("Light_dimmed");
	
	public Flower(int type, Point pos, Node worldLink){
		super(new Animator(Res.FLOWER, new Animation(type, 1)), pos, worldLink);
		this.type = type;
	}
	
	public void afterRender(){
		GL11.glLoadIdentity();
		GL11.glTranslatef(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2 - FLOWER_LIGHT.width/2, -(pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 - FLOWER_LIGHT.height/2 - 20, 0);
		if(Settings.shader){
			colors[type].set();
			WorldWindow.light.bind();
				Shader.Test.bind();
					FLOWER_LIGHT.box.drawTex(FLOWER_LIGHT);
				Shader.Test.release();
			WorldWindow.light.release();
			GL11.glColor4f(1, 1, 1, 1);
		}
	}
	
}
