package world.worldObjects;

import org.lwjgl.opengl.GL11;

import particles.RainEffect;
import resources.Res;
import resources.TextureFile;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Cloud extends WorldObject{

	private RainEffect effect;
	public float size;
	
	public boolean needEffectPosReset = true;
	private boolean raining = false;//should not simply be set, because you maybe need to create the effect first
	
	public Cloud(Vec pos, Node worldLink, float size, boolean raining){
		super(new Animator(Res.CLOUD, new Animation(0, 0)), pos, worldLink);
		this.size = size;
		effect = new RainEffect(new Vec(pos.x + ((animator.tex.box.x*size)/2), pos.y + (animator.tex.box.y*size) + 650), (animator.tex.box.size.x*size)/2, (animator.tex.box.size.y*size)/2);
		front = false;
		this.raining = raining;
	}
	
	public void update(int dTime){
		pos.x += 0.1f;
		
		if(raining){
			if(needEffectPosReset){
				effect.pos.set(pos.x - (effect.size.x/2), pos.y - effect.size.y);
				needEffectPosReset = false;
			} else {
				effect.pos.x += 0.1f;
				effect.tick(dTime);
			}
		}
	}
	
	public void render(){
		if(raining){
			effect.render();
			GL11.glColor4f(1, 1, 1, 1);
		}
		animator.tex.file.bind();
		super.render();
		TextureFile.bindNone();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
}
