package world;

import item.Inventory;
import item.Item;
import item.WorldItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import particles.ParticleEffect;
import resources.Lightmap;
import resources.Res;
import resources.TextureFile;
import util.Tessellator;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Cow;
import world.creatures.Creature;
import world.creatures.Gnat;
import world.creatures.Heart;
import world.creatures.Panda;
import world.creatures.Rabbit;
import world.creatures.Sarah;
import world.creatures.Scorpion;
import world.creatures.Snail;
import world.creatures.Trex;
import world.creatures.Unicorn;
import world.worldGen.SurfaceGenerator;
import worldObjects.Bamboo;
import worldObjects.Bush;
import worldObjects.Cactus;
import worldObjects.CandyBush;
import worldObjects.CandyFlower;
import worldObjects.CandyTree;
import worldObjects.Cloud;
import worldObjects.Crack;
import worldObjects.Flower;
import worldObjects.Fossil;
import worldObjects.Grass_tuft;
import worldObjects.PalmTree;
import worldObjects.Tree;
import worldObjects.WorldObject;
import core.Menu;
import core.Menu.View;
import core.Settings;
import core.Window;
import core.geom.Vec;

@SuppressWarnings("unchecked")
public class WorldOld {
	public static final int measureScale = 50;

	public static List<Node>[] contours;

	public static List<WorldObject>[] worldObjects;
	public static List<Creature>[] creatures;
	public static List<WorldItem>[] items = (List<WorldItem>[]) (new ArrayList<?>[Item.list.size()]);
	public static List<ParticleEffect> particleEffects = new ArrayList<>();
	
	public static List<Runnable> thingTasks = new ArrayList<>();
	
	static {
		contours = (ArrayList<Node>[]) new ArrayList<?>[Material.values().length];
		for(int i = 0; i < contours.length; i++){
			contours[i] = new ArrayList<>();
		}
		for(int i = 0; i < items.length; i++){
			items[i] = new ArrayList<>();
		}
		
		int s_id = 0;
		Tree.typeId = s_id++;
		PalmTree.typeId = s_id++;
		CandyTree.typeId = s_id++;
		Bush.typeId = s_id++;
		CandyBush.typeId = s_id++;
		Cactus.typeId = s_id++;
		Flower.typeId = s_id++;
		CandyFlower.typeId = s_id++;
		Bamboo.typeId = s_id++;
		Grass_tuft.typeId = s_id++;
		Cloud.typeId = s_id++;
		Crack.typeId = s_id++;
		Fossil.typeId = s_id++;
		
		worldObjects = (List<WorldObject>[]) new List<?>[s_id];
		for(int i = 0; i < worldObjects.length; i++){
			worldObjects[i] = new ArrayList<>();
		}

		int c_id = 1;//0 is sarah
		Snail.typeId = c_id++;
		Butterfly.typeId = c_id++;
		Heart.typeId = c_id++;
		Rabbit.typeId = c_id++;
		Bird.typeId = c_id++;
		Panda.typeId = c_id++;
		Cow.typeId = c_id++;
		Gnat.typeId = c_id++;
		Unicorn.typeId = c_id++;
		Scorpion.typeId = c_id++;
		Trex.typeId = c_id++;
		
		creatures = (List<Creature>[]) new List<?>[c_id];
		for(int i = 0; i < creatures.length; i++){
			creatures[i] = new ArrayList<>();
		}
	}
	
	public static Sarah sarah;
	
	public static SurfaceGenerator generator;
	public static Random random = new Random();
	public static Tessellator tessellator;
	public static Lightmap light;
	public static String worldName;
	
	
	public static void load(String name, float sarahX){
//		if(existsAlready(name)){
//			loadFromDatabase(name);
//		} else {
		worldName = name;
		tessellator = new Tessellator();
		for(List<WorldObject> s : worldObjects) s.clear();
		for(List<Creature> s : creatures) s.clear();
		for(List<Node> ns : contours) ns.clear();
		generator = new SurfaceGenerator(Window.WIDTH + 400);
		
		generator.gen(sarahX);
		sarah = new Sarah(new Vec(sarahX, 1000), null);
		light = new Lightmap(new TextureFile(Window.WIDTH, Window.HEIGHT));
		Inventory.reset();
//		}
	}
	
	public static void update(int delta){
		
		sarah.update(delta);
		if(sarah.health <= 0){
			View.DEATH.set();
		}
		
		generator.gen(sarah.pos.x);
		
//		if(random.nextInt(2000) == 0){
//			Point inter = new Point();
//			int sec = random.nextInt(3);
//			if(sectors[sec] != null){
//				Node link = (sectors[sec].findGrassPointAt((sectors[sec].x + random.nextFloat())*Sector.WIDTH, inter, 100));
//				Creature.creatures.get(Heart.typeId).add(new Heart(inter, link));
//			}
//		}
		
		updateStructures(delta);
		updateCreatures(delta);
	}
	
