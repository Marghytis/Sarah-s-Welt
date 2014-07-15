package world.worldObjects;

import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import core.geom.Vec;

public class GraveTree extends WorldObject{
	
	public int variant;
	public float size;
	
	public GraveTree(int variant, Vec pos, Node worldLink, float size){
		this(size, random.nextBoolean(), false, variant, pos, worldLink);
	}
	
	protected GraveTree(float size, boolean mirrored, boolean front, int variant, Vec pos, Node worldLink){
		super(new Animator(Res.GRAVE_TREE, new Animation(0, 0)), pos, worldLink, front, ObjectType.GRAVE_TREE);
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
		return World.sarah.inventory.addItem(Item.stick);
	}

	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		float size = Float.parseFloat(args[0]);
		boolean mirrored = Boolean.parseBoolean(args[1]);
		int variant = Integer.parseInt(args[2]);
		
		return new GraveTree(size, mirrored, front, variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
}
