package core.menu;

import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import core.Window;

public class Text extends Component{
	
	public Stringer stringer;
	
	public Text(Stringer stringer, float cX, float cY, float width, float height){
		super((Window.WIDTH*cX) - (width/2), (Window.HEIGHT*cY) - (height/2), width, height, "", "", null);
		this.stringer = stringer;
	}
	
	public void render(){
//Quad
		TextureFile.bindNone();
		GL11.glColor4f(1, 1, 1, 0.7f);
		draw();
//Text
		String text = stringer.get();
		
		GL11.glColor4f(0, 0, 0, 1);
		float xText = x + (size.x/2) - (Res.font.getWidth(text)/3);
		float yText = y + (size.y/2) - (Res.font.getHeight()/2);
		Res.font.drawString(xText, yText, text, 1, 1);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public interface Stringer {
		public abstract String get();
	}

	public boolean mousePressed() {
		return false;
	}

	public boolean mouseReleased() {
		return false;
	}
}
