package world;

import item.Inventory;
import item.Item;
import item.WorldItem;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import particles.ParticleEffect;
import resources.Lightmap;
import resources.Res;
import resources.Shader;
import resources.TextureFile;
import util.Tessellator;
import world.BasePoint.ZoneType;
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
import world.worldObjects.Bamboo;
import world.worldObjects.Bush;
import world.worldObjects.Cactus;
import world.worldObjects.CandyBush;
import world.worldObjects.CandyFlower;
import world.worldObjects.CandyTree;
import world.worldObjects.Cloud;
import world.worldObjects.Crack;
import world.worldObjects.Flower;
import world.worldObjects.Fossil;
import world.worldObjects.Grass_tuft;
import world.worldObjects.PalmTree;
import world.worldObjects.Tree;
import world.worldObjects.WorldObject;
import core.Menu;
import core.Settings;
import core.Window;
import core.Menu.View;
import core.geom.Vec;

public class WorldView {
	//world information
	public static float measureScale = 50;
	public static String name;
	public static WorldDatabase database;
	//world generation
	static BasePoint rightGenerator, leftGenerator;
	static float widthHalf = 1000;
	static float rimR;
	static float rimL;
	static List<Zone> zones;
	//things
	public static Sarah sarah;
	public static List<Node>[] contours;
	public static List<WorldObject>[] worldObjects;
	public static List<Creature>[] creatures;
	public static List<WorldItem>[] items;
	//tasks
	public static List<Runnable> thingTasks = new ArrayList<>();
	//rendering
	static Tessellator tessellator;
	public static List<ParticleEffect> particleEffects;
	public static Lightmap light;
	
	@SuppressWarnings("unchecked")
	public static void reset(String worldName){
			//setup rendering
		tessellator = new Tessellator();
		light = new Lightmap(new TextureFile(Window.WIDTH, Window.HEIGHT));
			//setup things
		sarah = new Sarah(new Vec(0, 5), null);
		Inventory.reset();
			//items
		items = (List<WorldItem>[]) (new ArrayList<?>[Item.list.size()]);
		for(int i = 0; i < items.length; i++){
			items[i] = new ArrayList<>();
		}
			//world.worldObjects
		int o_id = 0;
		Tree.typeId = o_id++;
		PalmTree.typeId = o_id++;
		CandyTree.typeId = o_id++;
		Bush.typeId = o_id++;
		CandyBush.typeId = o_id++;
		Cactus.typeId = o_id++;
		Flower.typeId = o_id++;
		CandyFlower.typeId = o_id++;
		Bamboo.typeId = o_id++;
		Grass_tuft.typeId = o_id++;
		Cloud.typeId = o_id++;
		Crack.typeId = o_id++;
		Fossil.typeId = o_id++;
		
		worldObjects = (List<WorldObject>[]) new List<?>[o_id];
		for(int i = 0; i < worldObjects.length; i++){
			worldObjects[i] = new ArrayList<>();
		}
			//creatures
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
			//particle effects
		particleEffects = new ArrayList<>();
		
		//setup world and generators
		contours = (ArrayList<Node>[]) new ArrayList<?>[Material.values().length];
		for(int i = 0; i < contours.length; i++) contours[i] = new ArrayList<>();
		zones = new ArrayList<>();
		rightGenerator = new BasePoint(true, new Vec(0, 0));
		leftGenerator = rightGenerator.setupLayers();
		database = new WorldDatabase(worldName);
	}
	
	public static void update(int delta){
		sarah.update(delta);
		if(sarah.health <= 0){
			View.DEATH.set();
		}
		updateContours();
		updateObjects(delta);
		executeTasks();
		updateCreatures(delta);
		updateItems();
		updateEffects(delta);
	}
	
	public static void updateContours(){
		rimR = sarah.pos.x + widthHalf;
//		database.tryToLoadUpTo(rimR);
		while(rightGenerator.pos.x < rimR){
			rightGenerator.shift();
		}
		rimL = sarah.pos.x - widthHalf;
		//TODO add remove and load code later
		while(leftGenerator.pos.x > rimL){
			leftGenerator.shift();
		}
		rimR += 21;
		rimL -= 21;
		
		//remove nodes
		for(int m = 0; m < contours.length; m++){
			List<Node> list = contours[m];
			List<Node> save = new ArrayList<>();
			contours : for(int node = 0; node  < list.size(); node ++){
			Node n = list.get(node);
			//replace the linking Node if its outside the view
			if(n.getPoint().x < rimL){
				Node n1 = n;
				do { n1 = n1.getNext(); } while(n1.getPoint().x < rimL && n1 != n);
					if(n1 == n){//the whole contour lies outside of the WorldView
						//TODO save the whole contour
						list.remove(node);
						node--;
						continue contours;
					} else {
						list.set(node, n1);
						n = n1;
					}
				}
				if(n.getPoint().x > rimR){
					Node n1 = n;
					do { n1 = n1.getNext(); } while(n1.getPoint().x > rimR && n1 != n);
					if(n1 == n){//the whole contour lies outside of the WorldView
						//TODO save the whole contour
						list.remove(node);
						node--;
						continue contours;
					} else {
						list.set(node, n1);
						n = n1;
					}
				}
				Node n2 = n;
				do {
					if(n2.getNext().getPoint().x < rimL){
						Node end = n2;
						do {
							save.add(n2);
							n2 = n2.getNext();
						} while(n2.getPoint().x < rimL && n2 != end);
						if(n2 != end){
							end.connect(n2);
						}
					}
					if(n2.getNext().getPoint().x > rimR){
						Node end = n2;
						do {
							save.add(n2);
							n2 = n2.getNext();
						} while(n2.getPoint().x > rimR && n2 != end);
						if(n2 != end){
							end.connect(n2);
						}
					}
					n2 = n2.getNext();
				} while(n2 != n);
			}
//			database.save(save.toArray(new Node[save.size()]), Material.values()[m].name());
		}
	}
	
