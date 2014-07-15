package world;

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
import world.Zone.ZoneType;
import world.creatures.Creature;
import world.creatures.Creature.CreatureType;
import world.creatures.Gnat;
import world.creatures.Unicorn;
import world.worldObjects.CandyFlower;
import world.worldObjects.Flower;
import world.worldObjects.WorldObject;
import core.Settings;
import core.Window;
import core.menu.Menu;
import core.menu.Menu.View;

public class WorldView {
	//world generation
	public static float rimR;
	public static float rimL;
	//tasks
	public static List<Runnable> thingTasks = new ArrayList<>();
	//rendering
	static Tessellator tessellator;
	public static List<ParticleEffect> particleEffects;
	public static Lightmap light;
	
	public static List<Node>[] contours;
	
	@SuppressWarnings("unchecked")
	public static void reset(){
			//setup rendering
		tessellator = new Tessellator();
		light = new Lightmap(new TextureFile(Window.WIDTH, Window.HEIGHT));
			//particle effects
		particleEffects = new ArrayList<>();
		
		contours = (List<Node>[]) new ArrayList<?>[World.nodes.length];

		World.updateContours();
		setContourNodes();
	}
	
	public static void update(float delta){
		World.sarah.update(delta);
		if(World.sarah.health <= 0){
			View.DEATH.set();
		}
		World.updateContours();
		setContourNodes();
		updateObjects(delta);
		executeTasks();
		updateCreatures(delta);
		updateItems(delta);
		updateEffects(delta);
	}
	
	public static void setContourNodes(){

		for(int mat = 0; mat < World.nodes.length; mat++){
			List<Node> contourStarts = new ArrayList<>();
			boolean[] used = new boolean[World.nodes[mat].size()];
			for(int i = 0; i < World.nodes[mat].size(); i++){
				if(!used[i]){
					Node start = World.nodes[mat].get(i);
					contourStarts.add(start);
					Node n = start;
					do {
						used[World.nodes[mat].indexOf(n)] = true;
						n.inside = n.p.x >= rimL && n.p.x <= rimR;
						n = n.next;
					} while(n != start);
				}
			}
			contours[mat] = new ArrayList<>();
			contours[mat].addAll(contourStarts);
		}
	}
	
	public static void updateObjects(float delta){
		for(List<WorldObject> list : World.worldObjects) for(WorldObject o : list){
			if(o.pos.x >= rimL && o.pos.x <= rimR){
				o.update(delta);
			}
		}
	}
	
	public static void updateCreatures(float delta){
		thingTasks.clear();
		for(List<Creature> list : World.creatures) for(Creature c : list){
			if(c.pos.x >= rimL && c.pos.x <= rimR){
				c.update(delta);
			}
		}
	}
	
	public static void updateItems(float delta){
        for(List<WorldItem> list : World.items) for(WorldItem item : list){
			if(item.pos.x >= rimL && item.pos.x <= rimR){
				item.update(delta);
			}
		}
	}
	
