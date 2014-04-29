package util;

import resources.StackedTexture;

public class Animator {

	public Animation animation;
	public int frame;
	public StackedTexture tex;
	public Runnable doOnReady;
	
	public Animator(StackedTexture tex, Animation defaultA){
		this(tex, () -> {}, defaultA);
	}
	
	public Animator(StackedTexture tex, Runnable doOnReady, Animation defaultA){
		this.tex = tex;
		this.doOnReady = doOnReady;
		this.animation = defaultA;
	}
	
	public void animate(boolean mirrored){

		int texPos = animation.getPoint(frame);
		
		tex.box.drawTexNotBind(tex, texPos, animation.y, mirrored);

		if(frame != -1){
			frame = animation.next(frame);
			if(frame == -1){
				doOnReady.run();
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
