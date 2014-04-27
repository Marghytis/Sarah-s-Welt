package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Res;
import resources.Texture;
import util.Tessellator;
import world.creatures.Creature;
import world.creatures.Sarah;
import world.otherThings.Heart;
import world.structures.Structure;
import world.worldGen.WorldGenerator;
import core.Menu;
import core.Menu.View;
import core.Settings;
import core.Window;

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
		
		public static float lightLevel = 0.1f;
		
		public static List<Structure> structures = new ArrayList<>();
		public static List<Creature> creatures = new ArrayList<>();
		
		public static int[] sky = {0, 0, 100};
	
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
			WorldLoader.load(2, true);
			WorldLoader.load(-2, false);
			
			sectors[1].connectTo(sectors[0]);
			sectors[1].connectTo(sectors[2]);

			takeThingsFrom(sectors[0]);
			takeThingsFrom(sectors[1]);
			takeThingsFrom(sectors[2]);

			for(int i = 0; i < sectors[1].areas.length; i++){
				sectors[1].areas[i].tess(Material.values()[i].texture);
			}
			
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
				boolean dir = playerX == xSector + 1;
				xSector += dir ? 1 : -1;
				if(WorldLoader.ready(dir)){
					step(dir);
				} else {
					System.out.println("ERROR: WorldLoader to the " + (dir ? "right" : "left") + " was not ready to step!");
				}
				WorldLoader.load(xSector + (dir ? 2 : -2), dir);
			}
			if(random.nextInt(2000) < 1){
				Point inter = new Point();
				int sec = random.nextInt(3);
				if(sectors[sec] != null){
					Node link = (sectors[sec].findGrassPointAt((sectors[sec].x + random.nextFloat())*Sector.WIDTH, inter, 100));
					creatures.add(new Heart(inter, link));
				}
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
						if((c.pos.x + c.tex.box.x < x && c.pos.x + c.tex.box.x + c.tex.box.width > x) && (c.pos.y + c.tex.box.y < y && c.pos.y + c.tex.box.y + c.tex.box.height > y)){
							c.hitBy(sarah);
						}
					}
					sarah.punch();
				}
			}
		}
		
		public static void keyListening(){
			while(Keyboard.next()){
				if(Menu.view != View.DEBUG && Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState()){
					Menu.view = View.DEBUG;
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
					Menu.view = View.MAIN;
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
		
		static int color = 0;
		static boolean turnUp = true;
		static int colorCounter;
		
		public static void render(){
			if(turnUp){
				sky[color] += 1;
			} else {
				sky[(color + 2) % 3] -= 1;
			}
			colorCounter += 1;
			if(colorCounter >= 1000){
				if(!turnUp){
					color = (color + 1) % 3;
				}
				turnUp = !turnUp;
				colorCounter = 0;
			}
			GL11.glClearColor(sky[0]/10000.0f + 0.4f, sky[1]/10000.0f + 0.4f, sky[2]/10000.0f + 0.4f, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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
			if(Settings.health) creatures.forEach(c -> Res.font.drawString(c.pos.x - (Res.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f));
			
			//health on sarah
			if(Settings.health){
				GL11.glColor3f(1, 0, 0);
				Res.font.drawString(sarah.pos.x - (Res.font.getWidth(sarah.health + "")/3), sarah.pos.y + 60, sarah.health + "", 0.5f, 0.5f);
				GL11.glColor3f(1, 1, 1);
			}

			//draw the darkness of the night (or not)
			if(Settings.shader){
				GL11.glLoadIdentity();
				light.draw();
				light.bind();
				light.resetDark(lightLevel);
				light.release();
			}
		}
		
		public static void step(boolean rightwards){
			//if !load(x)
			if(rightwards){
				//expand rightwards
				//move rim etc.
				putThingsTo(sectors[0]);
				sectors[0].partiateFrom(sectors[1]);
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				sectors[2] = WorldLoader.right.sector;
				sectors[1].connectTo(sectors[2]);
				takeThingsFrom(sectors[2]);
//				for(int i = 0; i < sectors[1].areas.length; i++) sectors[1].areas[i].tessellationNeeded = true;
			} else {
				putThingsTo(sectors[2]);
				sectors[2].partiateFrom(sectors[1]);
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				sectors[0] = WorldLoader.left.sector;
				if(sectors[0] != null){
					sectors[0].connectTo(sectors[1]);
					takeThingsFrom(sectors[0]);
				}
			}
		}
		
		public static void putThingsTo(Sector s){
			for(int i = 0; i < creatures.size(); i++){
				Creature c = creatures.get(i);
				if(c.pos.x >= s.x*Sector.WIDTH && c.pos.x <= (s.x + 1)*Sector.WIDTH){
					s.creatures.add(c);
					creatures.remove(i);
					i--;
				}
			}
			for(int i = 0; i < structures.size(); i++){
				Structure c = structures.get(i);
				if(c.pos.x >= s.x*Sector.WIDTH && c.pos.x <= (s.x + 1)*Sector.WIDTH){
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
