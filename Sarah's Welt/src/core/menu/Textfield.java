package core.menu;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import world.World;
import core.Window;

public class Textfield extends Component{

	
	public Textfield(String name, float x, float y, float width, float height, Runnable onEnter){
		super(x, y, width, height, name, World.name, onEnter);
	}
	
	public void keyListening(){
		char c = Keyboard.getEventCharacter();
		if(Character.isAlphabetic(c)){
			text += c;
			if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
				action.run();
			}
		} else if(Keyboard.getEventKey() == Keyboard.KEY_BACK){
			if(text.length() > 0) text = text.substring(0, text.length()-1);
		} else if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
			action.run();
		}
	}
	
	@Override
	public void render(){
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

	public boolean mousePressed() {return false;}

	public boolean mouseReleased() {
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			state = true;
			return true;
		}
		return false;
	}
}
