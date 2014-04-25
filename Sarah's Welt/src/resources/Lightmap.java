package resources;

import main.Window;

import org.lwjgl.opengl.GL11;

public class Lightmap extends Framebuffer{
		
	public Lightmap(Texture tex){
		super(tex);
	}
	
	public void resetDark(Window w, float howDark){
//      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glColor4f(howDark, howDark, howDark, 1);
		w.fill();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}
}
