package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Res;
import resources.Texture;
import util.Tessellator;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Creature;
import world.creatures.Rabbit;
import world.creatures.Sarah;
import world.creatures.Snail;
import world.otherThings.Heart;
import world.structures.Bamboo;
import world.structures.Bush;
import world.structures.Cloud;
import world.structures.Crack;
import world.structures.Flower;
import world.structures.Grass_tuft;
import world.structures.Structure;
import world.structures.Tree;
import core.Menu;
import core.Menu.View;
import core.Settings;
import core.Window;
import core.geom.Vec;

@SuppressWarnings("unchecked")
public class World {
	public static final int measureScale = 50;

	public static List<Node>[] contours;

	public static List<ArrayList<Structure>> structures = new ArrayList<>();
	public static List<ArrayList<Creature>> creatures = new ArrayList<>();
	
	static {
		contours = (ArrayList<Node>[]) new ArrayList<?>[Material.values().length];
		for(int i = 0; i < contours.length; i++){
			contours[i] = new ArrayList<>();
		}
		
		int s_id = 0;
		Tree.typeId = s_id++; structures.add(new ArrayList<>());
		Bush.typeId = s_id++; structures.add(new ArrayList<>());
		Flower.typeId = s_id++; structures.add(new ArrayList<>());
		Bamboo.typeId = s_id++; structures.add(new ArrayList<>());
		Grass_tuft.typeId = s_id++; structures.add(new ArrayList<>());
		Cloud.typeId = s_id++; structures.add(new ArrayList<>());
		Crack.typeId = s_id++; structures.add(new ArrayList<>());

		int c_id = 0;
		Snail.typeId = c_id++; creatures.add(new ArrayList<>());
		Butterfly.typeId = c_id++; creatures.add(new ArrayList<>());
		Heart.typeId = c_id++; creatures.add(new ArrayList<>());
		Rabbit.typeId = c_id++; creatures.add(new ArrayList<>());
		Bird.typeId = c_id++; creatures.add(new ArrayList<>());
	}
	
	public static Sarah sarah;
	
	public static SurfaceGenerator generator;
	public static Random random = new Random();
	public static Tessellator tessellator;
	public static Lightmap light;
	public static String worldName;
	
	public static void load(String name){
//		if(existsAlready(name)){
//			loadFromDatabase(name);
//		} else {
		worldName = name;
		tessellator = new Tessellator();
		generator = new SurfaceGenerator(Window.WIDTH);
		
		generator.gen(10);
		sarah = new Sarah(new Vec(10, 1000), null);
		light = new Lightmap(new Texture(Window.WIDTH, Window.HEIGHT));
//		}
	}
	
	public static void update(int delta){
		
		sarah.update(delta);
		
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
		for(List<Structure> list : World.structures) for(int i = 0; i < list.size(); i++){
			Structure s = list.get(i);
			if(s.pos.x < sarah.pos.x - generator.WIDTH/4 || s.pos.x > sarah.pos.x + generator.WIDTH/4){
				list.remove(i);//TODO SAVE IT!!!!
				i--;
			} else {
				s.update(delta);
			}
		}
	}
	
	public static void updateCreatures(int delta){
		for(List<Creature> list : World.creatures) for(int i = 0; i < list.size(); i++){
			System.out.println(i);
			Creature s = list.get(i);
			s.update(delta);
			if(s.pos.x < sarah.pos.x - generator.WIDTH/4 || s.pos.x > sarah.pos.x + generator.WIDTH/4){
				list.remove(i);//TODO SAVE IT!!!!
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
		Structure.renderStructures(false);

		Material[] mats = Material.values();
		for(int i = 0; i < mats.length; i++){
			mats[i].texture.bind();
			tessellator.tessellateOneNode(contours[i], Material.GRASS.texture.width, Material.GRASS.texture.height);
		}
		Texture.bindNone();
		
		//front
		Structure.renderStructures(true);
	
		Creature.renderCreatures();

		GL11.glPushMatrix();
		sarah.render();
		GL11.glPopMatrix();

		
		//render health on creatures
		if(Settings.health) for(List<Creature> list : creatures) for(Creature c : list){
			Res.font.drawString(c.pos.x - (Res.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f);
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
}
