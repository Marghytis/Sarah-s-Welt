package world.structures;

import org.lwjgl.opengl.GL11;

import particles.RainEffect;
import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Cloud extends Structure{

	public static StackedTexture CLOUD = new StackedTexture("structures/Cloud", 1, 1, -0.5f, -0.5f);
	private RainEffect effect;
	public float xSize;
	public float ySize;
	
	public Cloud(Point pos, Node worldLink, float xSize, float ySize){
		super(CLOUD, new Animation(1, 1), pos, worldLink);
		this.xSize = xSize;
		this.ySize = ySize;
		effect = new RainEffect(new Point(pos.x + ((tex.box.x*xSize)/2), pos.y + (tex.box.y*xSize)), (tex.box.width*xSize)/2, (tex.box.height*xSize)/2);
		front = true;
	}
	
	public void tick(float dTime){
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
