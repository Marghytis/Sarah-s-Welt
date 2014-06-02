package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bush extends WorldObject{

	public static int typeId;
	
	public int type;
	
	public int berryAmount;
	
	public Bush(int type, Vec pos, Node worldLink){
		super(new Animator(Res.BUSH, new Animation(0, type)), pos, worldLink, typeId);
		this.front = random.nextInt(10) == 0;
		this.type = type;
		berryAmount = random.nextInt(2);
	}
	
	public boolean rightClickAction(){
		if(berryAmount > 0 && type == 1){
			Inventory.addItem(Item.berry);
			berryAmount--;
			return true;
		}
		return false;
	}
	
	public void beforeRender(){
		GL11.glRotatef(worldLink.getPoint().minus(worldLink.getNext().getPoint()).angle()*(180/(float)Math.PI), 0, 0, 1);//worldLink.p.minus(worldLink.getNext().p).angle()
	}
}
