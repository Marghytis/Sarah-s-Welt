package resources;

import org.lwjgl.opengl.GL11;

import core.Window;

public class Lightmap extends Framebuffer{
		
	public Lightmap(TextureFile tex){
		super(tex);
	}
	
	public void resetDark(float howDark){
//      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glColor4f(howDark, howDark, howDark, 1);
		Window.fill(0);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}
}
