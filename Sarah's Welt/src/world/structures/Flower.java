package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.World;
import core.Window;
import core.geom.Vec;

public class Flower extends Structure {

	public static int typeId;
	
	public int type;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	public static Texture FLOWER_LIGHT = new Texture("Light_dimmed");
	
	public Flower(int type, Vec pos, Node worldLink){
		super(new Animator(Res.FLOWER, new Animation(0, type)), pos, worldLink);
		this.type = type;
	}
	
	public void renderLight(){
		GL11.glTranslatef(pos.x - World.sarah.pos.x + Window.WIDTH/2 - FLOWER_LIGHT.width/2, -(pos.y - World.sarah.pos.y) + Window.HEIGHT/2 - FLOWER_LIGHT.height/2 - 20, 0);
		colors[type].set();
		FLOWER_LIGHT.box.drawTex(FLOWER_LIGHT);
	}
	
}
