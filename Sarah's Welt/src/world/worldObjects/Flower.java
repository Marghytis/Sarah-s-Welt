package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.StackedTextures;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.WorldView;
import core.Window;
import core.geom.Vec;

public class Flower extends WorldObject {
	
	public int type;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	
	public Color color;
	
	public Flower(int type, Vec pos, Node worldLink){
		super(new Animator(Res.FLOWER, new Animation(0, type)), pos, worldLink);
		this.type = type;
		this.color = colors[type];
	}
	
	protected Flower(int type, Vec pos, Node worldLink, StackedTextures tex, Color lightColor){
		super(new Animator(tex, new Animation(0, type)), pos, worldLink);
		this.type = type;
		this.color = lightColor;
	}
	
	public void renderLight(){
		GL11.glTranslatef(pos.x - WorldView.sarah.pos.x + Window.WIDTH/2 - Res.FLOWER_LIGHT.file.width/2, -(pos.y - WorldView.sarah.pos.y) + Window.HEIGHT/2 - Res.FLOWER_LIGHT.file.height/2 - 20, 0);
		color.set();
		Res.FLOWER_LIGHT.file.bind();
		Res.FLOWER_LIGHT.box.drawTex(Res.FLOWER_LIGHT);
	}
	
}