	public static void updateStructures(int delta){
		for(List<WorldObject> list : World.structures) for(int i = 0; i < list.size(); i++){
			WorldObject s = list.get(i);
			if(s.pos.x < generator.grassT.end.getPoint().x || s.pos.x > generator.grassT.start.getPoint().x){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			} else {
				s.update(delta);
			}
		}
	}
	
	public static void updateCreatures(int delta){
		
		for(Runnable r : thingTasks){
			r.run();
		}
		thingTasks.clear();
		for(List<Creature> list : World.creatures) for(int i = 0; i < list.size(); i++){
			Creature s = list.get(i);
			s.update(delta);
			if(s.pos.x < generator.grassT.end.getPoint().x || s.pos.x > generator.grassT.start.getPoint().x){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			}
		}
		for(int i = 0; i < particleEffects.size(); i++){
			particleEffects.get(i).tick(delta);
			if(!particleEffects.get(i).living()){
				particleEffects.remove(i);
				i--;
			}
		}
	}
	
	public static void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.3f, 0.7f, 1);
		GL11.glLoadIdentity();
		GL11.glTranslatef(- sarah.pos.x + (Window.WIDTH/2.0f), - sarah.pos.y + (Window.HEIGHT/2.0f), 0);
		GL11.glColor4f(1, 1, 1, 1);

		//back
		WorldObject.renderStructures(false);
		
		WorldItem.renderItems();

		Material[] mats = Material.values();
		for(int i = 0; i < mats.length; i++){
			mats[i].textureFile.bind();
			tessellator.tessellateOneNode(contours[i], Material.GRASS.textureFile.width, Material.GRASS.textureFile.height);
		}
		TextureFile.bindNone();
		
		//front
	
		Creature.renderCreatures();
		particleEffects.forEach((d) -> d.render());
		GL11.glColor4f(1, 1, 1, 1);

		GL11.glPushMatrix();
		sarah.render();
		GL11.glPopMatrix();

		WorldObject.renderStructures(true);
		
		//render health on creatures
		if(Settings.health) for(List<Creature> list : creatures) for(Creature c : list){
			if(!(c instanceof Gnat)) Res.font.drawString(c.pos.x - (Res.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f);
		}
		
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
			light.resetDark(Calendar.lightLevel);
			light.release();
		}
	}
	
	public static void mouseListening(){
		//add later
		events : while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				if(Mouse.getEventButton() == 0){
					boolean clickedInWorld = true;
					if(Menu.view == View.INVENTORY){
						clickedInWorld = !Inventory.mouseClickedAt(Mouse.getEventX(), Mouse.getEventY());
					}
					if(clickedInWorld){
						int x = Mouse.getEventX() + (int)sarah.pos.x - (Window.WIDTH/2);
						int y = Mouse.getEventY() + (int)sarah.pos.y - (Window.HEIGHT/2);
						Inventory.getSelectedItem().use(x, y);
					}
				} else if(Mouse.getEventButton() == 1){
					int x = Mouse.getEventX() + (int)sarah.pos.x - (Window.WIDTH/2);
					int y = Mouse.getEventY() + (int)sarah.pos.y - (Window.HEIGHT/2);
					for(List<Creature> list : creatures) for(int ci = 0; ci < list.size(); ci++){
						Creature c = list.get(ci);
						if(!(c instanceof Gnat) && (c.pos.x + c.animator.tex.box.x < x && c.pos.x + c.animator.tex.box.x + c.animator.tex.box.size.x > x) && (c.pos.y + c.animator.tex.box.y < y && c.pos.y + c.animator.tex.box.y + c.animator.tex.box.size.y > y)){
							if(c.rightClickAction())
							continue events;
						}
					}
					for(List<WorldItem> list : items) for(WorldItem c : list){
						if((c.pos.x + c.item.boxWorld.x < x && c.pos.x + c.item.boxWorld.x + c.item.boxWorld.size.x > x) && (c.pos.y + c.item.boxWorld.y < y && c.pos.y + c.item.boxWorld.y + c.item.boxWorld.size.y > y)){
							if(c.rightClickAction())
							continue events;
						}
					}
					for(List<WorldObject> list : worldObjects) for(WorldObject c : list){
						if((c.pos.x + c.animator.tex.box.x < x && c.pos.x + c.animator.tex.box.x + c.animator.tex.box.size.x > x) && (c.pos.y + c.animator.tex.box.y < y && c.pos.y + c.animator.tex.box.y + c.animator.tex.box.size.y > y)){
							if(c.rightClickAction())
							continue events;
						}
					}
				}
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
					case Keyboard.KEY_E: sarah.dismountCow(); break;
					case Keyboard.KEY_F: Menu.buttonPressed(View.DEBUG.buttons[0]); break;
					case Keyboard.KEY_I:
						if(Menu.view == View.INVENTORY){
							View.WORLD.set();
						} else {
							View.INVENTORY.set();
						} break;
					}
				} else {
					
				}
			}
		}
	}
}
