package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JungleBush extends WorldObject{

	public float size;
	
	public JungleBush(Vec pos, Node worldLink, float size){
		super(new Animator(Res.JUNGLE_BUSH, new Animation(0, 0)), pos, worldLink, false, ObjectType.JUNGLE_BUSH);
		this.size = size;
	}
	
	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		
		return new JungleBush(new Vec(x, y), worldLink, size);
	}

	@Override
	public String createMetaString() {
		return size + "";
	}
	
}