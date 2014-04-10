package main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import world.WorldWindow;

public class Settings {
	
	public static boolean debugView = true;
	
	public void disableFlying(){}
	
	public static void switchDebugView(){
		if(debugView){
			WorldWindow.tessellator.tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_TRUE);
		} else {
			WorldWindow.tessellator.tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_FALSE);
		}
		debugView = !debugView;
	}
}