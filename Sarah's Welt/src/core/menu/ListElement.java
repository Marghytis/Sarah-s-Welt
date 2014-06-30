package core.menu;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import core.Window;

public class ListElement extends Component{

	public ListElement(float x, float y, float width, float height, String name, String text, Runnable onClick) {
		super(x, y, width, height, name, text, onClick);
	}

	public void render() {
		
		TextureFile.bindNone();
		GL11.glPushMatrix();
		GL11.glTranslatef((x*Window.WIDTH) - (size.x/2), (y*Window.HEIGHT) - (size.y/2), 0);
		GL11.glColor4f(1, 1, 1, 0.7f);
		draw();
		
		GL11.glColor4f(0, 0, 0, 1);
		float xText = x + (size.x/2) - (Res.font.getWidth(text)/3);
		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(xText, yText, text, 1, 1);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}
}
