package world.worldObjects;

import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import core.geom.Vec;

public class Tree extends WorldObject{
	
	public int type;
	public float size;
	
	public Tree(int type, Vec pos, Node worldLink, float size){
		this(size, random.nextBoolean(), false, type, pos, worldLink);
	}
	
	protected Tree(float size, boolean mirrored, boolean front, int type, Vec pos, Node worldLink){
		super(new Animator(Res.TREE, new Animation(0, type)), pos, worldLink, front, ObjectType.TREE);
		this.size = size;
		this.mirrored = mirrored;
		this.type = type;
	}
	
	@Override
	public void beforeRender(){
		GL11.glScalef(size, size, 0);
	}
	
	@Override
	public boolean rightClickAction(){
		return World.sarah.inventory.addItem(Item.stick);
	}

	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new Tree(size, mirrored, front, variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return size + ";" + mirrored + ";" + type;
	}
}
