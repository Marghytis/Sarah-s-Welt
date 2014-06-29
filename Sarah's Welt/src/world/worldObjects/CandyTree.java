package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyTree extends WorldObject{
	
	public float size;
	
	public CandyTree(Vec pos, Node worldLink, float size){
		this(size, random.nextBoolean(), false, pos, worldLink);
	}
	
	protected CandyTree(float size, boolean mirrored, boolean front, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_TREE, new Animation(0, 0)), pos, worldLink, front, ObjectType.CANDY_TREE);
		this.size = size;
		this.mirrored = mirrored;
	}
	
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	public boolean rightClickAction(){
		return Inventory.addItem(Item.candyCane);
	}

	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		
		return new CandyTree(size, mirrored, front, new Vec(x, y), worldLink);
	}

	public String createMetaString() {
		return size + ";" + mirrored;
	}
	
}