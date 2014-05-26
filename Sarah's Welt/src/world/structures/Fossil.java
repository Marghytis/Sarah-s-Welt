package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Fossil extends Structure{
	
	public static int typeId;
	public float size;
	public int rotation;

	public static Animation[] crack = {new Animation(0, 0), new Animation(0, 1), new Animation(0, 2), new Animation(0, 3)};
	
	public Fossil(int type, Vec pos, Node worldLink, float size, int rotation){
		super(new Animator(Res.FOSSIL, crack[type]), pos, worldLink);
		front = true;
		this.size = size;
		this.rotation = rotation;
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
		GL11.glRotatef(rotation, 0, 0, 1);
	}
}