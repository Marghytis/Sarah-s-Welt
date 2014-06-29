package item;

import org.lwjgl.opengl.GL11;

import util.Animator;
import world.Node;
import world.Thing;
import world.World;
import world.WorldView;
import core.geom.Vec;

public class WorldItem extends Thing{

	public Item item;
	
	public WorldItem(Item item, Vec pos, Node worldLink) {
		super(new Animator(item.texWorld), pos, worldLink, false);
		this.item = item;
	}

	public boolean rightClickAction(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			if(Inventory.stacks[i].item == item || World.sarah.pos.minus(pos).lengthSqare() > 25000){
				return false;
			} else if (Inventory.stacks[i].item == Item.fist){
				Inventory.stacks[i].item = item;
				WorldView.thingTasks.add(() -> World.items[item.id].remove(this));
				return true;
			}
		}
		return false;
	}

	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);

		item.renderWorld();
		
		GL11.glPopMatrix();
	}

	public String createMetaString() {
		return "";
	}
	
}
