package item;

import org.lwjgl.opengl.GL11;

import resources.Res;
import core.Window;
import core.geom.Quad;


public class ItemStack extends Quad{

	public Item item = null;
	public int count = 0;
	public int slot;
	
	public ItemStack(int slot){
		super((slot+1)*(Window.WIDTH/7) -50, Window.HEIGHT/5 -50, 100, 100);
		this.slot = slot;
	}
	
	public void render(){
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Res.INVENTORY.file.bind();
		drawTex(Res.INVENTORY.texs[0][slot == Inventory.selectedItem ? 1 : 0]);
		
		Res.ITEMS_INV.file.bind();
		if(item != null && item.texInv != null){
			drawTex(item.texInv);
		}
		
//		float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
//		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
//		Res.font.drawString(xText, yText, name, 1, 1);
		GL11.glPopMatrix();
	}
}
