package core.menu;

import item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import util.Color;
import core.Window;
import core.WorldO;

public class ListElement extends Component{

	public Object item;
	
	public ListElement(float cX, float cY, float width, float height, String text, Object item, Runnable action) {
		super((Window.WIDTH*cX) - (width/2), (Window.HEIGHT*cY) - (height/2), width, height, text, text, action);
		this.item = item;
	}

	public void render() {
		if(item instanceof WorldO){
//Quad String
			TextureFile.bindNone();
			if(state){
				GL11.glColor4f(1, 0.8f, 0.8f, 0.5f);
			} else {
				GL11.glColor4f(1, 1, 1, 0.7f);
			}
			draw();
//Content String
			GL11.glColor4f(0, 0, 0, 1);
			float xText = x + (size.x/2) - (Res.font.getWidth(text)/3);
			float yText = y + (size.y/2) - (Res.font.getHeight()/2);
			Res.font.drawString(xText, yText, text, 1, 1);
		} else if(item instanceof ItemStack){
			GL11.glColor4f(1, 1, 1, 1);
//Quad ItemStack
			ItemStack stack = (ItemStack)item;
			
			Res.INVENTORY.file.bind();
			drawTex(Res.INVENTORY.texs[0][state ? 1 : 0]);
//Content ItemStack
			Res.ITEMS_INV.file.bind();
			if(stack != null && stack.item.texInv != null){
				drawTex(stack.item.texInv);
			}
			if(state){
				Color.setGL(1, 1, 1, 0.7f);
				Res.font.drawString(x + (size.x/2), y + (size.y/2), item.toString(), 0.5f, 0.5f);
			}
		}
		GL11.glColor4f(1, 1, 1, 1);
	}

	public boolean mousePressed() {
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			state = true;
			((ItemStack)item).inv.selectedItem = ((ItemStack)item).slot;
			return true;
		}
		return false;
	}

	public boolean mouseReleased() {return false;}
}
