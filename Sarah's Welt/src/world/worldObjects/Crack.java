package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Crack extends WorldObject{
	public float size;
	public int rotation;
	public int variant;

	public Crack(int variant, Vec pos, Node worldLink, float size, int rotation){
		super(new Animator(Res.CRACK, new Animation(0, variant)), pos, worldLink, true, ObjectType.CRACK);
		this.size = size;
		this.rotation = rotation;
		this.variant = variant;
	}

	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
		GL11.glRotatef(rotation, 0, 0, 1);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		int rotation = Integer.parseInt(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new Crack(variant, new Vec(x, y), worldLink, size, rotation);
	}

	@Override
	public String createMetaString() {
		return size + ";" + rotation + ";" + variant;
	}
}
