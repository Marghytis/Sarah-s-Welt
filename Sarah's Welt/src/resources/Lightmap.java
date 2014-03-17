package resources;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import main.Game;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class Lightmap extends Framebuffer{
		
	public Lightmap(Texture tex){
		super(tex);
	}
	
	public void resetDark(){
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 1f);
		Game.window.fill();
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void drawLight(float x, float y, float strength, Shader shader){
		ARBShaderObjects.glUseProgramObjectARB(shader.handle);
			ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(shader.handle, "lightLocation"), x, y);
			ARBShaderObjects.glUniform1fARB(glGetUniformLocationARB(shader.handle, "lightStrength"), strength);
			Game.window.fill();
		glUseProgramObjectARB(0);
	}
}
