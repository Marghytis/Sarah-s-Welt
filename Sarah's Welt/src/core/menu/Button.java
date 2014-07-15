package core.menu;

import org.lwjgl.input.Mouse;

import core.Window;
import resources.Res;
import resources.StackedTextures;

public class Button extends Component{
	
	static StackedTextures tex = Res.MENU_BUTTON;
	
	Runnable onClick;
	
	public Button(String name, float cX, float cY, Runnable onClick){
		super((Window.WIDTH*cX) - 150, (Window.HEIGHT*cY) - 30, 300, 60, name, name, onClick);
	}
	
	public void render(){
//Quad
		tex.file.bind();
		drawTex(tex.texs[0][state ? 1 : 0]);
//Text
		float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(xText, yText, name, 1, 1);
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