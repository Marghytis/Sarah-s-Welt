package world.structures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.Point;

public class Crack extends Structure{
	
	public static int typeId;
	public float xSize;
	public float ySize;
	public float rotation;

	public static Animation[] crack = {new Animation(0, 0), new Animation(0, 1), new Animation(0, 2), new Animation(0, 3)};
	
	public Crack(int type, Point pos, Node worldLink, float sizeX, float sizeY, float rotation){
		super(new Animator(Res.CRACK, crack[type]), pos, worldLink);
		front = true;
		this.xSize = sizeX;
		this.ySize = sizeY;
		this.rotation = rotation;
	}
	
	public void beforeRender(){
		GL11.glScalef(xSize, ySize, 0);
		GL11.glRotatef(rotation, 0, 0, 1);
	}
}
