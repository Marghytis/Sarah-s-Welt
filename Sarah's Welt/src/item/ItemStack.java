package item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import world.World;
import core.Menu.Button;
import core.Window;
import core.geom.Quad;


public class ItemStack extends Button{

	public Item item = null;
	int count = 0;
	int slot;
	static Quad slotQuad = new Quad(-50, -50, 100, 100);
	static Quad itemQuad = new Quad(-20, -30, 40, 40);
	
	public ItemStack(int slot){
		super(slot + "", 0, 0, null);
		this.slot = slot;
	}
	
	public void renderInInv(){
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		int oneWidth = Window.WIDTH/7;
		GL11.glTranslatef((slot+1)*oneWidth, Window.HEIGHT/5, 0);
		Res.INVENTORY.file.bind();
		slotQuad.drawTex(Res.INVENTORY);
		if(item != null) itemQuad.drawTex(Res.ITEMS.texs[item.xItemsPNG][item.yItemsPNG]);
//		float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
//		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
//		Res.font.drawString(xText, yText, name, 1, 1);
		GL11.glPopMatrix();
	}
	
	public void renderInHand(){
		if(item == null) return;
		int[] sarahHandPos = World.sarah.getHandPosition();
		
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef((Window.WIDTH/2) + World.sarah.animator.tex.box.x + sarahHandPos[0], (Window.HEIGHT/2) + World.sarah.animator.tex.box.y + sarahHandPos[1], 0);
//		try {
//			throw new AccessException("Breakpoint?");
//		} catch (AccessException e) {}
		GL11.glRotatef(item.defaultRotationHand + sarahHandPos[2], 0, 0, 1);
		
		item.hand.bind();
		item.box.drawTex(item.hand, item.handTexQuad);
		TextureFile.bindNone();
//		Texture.bindNone();
//		GL11.glColor4f(1, 0, 0, 1);
//		item.box.draw();
//		Res.ITEMS.bind();
		
		GL11.glPopMatrix();
	}
}
