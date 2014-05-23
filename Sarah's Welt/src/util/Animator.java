package util;

import resources.StackedTextures;

public class Animator {

	public Animation animation;
	public int frame;
	public StackedTextures tex;
	public Runnable doOnReady;
	
	public Animator(StackedTextures tex, Animation defaultA){
		this(tex, () -> {}, defaultA);
	}
	
	public Animator(StackedTextures tex, Runnable doOnReady, Animation defaultA){
		this.tex = tex;
		this.doOnReady = doOnReady;
		this.animation = defaultA;
	}
	
	public void animate(boolean mirrored){
		if(animation != null){
			int texPos = animation.getPoint(frame);
			
			tex.box.drawTexNotBind(tex, texPos, animation.y, mirrored);
	
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
