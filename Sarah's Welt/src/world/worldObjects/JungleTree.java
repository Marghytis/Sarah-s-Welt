package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JungleTree extends WorldObject{

	public float size;
	public int variant;
	
	public JungleTree(Vec pos, Node worldLink){
		this(random.nextInt(Res.JUNGLE_TREE.texs[0].length), pos, worldLink, 0.5f + random.nextFloat(), random.nextBoolean());
	}
	
	public JungleTree(int variant, Vec pos, Node worldLink, float size, boolean mirrored){
		super(new Animator(Res.JUNGLE_TREE, new Animation()), pos, worldLink, false, ObjectType.JUNGLE_TREE);
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
		
		return new JungleTree(variant, new Vec(x, y), worldLink, size, mirrored);
	}

	@Override
	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
	
}
