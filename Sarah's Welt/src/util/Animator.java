package util;

import resources.StackedTexture;
import world.Point;

public class Animator {

	public Animation animation;
	public int frame;
	public Quad texBox;
	public Runnable doOnReady;
	
	public Animator(Quad box){
		this(box, () -> {});
	}
	
	public Animator(Quad box, Runnable doOnReady){
		this.texBox = box;
		this.doOnReady = doOnReady;
	}
	
	public void animate(StackedTexture tex, boolean mirrored){
		
		Point texPos = animation.getPoint(frame);
		
		if(mirrored){
			texBox.drawMirrored(tex, (int)texPos.x, (int)texPos.y);
		} else {
			texBox.draw(tex, (int)texPos.x, (int)texPos.y);
		}

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
