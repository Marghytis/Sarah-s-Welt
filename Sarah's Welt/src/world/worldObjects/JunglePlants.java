package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JunglePlants extends WorldObject{

	public int variant;
	public float size;
	
	public JunglePlants(Vec pos, Node worldLink){
		this(random.nextInt(Res.JUNGLE_PLANTS.texs[0].length), pos, worldLink, random.nextFloat() + 0.5f, random.nextBoolean());
	}
	
	public JunglePlants(int variant, Vec pos, Node worldLink, float size, boolean mirrored){
		super(new Animator(Res.JUNGLE_PLANTS, new Animation()), pos, worldLink, false, ObjectType.JUNGLE_PLANT);
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
		
		return new JunglePlants(variant, new Vec(x, y), worldLink, size, mirrored);
	}

	@Override
	public String createMetaString() {
		return size + ";" + mirrored + ";" + variant;
	}
	
}
