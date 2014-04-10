package world;

import resources.StackedTexture;
import util.Quad;

public class Butterfly extends Thing{

	public Butterfly(float x, float y) {
		super(0.1f, 0.1f);
		pos.set(x, y);
	}
	
	StackedTexture tex = new StackedTexture("butterfly1.png", 3, 1);
	
	public void tick(){
		pos.add(random.nextInt(5)-2, random.nextInt(5)-2);
		updatePos();
	}
	
	public void render(){
		(new Quad(pos.x, pos.y, ))
	}
}
