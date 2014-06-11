package world.worldObjects;

import item.Inventory;
import item.Item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.StackedTextures;
import util.Animation;
import util.Animator;
import util.Color;
import world.Node;
import world.WorldView;
import core.Window;
import core.geom.Vec;

public class JungleFlower extends WorldObject {

public static int typeId;
	

	
	static Animation[] anis = new Animation[]{
			new Animation(0, 0),
			new Animation(0, 1),
			new Animation(0, 2),
			new Animation(0, 3),
			new Animation(0, 4),
	};
	
	public JungleFlower(int type, Vec pos, Node worldLink){
		super(new Animator(Res.JUNGLE_FLOWER, anis[type]), pos, worldLink, typeId);

		mirrored = random.nextBoolean();
	}
	
	
	public boolean rightClickAction(){
		return Inventory.addItem(Item.stick);
	}
	
}

