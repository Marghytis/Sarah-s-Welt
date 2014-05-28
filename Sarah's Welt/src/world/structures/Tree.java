package world.structures;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Tree extends Structure{

	public static int typeId;
	
	public float size;
	
	static Animation[] anis = new Animation[]{
			new Animation(0, 0),
			new Animation(0, 1),
			new Animation(0, 2),
	};
	
	public Tree(int type, Vec pos, Node worldLink, float size){
		super(new Animator(Res.TREE, anis[type]), pos, worldLink);
		this.size = size;
		mirrored = random.nextBoolean();
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public boolean rightClickAction(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			if(Inventory.stacks[i].item == Item.stick){
				return false;
			} else if (Inventory.stacks[i].item == null){
				Inventory.stacks[i].item = Item.stick;
				return true;
			}
		}
		return false;
	}
	
}
