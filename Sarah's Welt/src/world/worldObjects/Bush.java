package world.worldObjects;

import item.Inventory;
import item.Item;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bush extends WorldObject{
	
	public int type;
	
	public int berryAmount;
	
	public Bush(int type, Vec pos, Node worldLink){
		super(new Animator(Res.BUSH, new Animation(0, type)), pos, worldLink);
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
	
	public void applyMetaString(String string){
		String[] args = string.split(";");
		type = Integer.parseInt(args[0]);
		berryAmount = Integer.parseInt(args[1]);
	}
	
	public String createMetaString(){
		return type + ";" + berryAmount;
	}
	
	public void beforeRender(){
		alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
	}
}
