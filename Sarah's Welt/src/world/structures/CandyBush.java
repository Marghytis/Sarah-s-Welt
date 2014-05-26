package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyBush extends Structure{

	public static int typeId;
	
	public CandyBush(int type, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_BUSH, new Animation(0, type)), pos, worldLink);
		this.front = random.nextInt(10) == 0;
	}
	
	public void beforeRender(){
		GL11.glRotatef(worldLink.getPoint().minus(worldLink.getNext().getPoint()).angle()*(180/(float)Math.PI), 0, 0, 1);//worldLink.p.minus(worldLink.getNext().p).angle()
	}
	
}