	public static void updateEffects(float delta){
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
		GL11.glTranslatef(- World.sarah.pos.x + (Window.WIDTH/2), - World.sarah.pos.y + (Window.HEIGHT/2), 0);
		GL11.glColor4f(1, 1, 1, 1);

		//back
		renderStructures(false);
		
		renderItems();

		Material[] mats = Material.values();
		for(int i = 0; i < mats.length; i++){
			mats[i].textureFile.bind();
			tessellator.tessellateSingleNodes(contours[i], mats[i].textureFile.width, mats[i].textureFile.height);
		}
		TextureFile.bindNone();
		
		//front
	
		renderCreatures();
		renderEffects();
		GL11.glColor4f(1, 1, 1, 1);

		GL11.glPushMatrix();
		World.sarah.render();
		GL11.glPopMatrix();

		renderStructures(true);
		
		//render health on creatures
		if(Settings.health) for(List<Creature> list : World.creatures) for(Creature c : list){
			GL11.glColor3f(0.9f, 0.1f, 0);
			if(!(c instanceof Gnat)) Res.font.drawString(c.pos.x - (Res.font.getWidth(c.health + "")/3), c.pos.y + 30, c.health + "", 0.5f, 0.5f);
			//health on sarah
			Res.font.drawString(World.sarah.pos.x - (Res.font.getWidth(World.sarah.health + "")/3), World.sarah.pos.y + 60, World.sarah.health + "", 0.5f, 0.5f);
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
		for(List<Creature> list : World.creatures){
			if(list.size() > 0) {
				if(list.get(0) instanceof Gnat){
					TextureFile.bindNone();
					GL11.glColor3f(0.2f, 0.2f, 0.2f);
					GL11.glBegin(GL11.GL_POINTS);
						for(Creature c : list){
							if(c.pos.x >= rimL && c.pos.x <= rimR){
								((Gnat)c).render();
							}
						}
					GL11.glEnd();
					GL11.glColor3f(1, 1, 1);
				} else {
					list.get(0).animator.tex.file.bind();
					for(Creature c : list){
						if(c.pos.x >= rimL && c.pos.x <= rimR){
							c.render();
						}
					}
					if(list.get(0).type == CreatureType.UNICORN){
						Res.UNICORN_HAIR.file.bind();
						for(Creature c : list){
							if(c.pos.x >= rimL && c.pos.x <= rimR){
								((Unicorn)c).renderHair();
							}
						}
					}
				}
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<Creature> list : World.creatures){
				list.forEach((c) -> {
					if(!(c instanceof Gnat) && c.pos.x >= rimL && c.pos.x <= rimR){
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
		for(List<WorldObject> list : World.worldObjects){
			if(list.size() > 0){
				list.get(0).animator.tex.file.bind();
				list.forEach((c) -> {if(front == c.front && c.pos.x >= rimL && c.pos.x <= rimR){c.render();}});
				
				if(Settings.shader && (list.get(0) instanceof Flower || list.get(0) instanceof CandyFlower)){
					GL11.glPushMatrix();
					GL11.glLoadIdentity();
					light.bind();
						Shader.Test.bind();
							list.forEach((c) -> {
								GL11.glPushMatrix();
								if(front == c.front){
									if(c instanceof Flower){
										((Flower)c).renderLight();
									} else {
										((CandyFlower)c).renderLight();
									}
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
			for(List<WorldObject> list : World.worldObjects){
				list.forEach((c) -> {
					if(c.pos.x >= rimL && c.pos.x <= rimR){
						GL11.glPushMatrix();
						GL11.glTranslatef(c.pos.x, c.pos.y, 0);
						c.animator.tex.box.outline();
						GL11.glPopMatrix();
					}
				});
			}
		}
	}

	public static void renderItems() {
		for(List<WorldItem> list : World.items){
			if(list.size() > 0) {
				list.get(0).animator.texture.file.bind();
				list.forEach((c) -> {if(c.pos.x >= rimL && c.pos.x <= rimR) c.render();});
			}
		}
		TextureFile.bindNone();
		if(Settings.hitbox){
			for(List<WorldItem> list : World.items){
				list.forEach((c) -> {
					if(c.pos.x >= rimL && c.pos.x <= rimR){
						GL11.glPushMatrix();
						GL11.glTranslatef(c.pos.x, c.pos.y, 0);
						c.animator.texture.box.outline();
						GL11.glPopMatrix();
					}
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
							boolean clickedInWorld = !Menu.mousePressed();
							if(clickedInWorld){
								int x = Mouse.getEventX() + (int)World.sarah.pos.x - (Window.WIDTH/2);
								int y = Mouse.getEventY() + (int)World.sarah.pos.y - (Window.HEIGHT/2);
								World.sarah.inventory.getSelectedItem().use(x, y);
							}
						} else if(Mouse.getEventButton() == 1){
							int x = Mouse.getEventX() + (int)World.sarah.pos.x - (Window.WIDTH/2);
							int y = Mouse.getEventY() + (int)World.sarah.pos.y - (Window.HEIGHT/2);
							for(List<Creature> list : World.creatures) for(int ci = 0; ci < list.size(); ci++){
								Creature c = list.get(ci);
								if(!(c instanceof Gnat) && (c.pos.x + c.animator.tex.box.x < x && c.pos.x + c.animator.tex.box.x + c.animator.tex.box.size.x > x) && (c.pos.y + c.animator.tex.box.y < y && c.pos.y + c.animator.tex.box.y + c.animator.tex.box.size.y > y)){
									if(c.rightClickAction())
									continue events;
								}
							}
							for(List<WorldItem> list : World.items) for(WorldItem c : list){
								if((c.pos.x + c.item.boxWorld.x < x && c.pos.x + c.item.boxWorld.x + c.item.boxWorld.size.x > x) && (c.pos.y + c.item.boxWorld.y < y && c.pos.y + c.item.boxWorld.y + c.item.boxWorld.size.y > y)){
									if(c.rightClickAction())
									continue events;
								}
							}
							for(List<WorldObject> list : World.worldObjects) for(WorldObject c : list){
								if((c.pos.x + c.animator.tex.box.x < x && c.pos.x + c.animator.tex.box.x + c.animator.tex.box.size.x > x) && (c.pos.y + c.animator.tex.box.y < y && c.pos.y + c.animator.tex.box.y + c.animator.tex.box.size.y > y)){
									if(c.rightClickAction())
									continue events;
								}
							}
						}
					} else {
						if(Mouse.getEventButton() == 0){
							boolean clickedInWorld = !Menu.mouseReleased();
						}
					}
				}
	}
	
	public static void keyListening(){
		while(Keyboard.next()){
			if(Menu.view != View.DEBUG && Keyboard.getEventKey() == Keyboard.KEY_F3 && Keyboard.getEventKeyState()){
				Menu.view = View.DEBUG;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
				if(Menu.view.pauseWorld || Menu.view == View.WORLD){
					View.MAIN.set();
				} else {
					View.WORLD.set();
				}
				World.save();
			} else {
				if(Keyboard.getEventKeyState()){
					switch(Keyboard.getEventKey()){
					case Keyboard.KEY_D : World.sarah.mirrored = false; break;
					case Keyboard.KEY_A : World.sarah.mirrored = true; break;
					case Keyboard.KEY_SPACE: World.sarah.jump(); break;
					case Keyboard.KEY_E: World.sarah.dismountCow(); break;
					case Keyboard.KEY_F4: View.DEBUG.components[0].state = !View.DEBUG.components[0].state;
										Res.buttonSound.play();
										View.DEBUG.components[0].action.run(); break;
					case Keyboard.KEY_I:
						if(Menu.view == View.INVENTORY){
							View.WORLD.set();
						} else {
							View.INVENTORY.set();
						} break;
					case Keyboard.KEY_F1: View.SETTINGS.components[2].action.run(); break;
					}
				} else {
					
				}
			}
		}
	}
	
	public static ZoneType getZoneType(float x){
		for(Zone z : World.zones){
			if(z.start < x && z.end > x){
				return z.type;
			}
		}
		return null;
	}
}