package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.World;
import core.Window;
import core.geom.Vec;

public class Flower extends WorldObject {
	
	public int variant;
	public static Color[] colors = {
			new Color(1, 1, 0),
			new Color(1, 0, 0),
			new Color(1, 1, 1)
	};
	
	public Color color;
	
	public Flower(int variant, Vec pos, Node worldLink){
		super(new Animator(Res.FLOWER, new Animation(0, variant)), pos, worldLink, random.nextBoolean(), ObjectType.FLOWER);
		this.variant = variant;
		this.color = colors[variant];
	}
	
	public void renderLight(){
		GL11.glTranslatef(pos.x - World.sarah.pos.x + Window.WIDTH/2 - Res.FLOWER_LIGHT.file.width/2, -(pos.y - World.sarah.pos.y) + Window.HEIGHT/2 - Res.FLOWER_LIGHT.file.height/2 - 20, 0);
		color.set();
		Res.FLOWER_LIGHT.file.bind();
		Res.FLOWER_LIGHT.box.drawTex(Res.FLOWER_LIGHT);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		return new Flower(variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
	
}
