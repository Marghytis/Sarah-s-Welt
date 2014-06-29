package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class PalmTree extends WorldObject{
	
	public int variant;
	public float size;
	
	public PalmTree(int variant, Vec pos, Node worldLink, float size){
		this(size, random.nextBoolean(), false, variant, pos, worldLink);
	}
	
	protected PalmTree(float size, boolean mirrored, boolean front, int variant, Vec pos, Node worldLink){
		super(new Animator(Res.PALM_TREE, new Animation(0, variant)), pos, worldLink, front, ObjectType.PALM_TREE);
		this.size = size;
		this.mirrored = mirrored;
		this.variant = variant;
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}

	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new PalmTree(size, mirrored, front, variant, new Vec(x, y), worldLink);
	}

	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
}
