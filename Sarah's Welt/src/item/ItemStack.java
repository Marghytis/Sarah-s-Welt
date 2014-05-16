package item;

import org.lwjgl.opengl.GL11;

import core.Window;
import world.World;


public class ItemStack{

	Item item;
	int count;
	
	public ItemStack(Item item){
		this(item, 1);
	}
	
	public ItemStack(Item item, int count){
		this.item = item;
		this.count = count;
	}
	
	public void renderInHand(){
		
		int[] sarahHandPos = World.sarah.getHandPosition();
		
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef((Window.WIDTH/2) - World.sarah.animator.tex.box.x + sarahHandPos[0], (Window.HEIGHT/2) - World.sarah.animator.tex.box.y + sarahHandPos[1], 0);
		GL11.glRotatef(item.defaultRotationHand + sarahHandPos[2], 0, 0, 1);
		
		item.handTexQuad.drawTex(item.hand, item.handTexQuad);
		
		GL11.glPopMatrix();
	}
}
