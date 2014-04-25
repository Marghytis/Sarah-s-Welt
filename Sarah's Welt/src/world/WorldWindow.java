package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Game;
import main.Menu;
import main.Settings;
import main.Window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Texture;
import util.Tessellator;
import world.creatures.Creature;
import world.creatures.Sarah;
import world.otherThings.Heart;
import world.structures.Structure;
import world.worldGen.WorldGenerator;

public class WorldWindow {
		/**The scale factor from meters to pixel*/
		public static final int measureScale = 50;
		public static Tessellator tessellator;
		public static Random random = new Random();
		
		public static String worldName;
		
		public static int xSector;
		public static WorldGenerator generator;
//		public static WorldDatabase database;
		public static Sarah sarah;
		
		public static Sector[] sectors = new Sector[3];
		
		public static Lightmap light;
		
		public static float lightLevel = 1;
		
		public static List<Structure> structures = new ArrayList<>();
		public static List<Creature> creatures = new ArrayList<>();
	
		public static void load(String worldName){
			WorldWindow.worldName = worldName;
			tessellator = new Tessellator();
			light = new Lightmap(new Texture(Window.WIDTH, Window.HEIGHT));
//			System.out.println(Window.WIDTH + "  " + Window.HEIGHT);

//			database = new WorldDatabase(worldName);
//			database.loadWorld();
			load();
		}
		
		private static void load(){
			generator = new WorldGenerator();//already creates the first 3 sectors
			
			sectors[1].connectTo(sectors[0]);
			sectors[1].connectTo(sectors[2]);

			takeThingsFrom(sectors[0]);
			takeThingsFrom(sectors[1]);
			takeThingsFrom(sectors[2]);

			for(int i = 0; i < sectors[1].areas.length; i++) sectors[1].areas[i].tessellationNeeded = true;
			
			Point intersection = new Point(10, 0);
			Node playerWorldLink = sectors[1].findGrassPointAt(10, intersection, 100);
			sarah = new Sarah(intersection, playerWorldLink);
		}
		
		public static void refresh(){
			light = new Lightmap(new Texture(Window.WIDTH, Window.HEIGHT));
		}
		
		public static void tick(float dTime){
			sarah.tick(dTime);
			
			int playerX = (int)(sarah.pos.x/Sector.WIDTH) - (sarah.pos.x < 0 ? 1 : 0);
			if(playerX != xSector){
				if(playerX == xSector - 1){
					step(false);
				} else if (playerX == xSector + 1){
					step(true);
				} else {
					//What??
				}
			}
			if(random.nextInt(2000) < 1){
				Point inter = new Point();
				int sec = random.nextInt(3);
				Node link = (sectors[sec].findGrassPointAt((sectors[sec].x + random.nextFloat())*Sector.WIDTH, inter, 100));
				creatures.add(new Heart(inter, link));
			}
			structures.forEach(s -> s.tick(dTime));
			for(int i = 0; i < creatures.size(); i++){
				Creature c = creatures.get(i);
				c.tick(dTime);
				if(c.health <= 0){
					creatures.remove(c);
				}
			}
		}
		
