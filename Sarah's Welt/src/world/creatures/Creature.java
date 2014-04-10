package world.creatures;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Quad;
import world.Point;
import world.Thing;

public class Creature extends Thing{


	public CreatureType type;

	public Creature(CreatureType type, Point pos){
		super(1, 1);
		this.type = type;
		this.pos = pos;
		this.nextPos = new Point(pos);
	}
	
	public Creature(CreatureType type, float x, float y){
		this(type, new Point(x, y));
	}
	
	public void tick(float dTime){
		accelerate((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
		updateVel(dTime);
		updatePos();
	}
	
	int frame = 0;
	/**
	 * World coordinates
	 */
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		if(vel.x < 0){
			type.box.draw(type.tex, type.frames[frame/10], 0);
		} else {
			type.box.drawMirrored(type.tex, type.frames[frame/10], 0);
		}
		
		GL11.glPopMatrix();
		frame++; if(frame/10 >= type.frames.length)frame = 0;
	}
	
	
	public enum CreatureType {
		BUTTERFLY(new StackedTexture("butterfly1", 3, 1), -0.5f, -0.5f, 0, 1, 2, 1);

		public StackedTexture tex;
		public Quad box;
		int[] frames;
		
		CreatureType(StackedTexture tex, float xOffset, float yOffset, int... frames){
			this.tex = tex;
			box = new Quad(xOffset*tex.width, yOffset*tex.height, tex.widthP*tex.width, tex.heightP*tex.height);
			this.frames = frames;
		}
	}
}
