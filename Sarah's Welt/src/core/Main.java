package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import world.Calendar;
import world.World;
import world.WorldView;
import core.menu.Menu;

public class Main {
	
	public static List<WorldO> worlds;
	
	public static void main(String[] args){
		//Size of the visible world
		if(args.length > 0) World.widthHalf = Integer.parseInt(args[0]);
		
		//Create the Window and show splash screen
		if(false){//args.length > 1
			Window.createFullScreen("Sarahs Welt 1.0");
		} else {
			Window.create("Sarahs Welt 1.0", 1500, 900);
		}
		Window.fill(new TextureFile("titelbild", 0, 0).handle);
		Display.update();
		
		//Look in the "worlds" file which worlds there are saved
		loadWorlds();

		//Load all the resources (Textures, Sounds, Texts)
		Res.load();
		
		//Load either the world which was last opened or, if there aren't any worlds, create the "StartWorld"
		String lastWorld = "StartWorld";
		for(WorldO o : worlds){
			if(o.last){
				lastWorld = o.name;
				break;
			}
		}
		World.load(lastWorld);
		
		startGameLoop();
		
		//save the existing worlds back into the "worlds"-file
		saveWorlds();
		Res.unload();
		Display.destroy();
	}
	
	public static void startGameLoop(){
		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			long testTime = System.currentTimeMillis();

			/**
			 * RENDER
			 */
			render();
			
			/**
			 * LISTEN
			 */
			listening();
			
			/**
			 * UPDATE
			 */
			long t = System.currentTimeMillis();
			update(Math.min((int)(t - timeLastWorldTick), 20));
			timeLastWorldTick = t;
			
			
			//Update the Window
			Display.update();
			
			//Cap the frames per second to 60
			try {
				Thread.sleep(17 - (System.currentTimeMillis() - testTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e){}
		}
	}
	
	public static boolean beenden;
	
	public static void render(){
		//reset OpenGL
		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.55f, 0.53f, 0.76f, 1);
		GL11.glColor4f(1, 1, 1, 1);
		
		//render World and Menu
		WorldView.render();
		GL11.glLoadIdentity();
		Menu.render();
	}
	
	public static void update(int delta){
		
		if(!Menu.pauseWorld()){
			WorldView.update(delta);
			Calendar.tick(delta);
		}
	}

	public static void listening(){
		if(Menu.pauseWorld()){
			Menu.keyListening();
		} else {
			WorldView.keyListening();
		}//extra
		if(Menu.pauseWorld()){
			Menu.mouseListening();
		} else {
			WorldView.mouseListening();
		}
	}
	
	public static void loadWorlds(){

		worlds = new ArrayList<>();
		try{
			BufferedReader r = new BufferedReader(new FileReader("worlds/worlds"));
			String line = "";
			while((line = r.readLine()) != null){
				String[] args = line.split(";");
				WorldO world = new WorldO(args[0]); 
				world.last = Boolean.parseBoolean(args[1]);
				worlds.add(world);
			}
			r.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void saveWorlds(){
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter("worlds/worlds"));
			for(WorldO world : worlds){
				w.write(world.name + ";" + world.last + "\n");
			}
			w.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}