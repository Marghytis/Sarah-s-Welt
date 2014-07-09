package core.menu;

import item.Inventory;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import util.Animation;
import util.Animator;
import world.Calendar;
import world.World;
import core.Main;
import core.Settings;
import core.Window;
import core.WorldO;
import core.geom.Quad;

public class Menu {

	public static View view = View.MAIN;
	
	public static boolean pauseWorld(){
		return view.pauseWorld;
	}
	
	public static void keyListening(){
		while(Keyboard.next()){
			boolean textfield = false;
			for(Component c : view.components){
				if(c != null && c instanceof Textfield && c.state){
					textfield = true;
					if(Keyboard.getEventKeyState())((Textfield)c).keyListening();
				}
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
				if(view == View.MAIN){
					View.WORLD.set();
				} else {
					View.MAIN.set();
				}
			}
			if(!textfield && Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState()){
				if(view == View.DEBUG){
					View.WORLD.set();
				} else {
					View.DEBUG.set();
				}
			}
		}
	}
	
	public static void mouseListening(){
		GL11.glLoadIdentity();
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				if(Mouse.getEventButtonState()){
					for(Component c : view.components){
						c.mousePressed();
					}
				} else {
					for(Component c : view.components){
						c.mouseReleased();
					}
				}
			}
		}
	}

	public static void render(){
		view.render();
	}
	
	public enum View {
		MAIN(true){
			void setup(){
				components = new Component[]{
					new Button("Continue", 	0.5f, 7/8.0f, new Runnable(){public void run(){View.WORLD.set();}}),
					new Button("Worlds", 	0.5f, 5/8.0f, new Runnable(){public void run(){View.WORLDS.set();}}),
					new Button("Settings", 	0.5f, 3/8.0f, new Runnable(){public void run(){View.SETTINGS.set();}}),
					new Button("Exit", 		0.5f, 1/8.0f, new Runnable(){public void run(){Main.beenden = true;}})
				};
			}
		},
		WORLD(false){
			
			Quad health = new Quad(0.1f, 0.15f, 0.8f, 0.03f);
			Quad mana = new Quad(0.1f, 0.05f, 0.8f, 0.03f);
			
			@Override
			void setup(){
				components = new Component[0];}
			
			@Override
			public void render(){
				TextureFile.bindNone();
				GL11.glScalef(Window.WIDTH, Window.HEIGHT, 0);
				//full box
				GL11.glColor4f(1, 1, 1, 0.2f);
				Quad.draw(health.x, health.y, health.x+health.size.x, health.y + health.size.y);
				Quad.draw(mana.x, mana.y, mana.x+mana.size.x, mana.y + mana.size.y);
				//health/mana
				GL11.glColor4f(0.8f, 0, 0, 0.5f);
				Quad.draw(health.x, health.y, health.x + (World.sarah.health/30.0f*health.size.x), health.y + health.size.y);
				GL11.glColor4f(0.8f, 0, 0.8f, 0.5f);
				Quad.draw(mana.x, mana.y, mana.x + (World.sarah.mana/30.0f*mana.size.x), mana.y+mana.size.y);
				//outline
				GL11.glColor4f(0.7f, 0.7f, 0.7f, 1);
				health.outline();
				mana.outline();
				GL11.glColor3f(1, 1, 1);
			}
		},
		WORLDS(true){
			
			
			void setup(){
				
				components = new Component[]{
						new Button("Play!", 		2/8.0f, 6/8.0f, new Runnable(){public void run(){
							for(int c = 0; c < ((Container)components[4]).children.size(); c++){
								ListElement e = ((ListElement)((Container)components[4]).children.get(c));
								if(e.state){
									if(!World.name.equals(e.name)) World.load(e.name);
									WORLD.set();
									break;
								}
							}
						}}),
						new Button("Delete", 		2/8.0f, 5/8.0f, new Runnable(){public void run(){
							
							for(int c = 0; c < ((Container)components[4]).children.size(); c++){
								ListElement e = ((ListElement)((Container)components[4]).children.get(c));
								if(e.state){
									if(!World.name.equals(e.name)){
								        System.gc();
										(new File("worlds/" + e.name + ".world")).delete();//delete sql file
										Main.worlds.remove(e.item);//remove world from list
										
										//reset the visual list in the menu
										((Container)components[4]).children.clear();
										int y = 0;
										for(WorldO world : Main.worlds){
											((Container)components[4]).children.add(new ListElement(6/8.0f, 0.8f - (y*1/8.0f), 300, 60, world.name, world, null));
											y++;
										}
										for(Component co : ((Container)components[4]).children){
											if(((ListElement)co).name.equals(World.name)){
												co.state = true;
											}
										}
										break;
									} else {
										(new Exception("Cannot delete world, because it's active!!!")).printStackTrace();
									}
								}
							}
						}}),
						new Button("New World",	2/8.0f, 4/8.0f, new Runnable(){public void run(){NEW_WORLD.set();}}),
						new Button("Back..",		2/8.0f, 3/8.0f, new Runnable(){public void run(){Menu.view = MAIN;}}),
						new Container("Worldlist")};
				
				int y = 0;
				for(WorldO world : Main.worlds){
					((Container)components[4]).children.add(new ListElement(6/8.0f, 0.8f - (y*1/8.0f), 300, 60, world.name, world, new Runnable(){public void run(){Menu.view = MAIN;}}));
					y++;
				}
				for(Component c : ((Container)components[4]).children){
					if(((ListElement)c).name.equals(World.name)){
						c.state = true;
					}
				}
			}
		},
		NEW_WORLD(true){
			void setup(){
				components = new Component[]{
						new Button("Create!", 0.5f, 2/8.0f, new Runnable(){public void run(){{
							
							String worldName =  View.NEW_WORLD.components[1].text;
							if(worldName != "" && worldName != null){
								World.load(worldName);
								WORLDS.set();
							}
							};}}),
						new Textfield("", 0.5f, 0.5f, 300, 60, new Runnable(){public void run(){
							View.NEW_WORLD.components[0].action.run();
							View.NEW_WORLD.components[1].state = false;}}),
						new Button("Back..", 0.5f, 1/8.0f, new Runnable(){public void run(){Menu.view = MAIN;}})
				};
			}
		},
		SETTINGS(true){
			void setup(){
				components = new Component[]{
						new ToggleButton("Health shown", "Health hidden", false, 			1/2.0f, 	9/12.0f, new Runnable(){public void run(){Settings.health = View.SETTINGS.components[0].state;}}),
						new ToggleButton("Shader active", "Shader inactive", false, 		1/2.0f, 	5/12.0f, new Runnable(){public void run(){Settings.shader = View.SETTINGS.components[1].state;}}),
						new ToggleButton("Sound on", "Sound off", false, 					1/2.0f,		7/8.0f, new Runnable(){public void run(){{
							Settings.sound = !Settings.sound; 
							if(Settings.sound) Res.music.play();};}}),
						new Button("Help Controls",											1/2.0f, 	3/12.0f, new Runnable(){public void run(){CONTROLS.set();}}),
						new Button("Back", 													6/8.0f,		1/8.0f, new Runnable(){public void run(){MAIN.set();}})
				};
			}
		},
		DEBUG(true){
			@Override
			void setup(){
				components = new Component[]{	new ToggleButton("Flying enabled", "Flying disabled", false,		1/2.0f, 	11/12.0f, new Runnable(){public void run(){Settings.flying = View.DEBUG.components[0].state;}}),
										new ToggleButton("Textures enabled", "Textures disabled", true,		1/2.0f, 	9/12.0f, new Runnable(){public void run(){Settings.debugView = View.DEBUG.components[1].state;}}),
										new ToggleButton("Hitbox shown", "Hitbox hidden", false,			1/2.0f, 	7/12.0f, new Runnable(){public void run(){Settings.hitbox = View.DEBUG.components[2].state;}}),
										new ToggleButton("Health shown", "Health hidden", false, 			1/2.0f, 	5/12.0f, new Runnable(){public void run(){Settings.health = View.DEBUG.components[3].state;}}),
										new ToggleButton("Creatures agressive", "Creatures friendly", true,	1/2.0f, 	3/12.0f, new Runnable(){public void run(){Settings.agro = View.DEBUG.components[4].state;}}),
										new ToggleButton("Shader active", "Shader inactive", false, 		1/2.0f, 	1/12.0f, new Runnable(){public void run(){Settings.shader = View.DEBUG.components[5].state;}}),
										
										new ToggleButton("Time running", "Time stopped", true, 				3/4.0f, 	9/12.0f, new Runnable(){public void run(){Settings.time = View.DEBUG.components[6].state;}}),
										new Button("Set morning",								 			3/4.0f, 	7/12.0f, new Runnable(){public void run(){Calendar.setMorning();}}),
										new Button("Set evening",											3/4.0f, 	5/12.0f, new Runnable(){public void run(){Calendar.setEvening();}}),
										new Button("Boost Health",											1/4.0f, 	5/12.0f, new Runnable(){public void run(){World.sarah.health += 5;}})};
			}
		},
		OPTIONS(true){
			@Override
			void setup(){
				components = new Component[]{
						new Button("Controls", 1/2.0f, 5/8.0f, new Runnable(){public void run(){CONTROLS.set();}}),
						new ToggleButton("Sound on", "Sound off", false, 1/2.0f, 3/8.0f, new Runnable(){public void run(){{Settings.sound = !Settings.sound; /*Res.test.stop();*/};}}),
						new Button("Back", 1/2.0f, 1/8.0f, new Runnable(){public void run(){MAIN.set();}})
				};
			}
		},
		INVENTORY(false){
			
			Quad health = new Quad(0.45f, 0.595f, 0.1f, 0.01f);
			Quad mana = new Quad(0.1f, 0.05f, 0.8f, 0.03f);
						
			@Override
			void setup(){
			}
			
			@Override
			public void render(){
				Inventory.render();
				TextureFile.bindNone();
				GL11.glScalef(Window.WIDTH, Window.HEIGHT, 0);
				//full box
				GL11.glColor4f(1, 1, 1, 0.2f);
				Quad.draw(health.x, health.y, health.x+health.size.x, health.y + health.size.y);
				Quad.draw(mana.x, mana.y, mana.x+mana.size.x, mana.y + mana.size.y);
				//health/mana
				GL11.glColor4f(0.8f, 0, 0, 0.5f);
				Quad.draw(health.x, health.y, health.x + (World.sarah.health/30.0f*health.size.x), health.y + health.size.y);
				GL11.glColor4f(0.8f, 0, 0.8f, 0.5f);
				Quad.draw(mana.x, mana.y, mana.x + (World.sarah.mana/30.0f*mana.size.x), mana.y+mana.size.y);
				//outline
				GL11.glColor4f(0.7f, 0.7f, 0.7f, 1);
				health.outline();
				mana.outline();
				GL11.glColor3f(1, 1, 1);
			}
		},
		CONTROLS(true){
			@Override
			void setup(){
				components = new Component[]{ new Button("Back", 6/8.0f, 1/8.0f, new Runnable(){public void run(){SETTINGS.set();}})};
			}
			String text = 	"ESC : Main menu\n"
					+ 	"A : left\n"
					+ 	"D : right\n"
					+ 	"   + S : crouch\n"
					+ 	"   + LSHIFT : crouch\n"
					+ 	"SPACE : jump\n";
			@Override
			public void render(){
				super.render();
				GL11.glColor3f(0.6f, 0.8f, 0.8f);
				Res.arial.drawString(20, Window.HEIGHT -100, text, 1, 1);
				GL11.glColor3f(1, 1, 1);
			}
		},
		DEATH(true){
			@Override
			void setup(){
				components = new Component[]{ new Button("Main Menu", -10000, -10000, new Runnable(){public void run(){view = MAIN;}})};
			}
			
			Animator ani = new Animator(Res.SARAH_DEATH, new Runnable(){public void run(){showButton();}}, new Animation(10, 0, false, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
			
			@Override
			public void set(){
				super.set();
				ani.frame = 0;
//				Res.test.stop();
				Res.death.play();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				components[0].set(-10000, -10000);
			}
			
			@Override
			public void render(){
				GL11.glLoadIdentity();
				TextureFile.bindNone();
				GL11.glColor4f(0, 0, 0, 1);
				Window.fill();
				GL11.glColor3f(1, 1, 1);
				GL11.glPushMatrix();
				GL11.glTranslatef(Window.WIDTH/2, Window.HEIGHT/2, 0);
				Res.SARAH_DEATH.file.bind();
				ani.animate(false);
				TextureFile.bindNone();
				GL11.glTranslatef(0, 100, 0);
				float xText = - 150;
				Res.font.drawString(xText*2, 0, "GAME OVER", 2, 2);
				GL11.glPopMatrix();
				super.render();
			}
			
			public void showButton(){
				components[0].set(1/2.0f, 1/8.0f);
			}
		};
		
		public Component[] components;
		boolean pauseWorld;
		
		View(boolean stopWorldTicking){
			this.pauseWorld = stopWorldTicking;
			setup();
		}
		
		abstract void setup();
		
		public void render(){
			for(Component b : components){
				if(b != null) b.render();
			}
		}
		
		public void set(){
			Menu.view = this;
			setup();
		}
	}
}
