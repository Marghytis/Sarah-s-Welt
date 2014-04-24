package resources;

import main.Game;

import org.lwjgl.opengl.GL11;

public class Lightmap extends Framebuffer{
		
	public Lightmap(Texture tex){
		super(tex);
	}
	
	public void resetDark(float howDark){
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(howDark, howDark, howDark, 1);
		Game.window.fill();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}
}
