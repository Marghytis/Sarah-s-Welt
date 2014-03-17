package main;

import org.lwjgl.opengl.GL11;

public class GL {
	
	public static void color(float r, float g, float b, float a){
		GL11.glColor4f(r, g, b, a);
	}
	
	public static void disableTex(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public static void enableTex(){
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public static void defaultBlending(){
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void drawRect(float x1, float y1, float x2, float y2){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x1, y1);
			GL11.glVertex2f(x2, y1);
			GL11.glVertex2f(x2, y2);
			GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}

	public static void drawQuad(float x1, float y1, float x2, float y2){
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glVertex2f(x1, y2);
	}

	public static void drawQuadTex(float x1, float y1, float x2, float y2, float x1T, float y1T, float x2T, float y2T){
		GL11.glTexCoord2f(x1T, y2T);
		GL11.glVertex2f(x1, y1);
		GL11.glTexCoord2f(x2T, y2T);
		GL11.glVertex2f(x2, y1);
		GL11.glTexCoord2f(x2T, y1T);
		GL11.glVertex2f(x2, y2);
		GL11.glTexCoord2f(x1T, y1T);
		GL11.glVertex2f(x1, y2);
	}
}
