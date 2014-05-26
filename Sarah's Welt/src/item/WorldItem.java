package item;

import java.util.List;

import org.lwjgl.opengl.GL11;

import resources.TextureFile;
import util.Animator;
import world.Node;
import world.Thing;
import world.World;
import core.Settings;
import core.geom.Vec;

public class WorldItem extends Thing{

	public Item item;
	
	public WorldItem(Item item, Vec pos, Node worldLink) {
		super(new Animator(item.texWorld), pos, worldLink);
		this.item = item;
	}
	
	public boolean rightClickAction(){
		for(int i = 0; i < Inventory.stacks.length; i++){
			if(Inventory.stacks[i].item == item){
				return false;
			} else if (Inventory.stacks[i].item == null){
				Inventory.stacks[i].item = item;
//				World.items[item.id].remove(this);TODO
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

	public static void renderItems() {
		for(List<WorldItem> list : World.items){
			if(list.size() > 0) {
				list.get(0).animator.texture.file.bind();
				list.forEach((c) -> c.render());
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<WorldItem> list : World.items){
				list.forEach((c) -> {
					GL11.glPushMatrix();
					GL11.glTranslatef(c.pos.x, c.pos.y, 0);
					c.animator.texture.box.outline();
					GL11.glPopMatrix();
				});
			}
		}
	}
	
}
