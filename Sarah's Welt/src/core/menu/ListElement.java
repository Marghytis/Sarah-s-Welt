package core.menu;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import core.Window;

public class ListElement extends Component{

	public Object item;
	
	public ListElement(float x, float y, float width, float height, String text, Object item, Runnable action) {
		super(x, y, width, height, text, text, action);
		this.item = item;
	}

	public void render() {
		
		TextureFile.bindNone();
		GL11.glPushMatrix();
		GL11.glTranslatef((x*Window.WIDTH) - (size.x/2), (y*Window.HEIGHT) - (size.y/2), 0);
		if(state){
			GL11.glColor4f(1, 0.8f, 0.8f, 0.5f);
		} else {
			GL11.glColor4f(1, 1, 1, 0.7f);
		}
		draw();
		
		GL11.glColor4f(0, 0, 0, 1);
		float xText = x + (size.x/2) - (Res.font.getWidth(text)/3);
		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(xText, yText, text, 1, 1);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

	public boolean mousePressed() {
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			state = true;
			return true;
		}
		return false;
	}

	public boolean mouseReleased() {return false;}
}
