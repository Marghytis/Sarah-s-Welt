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
		super(new Animator(Res.CLOUD, new Animation(0, 0)), pos, worldLink, false, ObjectType.CLOUD);
		this.size = size;
		this.raining = raining;
		effect = new RainEffect(new Vec(pos.x + ((animator.tex.box.x*size)/2), pos.y + (animator.tex.box.y*size) + 650), (animator.tex.box.size.x*size)/2, (animator.tex.box.size.y*size)/2);
		front = false;
	}
	
	@Override
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
	
	@Override
	public void render(){
		if(raining){
			effect.render();
			GL11.glColor4f(1, 1, 1, 1);
		}
		animator.tex.file.bind();
		super.render();
		TextureFile.bindNone();
	}
	
	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean raining = Boolean.parseBoolean(args[1]);
		
		return new Cloud(new Vec(x, y), worldLink, size, raining);
	}

	@Override
	public String createMetaString() {
		return size + ";" + raining;
	}
}
