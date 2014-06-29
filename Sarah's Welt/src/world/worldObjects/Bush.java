package world.worldObjects;

import item.Inventory;
import item.Item;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bush extends WorldObject{
	
	public int variant;
	
	public int berryAmount;
	
	public Bush(int variant, Vec pos, Node worldLink){
		this(variant, random.nextInt(2), random.nextInt(10) == 0, pos, worldLink);
	}
	
	protected Bush(int variant, int berryAmount, boolean front, Vec pos, Node worldLink){
		super(new Animator(Res.BUSH, new Animation(0, variant)), pos, worldLink, front, ObjectType.BUSH);
		this.variant = variant;
		this.berryAmount = berryAmount;
	}
	
	@Override
	public boolean rightClickAction(){
		if(berryAmount > 0 && variant == 1){
			Inventory.addItem(Item.berry);
			berryAmount--;
			return true;
		}
		return false;
	}
	
	@Override
	public void beforeRender(){
		alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		int variant = Integer.parseInt(args[0]);
		int berryAmount = Integer.parseInt(args[1]);
		
		return new Bush(variant, berryAmount, front, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + ";" + berryAmount;
	}
}
