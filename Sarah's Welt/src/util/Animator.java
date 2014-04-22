package util;

import resources.StackedTexture;
import world.Point;

public class Animator {

	public Animation animation;
	public int frame;
	public Quad texBox;
	
	public Animator(Quad box){
		this.texBox = box;
	}
	
	public void animate(StackedTexture tex, boolean mirrored){
		
		Point texPos = animation.getPoint(frame);
		frame = animation.next(frame);
		
		if(mirrored){
			texBox.drawMirrored(tex, (int)texPos.x, (int)texPos.y);
		} else {
			texBox.draw(tex, (int)texPos.x, (int)texPos.y);
		}
	}
	
	public void setAnimation(Animation ani){
		if(!ani.equals(animation)){
			animation = ani;
			frame = 0;
		}
	}
	
}
