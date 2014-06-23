package world.worldObjects;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JungleBush extends WorldObject{

	public static int typeId;
	
	public float size;
	
	public JungleBush(Vec pos, Node worldLink, float size){
		super(new Animator(Res.JUNGLE_BUSH, new Animation(0, 0)), pos, worldLink, typeId);
		this.front = random.nextInt(10) == 0;
	}
	
	public void beforeRender(){
//		GL11.glRotatef(worldLink.getPoint().minus(worldLink.getNext().getPoint()).angle()*(180/(float)Math.PI), 0, 0, 1);//worldLink.p.minus(worldLink.getNext().p).angle()
		GL11.glScalef(size, size, 0);
		}
	
}