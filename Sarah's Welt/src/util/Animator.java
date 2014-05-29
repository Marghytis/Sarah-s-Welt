package util;

import resources.StackedTextures;
import resources.Texture;
import core.geom.Quad;

public class Animator {

	public Animation animation;
	public int frame;
	public StackedTextures tex;
	public Texture texture;
	public Quad box;
	public Runnable doOnReady;
	public boolean staticFrame;
	
	public Animator(StackedTextures tex, Animation defaultA){
		this(tex, () -> {}, defaultA);
	}
	
	public Animator(StackedTextures tex, Runnable doOnReady, Animation defaultA){
		this.tex = tex;
		this.box = tex.box;
		this.doOnReady = doOnReady;
		this.animation = defaultA;
		staticFrame = false;
	}
	
	public Animator(Texture tex){
		staticFrame = true;
		texture = tex;
		this.box = tex != null ? tex.box : new Quad();
	}
	
	public void animate(boolean mirrored){
		if(staticFrame){
			texture.box.drawTex(texture);
		} else if(animation != null){
			int texPos = animation.getPoint(frame);
			
			if(mirrored){
				tex.texs[0][0].box.drawTexMirrored(tex.texs[texPos][animation.y]);
			} else {
				tex.texs[0][0].box.drawTex(tex.texs[texPos][animation.y]);
			}
	
			if(frame != -1){
				frame = animation.next(frame);
				if(frame == -1){
					doOnReady.run();
				}
			}
		}
	}
	
	public void setAnimation(Animation ani){
		if(!ani.equals(animation)){
			animation = ani;
			frame = 0;
		}
	}
	
}
