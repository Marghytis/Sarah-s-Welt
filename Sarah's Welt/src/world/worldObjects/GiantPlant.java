package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class GiantPlant extends WorldObject{

	public float size;
	public int variant;
	
	public GiantPlant(Vec pos, Node worldLink){
		this(random.nextInt(Res.GIANTGRAS.texs[0].length), pos, worldLink, 0.5f + random.nextFloat(), random.nextBoolean());
	}
	
	public GiantPlant(int variant, Vec pos, Node worldLink, float size, boolean mirrored){
		super(new Animator(Res.GIANTPLANT, new Animation()), pos, worldLink, false, ObjectType.GIANT_PLANT);
		this.size = size;
		this.mirrored = mirrored;
		this.variant = variant;
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
		animator.animation.y = variant;
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new GiantPlant(variant, new Vec(x, y), worldLink, size, mirrored);
	}

	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
	
}

