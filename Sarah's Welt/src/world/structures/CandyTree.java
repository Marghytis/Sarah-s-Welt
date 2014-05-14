package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyTree extends Structure{

	public static int typeId;
	
	public float size;
	
	public CandyTree(Vec pos, Node worldLink, float size){
		super(new Animator(Res.CANDY_TREE, new Animation(0, 0)), pos, worldLink);
		this.size = size;
		mirrored = random.nextBoolean();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
}