package resources;

import util.Quad;

public class ChangingTexture extends StackedTexture{

	public int hor, vert;
	public boolean repeat = true;//or ping-pong
	
	ChangingTexture(String name, int xParts, int yParts, boolean repeat) {
		super(name, xParts, yParts);
	}

	public Quad next(boolean horizontal){
		if(horizontal){
			hor++;
			if(hor > 1/widthP-1){
				if(!repeat)hor -= 2;
			} else {
				
			}
		} else {
			
		}
		return new Quad(hor*widthP, vert*heightP, widthP, heightP);
	}
}
