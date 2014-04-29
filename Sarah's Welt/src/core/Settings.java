package core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import world.WorldWindow;

public class Settings {
	
	public static boolean debugView = false;
	public static boolean hitbox = false;
	public static boolean health = false;
	public static boolean agro = true;
	public static boolean shader = false;
	public static boolean time = true;
	public static boolean sound = false;
	
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
