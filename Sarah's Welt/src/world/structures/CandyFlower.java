package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.World;
import core.Window;
import core.geom.Vec;

public class CandyFlower extends Structure {

	public static int typeId;
	
	public int type;
	public static Color[] colors = {
			new Color(0.3f, 0.56f, 0.74f),
			new Color(0.5f, 0.56f, 0.74f),
			new Color(1, 0, 0),
			new Color(0.5f, 0.56f, 0.74f),
			new Color(0.4f, 0.58f, 0.75f),
			new Color(0.97f, 0.56f, 0.74f)
	};
	
	public CandyFlower(int type, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_FLOWER, new Animation(0, type)), pos, worldLink);
		this.type = type;
	}
	
	public void renderLight(){
		GL11.glTranslatef(pos.x - World.sarah.pos.x + Window.WIDTH/2 - Res.FLOWER_LIGHT.file.width/2, -(pos.y - World.sarah.pos.y) + Window.HEIGHT/2 - Res.FLOWER_LIGHT.file.height/2 - 20, 0);
		colors[type].set();
		Res.FLOWER_LIGHT.box.drawTex(Res.FLOWER_LIGHT);
	}
	
}