		public static void mouseListening(){
			//add later
			while(Mouse.next()){
				if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0){
					int x = Mouse.getEventX() + (int)sarah.pos.x - (Window.WIDTH/2);
					int y = Mouse.getEventY() + (int)sarah.pos.y - (Window.HEIGHT/2);
					for(Creature c : creatures){
						if((c.pos.x + c.box.x < x && c.pos.x + c.box.x + c.box.width > x) && (c.pos.y + c.box.y < y && c.pos.y + c.box.y + c.box.height > y)){
							c.hitBy(sarah);
						}
					}
					sarah.punch();
				}
			}
		}
		
		public static void keyListening(){
			while(Keyboard.next()){
				if(Game.menu != Menu.DEBUG && Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState()){
					Game.menu = Menu.DEBUG;
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
					Game.menu = Menu.MAIN;
				} else {
					if(Keyboard.getEventKeyState()){
						switch(Keyboard.getEventKey()){
						case Keyboard.KEY_D : sarah.mirrored = false; break;
						case Keyboard.KEY_A : sarah.mirrored = true; break;
						case Keyboard.KEY_W: sarah.jump(); break;
						}
					} else {
						
					}
				}
			}
		}
		
		public static void render(){
			GL11.glLoadIdentity();
			GL11.glTranslatef(- sarah.pos.x + (Window.WIDTH/2.0f), - sarah.pos.y + (Window.HEIGHT/2.0f), 0);
			GL11.glColor4f(1, 1, 1, 1);

			//back
			for(Structure s : structures){
				if(!s.front)s.render();
			}
//			for(Sector sec : sectors){
			{Sector sec = sectors[1];
//				if(sec == null) continue;
				
				for(int mat = 0; mat < Material.values().length; mat++){
						sec.areas[mat].render(Material.values()[mat].texture);
				}
				
			}
			
			//front
			for(Structure s : structures){
				if(s.front)s.render();
			}
			
			creatures.forEach(c -> c.render());

			GL11.glPushMatrix();
			sarah.render();
			GL11.glPopMatrix();

			
			//render health on creatures
			if(Settings.health) creatures.forEach(c -> Window.font.drawString(c.pos.x - (Window.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f));
			
			//health on sarah
			if(Settings.health){
				GL11.glColor3f(1, 0, 0);
				Window.font.drawString(sarah.pos.x - (Window.font.getWidth(sarah.health + "")/3), sarah.pos.y + 60, sarah.health + "", 0.5f, 0.5f);
				GL11.glColor3f(1, 1, 1);
			}
			
			//draw the darkness of the night (or not)
			if(Settings.shader){
				GL11.glLoadIdentity();
				light.draw(Game.window);
				light.bind();
				light.resetDark(Game.window, lightLevel);
				light.release();
			}
		}
				
		public static void step(boolean rightwards){
			//if !load(x)
			if(rightwards){
				//expand rightwards
				//move rim etc.
				xSector++;
				putThingsTo(sectors[0]);
				sectors[0].partiateFrom(sectors[1]);
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				if(xSector > generator.rimR-2){
					sectors[2] = generator.generateRight();
				} else {
					sectors[2] = null;//TODO load
				}
				sectors[1].connectTo(sectors[2]);
				takeThingsFrom(sectors[2]);
//				for(int i = 0; i < sectors[1].areas.length; i++) sectors[1].areas[i].tessellationNeeded = true;
			} else {
				xSector--;
				sectors[2].partiateFrom(sectors[1]);
				putThingsTo(sectors[2]);
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				
				if(xSector < generator.rimL+1){
					sectors[0] = generator.generateLeft();
				} else {
					sectors[0] = null;//database.loadSectorAt();TODO
				}
				if(sectors[0] != null){
					sectors[0].connectTo(sectors[1]);
					takeThingsFrom(sectors[0]);
				}
			}
		}
		
		public static void putThingsTo(Sector s){
			for(int i = 0; i < creatures.size(); i++){
				Creature c = creatures.get(i);
				if(c.pos.x > s.x*Sector.WIDTH && c.pos.x < (s.x + 1)*Sector.WIDTH){
					s.creatures.add(c);
					creatures.remove(i);
					i--;
				}
			}
			for(int i = 0; i < structures.size(); i++){
				Structure c = structures.get(i);
				if(c.pos.x > s.x*Sector.WIDTH && c.pos.x < (s.x + 1)*Sector.WIDTH){
					s.structures.add(c);
					structures.remove(i);
					i--;
				}
			}
		}
		
		public static void takeThingsFrom(Sector s){
			
			structures.addAll(s.structures); s.structures.clear();
			creatures.addAll(s.creatures); s.creatures.clear();
		}
}
