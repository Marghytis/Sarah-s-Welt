package item;

import org.lwjgl.opengl.GL11;

import util.Animator;
import world.MovableThing;
import world.Node;
import world.World;
import world.WorldView;
import core.geom.Vec;

public class WorldItem extends MovableThing{

	public Item item;
	
	public WorldItem(Item item, Vec pos, Node worldLink) {
		super(new Animator(item.texWorld), pos, worldLink, false);
		this.item = item;
	}
	
	public void update(float delta){
		super.update(delta);
		item.update(delta, this);
	}

	@Override
	public boolean rightClickAction(){
		for(int i = 0; i < World.sarah.inventory.stacks.length; i++){
			if(World.sarah.inventory.stacks[i].item == item || World.sarah.pos.minus(pos).lengthSqare() > 25000){
				return false;
			} else if (World.sarah.inventory.stacks[i].item == Item.fist){
				World.sarah.inventory.stacks[i].item = item;
				WorldView.thingTasks.add(() -> World.items[item.id].remove(this));
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);

		item.renderWorld();
		
		GL11.glPopMatrix();
	}

	@Override
	public String createMetaString() {
		return "";
	}
	
}
