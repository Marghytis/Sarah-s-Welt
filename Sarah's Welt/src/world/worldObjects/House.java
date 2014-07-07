package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class House extends WorldObject{

	public static int typeId;
	
	public float size;
	public int variant;
	
	public House(int variant, Vec pos, Node worldLink){
		this(variant, pos, worldLink, 0.5f + random.nextFloat(), random.nextBoolean());
	}
	
	public House(int variant, Vec pos, Node worldLink, float size, boolean mirrored){
		super(new Animator(Res.HOUSE, new Animation()), pos, worldLink, false, ObjectType.HOUSE);
		this.size = size;
		this.mirrored = mirrored;
		this.variant = variant;
	}
	
	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
		animator.animation.y = variant;
	}
	
	@Override
	public boolean rightClickAction(){
		return Inventory.addItem(Item.stick);
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new House(variant, new Vec(x, y), worldLink, size, mirrored);
	}

	@Override
	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
	
}
