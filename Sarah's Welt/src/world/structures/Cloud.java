package world.structures;

import org.lwjgl.opengl.GL11;

import particles.RainEffect;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.Point;

public class Cloud extends Structure{

	public static int typeId;

	private RainEffect effect;
	public float xSize;
	public float ySize;
	
	public Cloud(Point pos, Node worldLink, float xSize, float ySize){
		super(new Animator(Res.CLOUD, new Animation(1, 1)), pos, worldLink);
		this.xSize = xSize;
		this.ySize = ySize;
		effect = new RainEffect(new Point(pos.x + ((animator.tex.box.x*xSize)/2), pos.y + (animator.tex.box.y*xSize)), (animator.tex.box.size.x*xSize)/2, (animator.tex.box.size.y*xSize)/2);
		front = false;
	}
	
	public void update(int dTime){
		pos.x += 0.1f;
		effect.pos.x += 0.1f;
		effect.tick((int)dTime*1);
	}
	
	public void render(){
		effect.render();
		GL11.glColor4f(1, 1, 1, 1);
		super.render();
	}
	
	public void beforeRender(){
		GL11.glScalef(xSize, ySize, 0);
	}
}