	public static void updateObjects(int delta){
		for(List<WorldObject> list : worldObjects) for(int i = 0; i < list.size(); i++){
			WorldObject s = list.get(i);
			if(s.pos.x < rimL || s.pos.x > rimR){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			} else {
				s.update(delta);
			}
		}
	}
	
	public static void updateCreatures(int delta){
		thingTasks.clear();
		for(List<Creature> list : creatures) for(int i = 0; i < list.size(); i++){
			Creature s = list.get(i);
			s.update(delta);
			if(s.pos.x < rimL || s.pos.x > rimR){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			}
		}
	}
	
	public static void updateItems(){
		for(List<WorldItem> list : items) for(int i = 0; i < list.size(); i++){
			WorldItem it = list.get(i);
			if(it.pos.x < rimL || it.pos.x > rimR){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			}
		}
	}
	
	public static void updateEffects(int delta){
		for(int i = 0; i < particleEffects.size(); i++){
			particleEffects.get(i).tick(delta);
			if(!particleEffects.get(i).living()){
				particleEffects.remove(i);
				i--;
			}
		}
	}
	
	public static void executeTasks(){
		for(Runnable r : thingTasks){
			r.run();
		}
	}
	
	public static void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.3f, 0.7f, 1);
		GL11.glLoadIdentity();
		GL11.glTranslatef(- sarah.pos.x + (Window.WIDTH/2), - sarah.pos.y + (Window.HEIGHT/2), 0);
		GL11.glColor4f(1, 1, 1, 1);

		//back
		renderStructures(false);
		
		renderItems();

		Material[] mats = Material.values();
		for(int i = 0; i < mats.length; i++){
			mats[i].textureFile.bind();
			tessellator.tessellateOneNode(contours[i], Material.GRASS.textureFile.width, Material.GRASS.textureFile.height);
		}
		TextureFile.bindNone();
		
		//front
	
		renderCreatures();
		renderEffects();
		GL11.glColor4f(1, 1, 1, 1);

		GL11.glPushMatrix();
		sarah.render();
		GL11.glPopMatrix();

		renderStructures(true);
		
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
	
	public static void renderCreatures(){
		for(List<Creature> list : creatures){
			if(list.size() > 0) {
				if(list.get(0) instanceof Gnat){
					TextureFile.bindNone();
					GL11.glColor3f(0.2f, 0.2f, 0.2f);
					GL11.glBegin(GL11.GL_POINTS);
						list.forEach((c) -> ((Gnat)c).render());
					GL11.glEnd();
					GL11.glColor3f(1, 1, 1);
				} else {
					list.get(0).animator.tex.file.bind();
					list.forEach((c) -> c.render());
				}
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<Creature> list : creatures){
				list.forEach((c) -> {
					if(!(c instanceof Gnat)){
						GL11.glPushMatrix();
						GL11.glTranslatef(c.pos.x, c.pos.y, 0);
						c.animator.tex.texs[0][0].box.outline();
						GL11.glPopMatrix();
					}
				});
			}
		}
	}

	public static void renderStructures(boolean front){
		for(List<WorldObject> list : worldObjects){
			if(list.size() > 0){
				list.get(0).animator.tex.file.bind();
				list.forEach((c) -> {if(front == c.front){c.render();}});
				
				if(Settings.shader && (list.get(0) instanceof Flower || list.get(0) instanceof CandyFlower)){
					GL11.glPushMatrix();
					GL11.glLoadIdentity();
					light.bind();
						Shader.Test.bind();
							list.forEach((c) -> {
								GL11.glPushMatrix();
								if(front == c.front){
									((Flower)c).renderLight();
								}
								GL11.glPopMatrix();
							});
						Shader.Test.release();
					light.release();
					GL11.glPopMatrix();
					GL11.glColor4f(1, 1, 1, 1);
				}
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<WorldObject> list : worldObjects){
				list.forEach((c) -> {
					GL11.glPushMatrix();
					GL11.glTranslatef(c.pos.x, c.pos.y, 0);
					c.animator.tex.box.outline();
					GL11.glPopMatrix();
				});
			}
		}
	}

	public static void renderItems() {
		for(List<WorldItem> list : items){
			if(list.size() > 0) {
				list.get(0).animator.texture.file.bind();
				list.forEach((c) -> c.render());
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<WorldItem> list : items){
				list.forEach((c) -> {
					GL11.glPushMatrix();
					GL11.glTranslatef(c.pos.x, c.pos.y, 0);
					c.animator.texture.box.outline();
					GL11.glPopMatrix();
				});
			}
		}
	}
	
	public static void renderEffects(){
		particleEffects.forEach((d) -> d.render());
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
	public static ZoneType getZoneType(float x){
		for(Zone z : zones){
			if(z.start < x && z.end > x){
				return z.type;
			}
		}
		return null;
	}
	public static class Zone {
		public ZoneType type;
		public float start;
		public float end;
		
		public Zone(ZoneType type){
			this.type = type;
		}
	}
}