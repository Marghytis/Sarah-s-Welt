package worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Tree extends WorldObject{

	public static int typeId;
	
	public float size;
	
	static Animation[] anis = new Animation[]{
			new Animation(0, 0),
			new Animation(0, 1),
			new Animation(0, 2),
	};
	
	public Tree(int type, Vec pos, Node worldLink, float size){
		super(new Animator(Res.TREE, anis[type]), pos, worldLink, typeId);
		this.size = size;
		mirrored = random.nextBoolean();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public boolean rightClickAction(){
		return Inventory.addItem(Item.stick);
	}
	
}
