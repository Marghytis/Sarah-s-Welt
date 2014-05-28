package particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Res;
import resources.TextureFile;
import world.creatures.Unicorn;
import core.Window;
import core.geom.Quad;
import core.geom.Vec;

public class ParticleTester {
	
	public static TextureFile background;
	
	public static void main(String[] args){
		Window.create("Particle", 1000, 700);
		background = new TextureFile("particles/Background");
		
		Lightmap lightmap = new Lightmap(new TextureFile(Window.WIDTH/2, Window.HEIGHT));
		lightmap.resetDark( 0);
		Quad leftWindow = new Quad(0, 0, Window.WIDTH/2, Window.HEIGHT);
		
		FireEffect fire = new FireEffect(Window.WIDTH/4, Window.HEIGHT/2, lightmap);

		RainEffect rain = new RainEffect(new Vec(Window.WIDTH*2/3 + 50, Window.HEIGHT*3/4 + 10), 100, 20);
		Quad cloud = new Quad(Window.WIDTH*2/3, Window.HEIGHT*3/4 - 20, 200, 140);
		
		float t = 0;
		float r = 20;
		float d = 100;
		
		long time = System.currentTimeMillis();
		while(!Display.isCloseRequested()){
			Display.sync(60);
			mouseListening();
//			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			fire.pos.set(Window.WIDTH/4 + (r * (float) Math.cos(t)), Window.HEIGHT/2 + (r * (float) Math.sin(t)));
			t += (float)Math.PI/200;
			rain.pos.x = Window.WIDTH*2/3 + 50 + (d * (float) Math.cos(t));
			cloud.x = rain.pos.x - 50;
			
			lightmap.bind();
			lightmap.resetDark(0.5f);
			lightmap.release();
			Window.fill(background.handle);
			
			long nextTime = System.currentTimeMillis();
			fire.tick((int)(nextTime - time));
			rain.tick((int)(nextTime - time));

			fire.render();
			rain.render();
			
			for(int i = 0; i < swooshs.size(); i++){
				swooshs.get(i).tick((int)(nextTime - time));
				swooshs.get(i).render();
				if(!swooshs.get(i).living()){
					swooshs.remove(i);
					i--;
				}
			}
			
			time = nextTime;
			GL11.glColor4f(0.8f, 0.8f, 0.8f, 1);
			cloud.drawTex(Res.CLOUD.texs[0][0]);
			
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
			lightmap.texture.file.bind();
			leftWindow.drawTex(lightmap.texture);
			TextureFile.bindNone();
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        Display.update();
		}
		Display.destroy();
	}
	
	public static List<ParticleEffect> swooshs = new ArrayList<>();
	
	public static void mouseListening(){
//		swooshs.add(new SWOOSH(new Vec(Mouse.getX(), Mouse.getY())));
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				if(Mouse.getEventButton() == 0){
					swooshs.add(new DeathDust(new Vec(Mouse.getEventX(), Mouse.getEventY())));
				} else {
					swooshs.add(new Magic(Mouse.getEventX(), Mouse.getEventY(), new Unicorn(new Vec(Mouse.getEventX(), Mouse.getEventY()), null)));
				}
			}
		}
	}
}
