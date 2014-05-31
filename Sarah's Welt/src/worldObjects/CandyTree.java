package worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyTree extends WorldObject{

	public static int typeId;
	
	public float size;
	
	public CandyTree(Vec pos, Node worldLink, float size){
		super(new Animator(Res.CANDY_TREE, new Animation(0, 0)), pos, worldLink, typeId);
		this.size = size;
		mirrored = random.nextBoolean();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public boolean rightClickAction(){
		return Inventory.addItem(Item.candyCane);
	}
	
}