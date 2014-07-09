package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Rainbow extends WorldObject{

	public float size;
	
	public Rainbow(Vec pos, Node worldLink, float size){
		super(new Animator(Res.RAINBOW, new Animation(0, 0)), pos, worldLink, false, ObjectType.RAINBOW);
		this.size = size;
		front = false;
	}
	
	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		float size = Float.parseFloat(metaString);
		
		return new Rainbow(new Vec(x, y), worldLink, size);
	}

	@Override
	public String createMetaString() {
		return size + "";
	}
}
