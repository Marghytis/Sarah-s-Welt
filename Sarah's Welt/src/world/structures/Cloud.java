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
	
	public Cloud(Point pos, Node worldLink, float xSize, float ySize){
		super(CLOUD, new Animation(1, 1), pos, worldLink);
		box.x*=xSize;
		box.y*=ySize;
		box.width*=xSize;
		box.height*=ySize;
		effect = new RainEffect(new Point(pos.x + (box.x/2), pos.y + box.y), box.width/2, box.height/2);
		effect.start();
		front = true;
	}
	
	public void tick(float dTime){
		pos.x += 0.1f;
		effect.pos.x += 0.1f;
		effect.tick((int)dTime*1);
	}
	
	public void render(){
		GL11.glPushMatrix();
		effect.render();
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		super.render();
	}
}
