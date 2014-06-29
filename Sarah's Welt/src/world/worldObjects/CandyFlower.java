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

public class CandyFlower extends WorldObject {

	public static int variantId;
	
	public int variant;
	public static Color[] colors = {
			new Color(0.79f, 0.91f, 0.31f),
			new Color(0.86f, 0.64f, 0.27f),
			new Color(0.91f, 0.38f, 0.31f),
			new Color(0.86f, 0.64f, 0.27f),
			new Color(0.43f, 0.84f, 0.84f),
			new Color(0.84f, 0.43f, 0.84f)
	};
	public Color color;
	
	public CandyFlower(int variant, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_FLOWER, new Animation(0, variant)), pos, worldLink, random.nextBoolean(), ObjectType.CANDY_FLOWER);
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
		
		return new CandyFlower(variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
}