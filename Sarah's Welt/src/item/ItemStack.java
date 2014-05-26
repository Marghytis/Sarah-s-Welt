package item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import core.Window;
import core.geom.Quad;


public class ItemStack{

	public Item item = null;
	int count = 0;
	int slot;
	static Quad slotQuad = new Quad(-50, -50, 100, 100);
	static Quad itemQuad = new Quad(-20, -30, 40, 40);
	
	public ItemStack(int slot){
		this.slot = slot;
	}
	
	public void render(){
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		int oneWidth = Window.WIDTH/7;
		GL11.glTranslatef((slot+1)*oneWidth, Window.HEIGHT/5, 0);
		slotQuad.drawTex(Res.INVENTORY);
//		float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
//		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
//		Res.font.drawString(xText, yText, name, 1, 1);
		GL11.glPopMatrix();
	}
}
