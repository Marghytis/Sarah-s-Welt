package world.structures;

import main.Settings;
import main.Window;

import org.lwjgl.opengl.GL11;

import resources.Shader;
import util.Animation;
import world.Node;
import world.Point;
import world.WorldWindow;

public class Flower extends Structure {
	
	int type;
	
	public Flower(int type, Point pos, Node worldLink){
		super(Structure.FLOWER[type], new Animation(1, 1), pos, worldLink);
		this.type = type;
	}
	
	public void render(){
		super.render();
		if(Settings.shader){
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			WorldWindow.light.bind();
			switch(type){
			case 0: Shader.BRIGHT.drawLight(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2, (pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 + 20, 1f, 1, 0); break;
			case 1: Shader.BRIGHT.drawLight(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2, (pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 + 20, 1f, 0, 0); break;
			case 2: Shader.BRIGHT.drawLight(pos.x - WorldWindow.sarah.pos.x + Window.WIDTH/2, (pos.y - WorldWindow.sarah.pos.y) + Window.HEIGHT/2 + 20, 1f, 1, 1); break;
			}
			WorldWindow.light.release();
			GL11.glPopMatrix();
		}
	}
	
}
