package core.menu;

import item.Item;
import item.ItemStack;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import util.Animation;
import util.Animator;
import util.Color;
import world.Calendar;
import world.World;
import world.creatures.Villager;
import core.Main;
import core.Settings;
import core.Window;
import core.WorldO;

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
				View.WORLD.set();
			}
			if(!textfield && Keyboard.getEventKey() == Keyboard.KEY_F3 && Keyboard.getEventKeyState()){
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
					mousePressed();
				} else {
					mouseReleased();
				}
			}
		}
	}
	
	public static boolean mousePressed(){
		boolean hit = false;
		for(Component c : view.components){
			if(c.mousePressed()){
				hit = true;
			}
		}
		return hit;
	}
	
	public static boolean mouseReleased(){
		boolean hit = false;
		for(Component c : view.components){
			if(c.mouseReleased()){
				hit = true;
			}
		}
		return hit;
	}

	public static void render(){
		view.render();
	}
	
	public enum View {
		MAIN(true){
			void setup(Object... args){
				components = new Component[]{
					new Button("Continue", 	0.5f, 7/8.0f, new Runnable(){public void run(){View.WORLD.set();}}),
					new Button("Worlds", 	0.5f, 5/8.0f, new Runnable(){public void run(){View.WORLDS.set();}}),
					new Button("Settings", 	0.5f, 3/8.0f, new Runnable(){public void run(){View.SETTINGS.set();}}),
					new Button("Exit", 		0.5f, 1/8.0f, new Runnable(){public void run(){Main.beenden = true;}}),
					new Button("Credits", 	7/8.0f,1/8.0f, new Runnable(){public void run(){CREDITS.set();}}),
				};
			}
		},
		WORLD(false){

			void setup(Object... args){
				components = new Component[]{
						new Bar(Window.WIDTH*0.1f, Window.HEIGHT*0.15f, Window.WIDTH*0.8f, Window.HEIGHT*0.03f, false, "Health", () -> World.sarah.health, 30, new Color(0.8f, 0, 0, 0.5f)),
						new Bar(Window.WIDTH*0.1f, Window.HEIGHT*0.05f, Window.WIDTH*0.8f, Window.HEIGHT*0.03f, false, "Mana", () -> World.sarah.mana, 30, new Color(0.8f, 0, 0.8f, 0.5f)),
				};
			}
			
			public void render(){
				TextureFile.bindNone();
				super.render();
			}
		},
		WORLDS(true){
			
			
			void setup(Object... args){
				
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
			void setup(Object... args){
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
			void setup(Object... args){
				components = new Component[]{
						new ToggleButton("Health shown", "Health hidden", false, 			1/2.0f, 	9/12.0f, new Runnable(){public void run(){Settings.health = View.SETTINGS.components[0].state;}}),
						new ToggleButton("Shader active", "Shader inactive", false, 		1/2.0f, 	5/12.0f, new Runnable(){public void run(){Settings.shader = View.SETTINGS.components[1].state;}}),
						new ToggleButton("Sound on", "Sound off", false, 					1/2.0f,		7/8.0f, new Runnable(){public void run(){{
							Settings.sound = !Settings.sound; 
							if(Settings.sound){
								Res.music.play();
							} else {
								Res.music.stop();
							}};}}),
						new Button("Help Controls",											1/2.0f, 	3/12.0f, new Runnable(){public void run(){CONTROLS.set();}}),
						new Button("Back", 													6/8.0f,		1/8.0f, new Runnable(){public void run(){MAIN.set();}})
				};
			}
		},
		DEBUG(true){
			@Override
			void setup(Object... args){
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
		INVENTORY(false){
						
			void setup(Object... args){
				
				ListElement[] itemStacks = new ListElement[6];
				
				for(ItemStack item : World.sarah.inventory.stacks){
					itemStacks[item.slot] = new ListElement((item.slot+1)*(1.0f/7), 1.0f/5, 100, 100, "", item, () -> World.sarah.inventory.selectedItem = item.slot);
					if(item.inv.selectedItem == item.slot) itemStacks[item.slot].state = true;
				}
				
				components = new Component[]{
						new Bar(Window.WIDTH*0.45f, Window.HEIGHT*0.595f, Window.WIDTH*0.1f, Window.HEIGHT*0.01f, false, "Health", () -> World.sarah.health, 30, new Color(0.8f, 0, 0, 0.5f)),
						new Bar(Window.WIDTH*0.1f, Window.HEIGHT*0.05f, Window.WIDTH*0.8f, Window.HEIGHT*0.03f, false, "Mana", () -> World.sarah.mana, 30, new Color(0.8f, 0, 0.8f, 0.5f)),
						new Container("Inventory", itemStacks),
				};
			}
			
			public void render(){
//Money bag
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glTranslatef(Window.WIDTH*7.0f/8, Window.HEIGHT*7.0f/8, 0);
				Res.MONEYBAG.file.bind();
				Res.MONEYBAG.box.drawTex(Res.MONEYBAG.texs[0][0]);
				float xText = - 50 - (Res.font.getWidth(World.sarah.inventory.coins + "")/3);
				float yText = - (Res.font.getHeight()/2);
				GL11.glColor3f(0.9f, 0.8f, 0.1f);
				Res.font.drawString(xText, yText, World.sarah.inventory.coins + "", 1, 1);
				GL11.glColor3f(1, 1, 1);
				GL11.glPopMatrix();
				TextureFile.bindNone();
//Item slots
				super.render();
			}
		},
		TRADE(false){
			
			Villager villy;
			
			void setup(Object... args){
				ListElement[] villyInv;
				if(args.length > 0){
					villy = (Villager)args[0];
					villyInv = new ListElement[4];
					
					for(ItemStack item : villy.inventory.stacks){
						villyInv[item.slot] = new ListElement(((item.slot%2)+1)*(1.0f/7), (4 - (item.slot/2))/5.0f, 100, 100, "", item, () -> villy.inventory.selectedItem = item.slot);
						if(item.inv.selectedItem == item.slot) villyInv[item.slot].state = true;
					}
				} else {
					villyInv = new ListElement[0];
				}
				
				ListElement[] sarahInv = new ListElement[6];
				
				for(ItemStack item : World.sarah.inventory.stacks){
					sarahInv[item.slot] = new ListElement((item.slot+1)*(1.0f/7), 1.0f/5, 100, 100, "", item, () -> World.sarah.inventory.selectedItem = item.slot);
					if(item.inv.selectedItem == item.slot) sarahInv[item.slot].state = true;
				}
				
				components = new Component[]{
						new Bar(Window.WIDTH*0.45f, Window.HEIGHT*0.595f, Window.WIDTH*0.1f, Window.HEIGHT*0.01f, false, "Health", () -> World.sarah.health, 30, new Color(0.8f, 0, 0, 0.5f)),
						new Bar(Window.WIDTH*0.1f, Window.HEIGHT*0.05f, Window.WIDTH*0.8f, Window.HEIGHT*0.03f, false, "Mana", () -> World.sarah.mana, 30, new Color(0.8f, 0, 0.8f, 0.5f)),
						new Container("Inventory", sarahInv),
						new Container("Inventory Villy", villyInv),
						new Button("Buy!", 3/4.0f, 7/12.0f, new Runnable(){public void run(){
								ItemStack selected = null;
								for(Component e : ((Container)View.TRADE.components[3]).children){
									if(e.state){
										selected = (ItemStack)((ListElement)e).item;
										break;
									}
								}
								if(selected.item != Item.fist && World.sarah.inventory.coins > selected.item.value){
									if(World.sarah.inventory.addItem(selected.item)){
										World.sarah.inventory.coins -= selected.item.value;
										villy.inventory.stacks[selected.slot].item = Item.fist;
									}
								}
							}}),
						new Button("Sell!", 3/4.0f, 5/12.0f, new Runnable(){public void run(){
							ItemStack selected = null;
							for(Component e : ((Container)View.TRADE.components[2]).children){
								if(e.state){
									selected = (ItemStack)((ListElement)e).item;
									break;
								}
							}
							if(selected.item != Item.fist){
								if(villy.inventory.addItem(selected.item)){
									World.sarah.inventory.coins += selected.item.value;
									World.sarah.inventory.stacks[selected.slot].item = Item.fist;
								}
							}
						}}),
				};
			}
			
			public void render(){
//Money bag
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glTranslatef(Window.WIDTH*7.0f/8, Window.HEIGHT*7.0f/8, 0);
				Res.MONEYBAG.file.bind();
				Res.MONEYBAG.box.drawTex(Res.MONEYBAG.texs[0][0]);
				float xText = - 50 - (Res.font.getWidth(World.sarah.inventory.coins + "")/3);
				float yText = - (Res.font.getHeight()/2);
				GL11.glColor3f(0.9f, 0.8f, 0.1f);
				Res.font.drawString(xText, yText, World.sarah.inventory.coins + "", 1, 1);
				GL11.glColor3f(1, 1, 1);
				GL11.glPopMatrix();
				TextureFile.bindNone();
//Item slots
				super.render();
			}
		},
		CONTROLS(true){
			@Override
			void setup(Object... args){
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
		CREDITS(true){
			@Override
			void setup(Object... args){
				components = new Component[]{ new Button("Back", 6/8.0f, 1/8.0f, new Runnable(){public void run(){MAIN.set();}})};
			}
			String text = 	"Graphics: Evelyn" + "\n\n"
						+ 	"Code: Mario" + "\n\n"
						+ 	"Music: Urs & Vlad" + "\n\n"
						+ 	"Documentation: Elli";
			@Override
			public void render(){
				super.render();
				GL11.glColor3f(0.4f, 0.8f, 1f);
				Res.arial.drawString(Window.WIDTH/2 - 200, Window.HEIGHT -100, text, 1, 1);
				GL11.glColor3f(1, 1, 1);
			}
		},
		DEATH(true){
			@Override
			void setup(Object... args){
				components = new Component[]{new Button("Main Menu", -10000, -10000, new Runnable(){public void run(){view = MAIN;}})};
			}
			
			Animator ani = new Animator(Res.SARAH_DEATH, new Runnable(){public void run(){showButton();}}, new Animation(10, 0, false, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
			
			@Override
			public void set(Object... args){
				super.set(args);
				ani.frame = 0;
				Res.music.stop();
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
		public boolean pauseWorld;
		
		View(boolean stopWorldTicking){
			this.pauseWorld = stopWorldTicking;
			setup();
		}
		
		abstract void setup(Object... args);
		
		public void render(){
			for(Component b : components){
				if(b != null) b.render();
			}
		}
		
		public void set(Object... args){
			Menu.view = this;
			setup(args);
		}
	}
}
