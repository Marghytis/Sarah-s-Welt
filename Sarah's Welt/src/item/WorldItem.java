package item;

import org.lwjgl.opengl.GL11;

import world.Node;
import world.Thing;
import core.geom.Vec;

public class WorldItem extends Thing{

	public Item item;
	
	public WorldItem(Item item, Vec pos, Node worldLink) {
		super(null, pos, worldLink);
	}

	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);

		item.worldTexQuad.drawTex(item.world);
		
		GL11.glPopMatrix();
	}
	
}
