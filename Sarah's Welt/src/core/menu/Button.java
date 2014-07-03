package core.menu;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.StackedTextures;
import core.Window;

public class Button extends Component{
	
	static StackedTextures tex = Res.MENU_BUTTON;
	
	Runnable onClick;
	
	public Button(String name, float x, float y, Runnable onClick){
		super(x, y, 300, 60, name, name, onClick);
	}
	
	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef((x*Window.WIDTH) - (size.x/2), (y*Window.HEIGHT) - (size.y/2), 0);
		tex.file.bind();
		drawTex(tex.texs[0][state ? 1 : 0]);
		float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(xText, yText, name, 1, 1);
		GL11.glPopMatrix();
	}
	
	@Override
	public boolean contains(float x, float y){
		float realX = (this.x*Window.WIDTH) - (size.x/2);
		float realY = (this.y*Window.HEIGHT) - (size.y/2);
		return x > realX && x < realX + size.x && y > realY && y < realY + size.y;
	}

	public boolean mousePressed() {
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			Res.buttonSound.play();
			state = true;
			return true;
		}
		return false;
	}

	public boolean mouseReleased() {
		boolean success = false;
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			action.run();
			success = true;
		}
		state = false;
		return success;
	}
}