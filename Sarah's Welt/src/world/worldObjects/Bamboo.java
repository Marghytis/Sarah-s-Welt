package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bamboo extends WorldObject{

	public int variant;
	public float size;
	
	public Bamboo(int variant, Vec pos, Node worldLink, float size){
		this(variant, false, pos, worldLink, size, random.nextBoolean());
	}
	
	public Bamboo(int variant, boolean front, Vec pos, Node worldLink, float size, boolean mirrored){
		super(new Animator(Res.BAMBOO, new Animation(0, variant)), pos, worldLink, front, ObjectType.BAMBOO);
		this.variant = variant;
		this.size = size;
		this.mirrored = mirrored;
	}

	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new Bamboo(variant, front, new Vec(x, y), worldLink, size, mirrored);
	}

	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
}
