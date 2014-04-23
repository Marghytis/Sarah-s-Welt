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
import util.Cycle;
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
			
			sectors[0].switchConnection(sectors[1], true);
			sectors[1].switchConnection(sectors[2], true);
			
			Point intersection = new Point();
			Node playerWorldLink = sectors[1].findGrassPointAt(500, intersection, 100);
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
			for(Sector sec : sectors){
				if(sec != null) {
					//spawn hearts
					if(random.nextInt(2000) < 1){
						Point inter = new Point();
						Node link = sec.findGrassPointAt((sec.x + random.nextFloat())*Sector.WIDTH, inter, 100);
						sec.creatures.add(new Heart(inter, link));
					}
					
					for(Structure s : sec.structures){
						s.tick(dTime);
					}
					List<Creature> deadCreas = new ArrayList<>();
					for(Creature c : sec.creatures){
						c.tick(dTime);
						if(c.health <= 0){
							deadCreas.add(c);
						}
					}
					deadCreas.forEach((Creature c) -> sec.creatures.remove(c));
				}
			}
		}
		
		public static void mouseListening(){
			//add later
			while(Mouse.next()){
				if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0){
					int x = Mouse.getEventX() + (int)sarah.pos.x - (Window.WIDTH/2);
					int y = Mouse.getEventY() + (int)sarah.pos.y - (Window.HEIGHT/2);
					for(Sector sec : sectors){
						for(Creature c : sec.creatures){
							if((c.pos.x + c.box.x < x && c.pos.x + c.box.x + c.box.width > x) && (c.pos.y + c.box.y < y && c.pos.y + c.box.y + c.box.height > y)){
								c.hitBy(sarah);
							}
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
			for(Sector sec : sectors){
				if(sec != null) {
					
					for(Structure s : sec.structures){
						if(!s.front)s.render();
					}
					
					for(int mat = 0; mat < Material.values().length; mat++){
						
						Texture tex = Material.values()[mat].texture;
						tex.bind();
						if(Settings.debugView){
							tessellator.tessellateOneNode(sec.areas[mat].cycles, tex.width, tex.height);
						} else {
							for(Node n : sec.areas[mat].cycles){
								GL11.glBegin(GL11.GL_LINE_LOOP);
								Cycle.iterate(n, (Node h) -> GL11.glVertex2f(h.p.x, h.p.y));
								GL11.glEnd();
							}
						}
						tex.release();
					}
					for(Creature c : sec.creatures){
						if(!c.front)c.render();
					}
				}
			}

			GL11.glPushMatrix();
			sarah.render();
			GL11.glPopMatrix();
			
			//front
			for(Sector sec : sectors){
				for(Structure s : sec.structures){
					if(s.front)s.render();
				}
				for(Creature c : sec.creatures){
					if(c.front)c.render();
					if(Settings.health)Window.font.drawString(c.pos.x - (Window.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f);
				}
			}
			if(Settings.health){
				GL11.glColor3f(1, 0, 0);
				Window.font.drawString(sarah.pos.x - (Window.font.getWidth(sarah.health + "")/3), sarah.pos.y + 60, sarah.health + "", 0.5f, 0.5f);
				GL11.glColor3f(1, 1, 1);
			}
			
			GL11.glLoadIdentity();
			light.draw();
			light.bind();
			light.resetDark(lightLevel);
			light.release();
			
		}
				
		public static void step(boolean rightwards){
			//if !load(x)
			if(rightwards){
				//expand rightwards
				//move rim etc.
				xSector++;
				sectors[0].switchConnection(sectors[1], true);
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				if(xSector > generator.rimR-2){
					sectors[2] = generator.generateRight();//TODO or load
//				}
				sectors[1].switchConnection(sectors[2], true);
				}
			} else {
				xSector--;
				sectors[1].switchConnection(sectors[2], true);
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				if(xSector < generator.rimL+1){
				sectors[0] = generator.generateLeft();//TODO or load
//				}
				sectors[0].switchConnection(sectors[1], true);
				} else {
//					database.loadSectorAt();TODO
				}
			}
		}
}
