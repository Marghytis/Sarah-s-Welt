package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bamboo extends Structure{

	public static int typeId;
	public float size;
	
	public Bamboo(int type, Vec pos, Node worldLink, float size){
		super(new Animator(Res.BAMBOO, new Animation(1, type)), pos, worldLink);
		this.front = random.nextInt(10) == 0;
		this.size = size;
		mirrored = random.nextBoolean();
	}

	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
}
