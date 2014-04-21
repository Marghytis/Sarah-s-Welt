package world.creatures;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Quad;
import world.Point;
import world.Thing;

public class Creature extends Thing{

	public static StackedTexture BUTTERFLY  = new StackedTexture("butterfly1", 3, 1, -0.5f, -0.5f);
	
	public StackedTexture tex;
	public Quad box;
	
	public boolean right;
	int frame = 0;
	

	public Creature(StackedTexture tex, Point pos){
		super(1, 1);
		this.tex = tex;
		this.pos = pos;
		this.nextPos = new Point(pos);
		box = new Quad(tex.xOffset, tex.yOffset, tex.width*tex.widthP, tex.height*tex.heightP);
	}
	
	public Creature(StackedTexture tex, float x, float y){
		this(tex, new Point(x, y));
	}
	
	public void tick(float dTime){
		accelerate((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
		updateVel(dTime);
		updatePos();
	}
	
	/**
	 * World coordinates
	 */
	public void render(){
		howToRender();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		if(right){
			box.drawMirrored(tex, frame, 0);
		} else {
			box.draw(tex, frame, 0);
		}
		
		GL11.glPopMatrix();
	}
	

	public void howToRender(){}
	
}
