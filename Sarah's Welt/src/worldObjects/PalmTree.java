package worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class PalmTree extends WorldObject{

	public static int typeId;
	
	public float size;
	
	public PalmTree(int type, Vec pos, Node worldLink, float size){
		super(new Animator(Res.PALM_TREE, new Animation(0, type)), pos, worldLink, typeId);
		this.size = size;
		mirrored = random.nextBoolean();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
}
