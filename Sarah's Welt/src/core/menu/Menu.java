package core.menu;

import item.Inventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import util.Animation;
import util.Animator;
import world.Calendar;
import world.World;
import world.WorldView;
import core.Main;
import core.Settings;
import core.Window;
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
				if(c instanceof Textfield && c.state){
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
						if(c.contains(Mouse.getEventX(), Mouse.getEventY())){
							componentPressed(c);
						}
					}
				} else {
					for(Component c : view.components){
						componentReleased(c);
					}
				}
			}
		}
	}
	
	public static void componentPressed(Component b){
		if(b instanceof Button){
			Res.buttonSound.play();
		}
		if(b instanceof ToggleButton){
			b.state = !b.state;
			b.action.run();
		} else if(!(b instanceof Textfield)){
			b.state = true;
		}
	}
	
	public static void componentReleased(Component b){
		if(!(b instanceof ToggleButton)){
			if(b.contains(Mouse.getEventX(), Mouse.getEventY())){
				if(b instanceof Button && b.state == true){
					b.action.run();
				} else {
					b.state = true;
					return;
				}
			}
			b.state = false;
		}
	}

	public static void render(){
		view.render();
	}
	
	public enum View {
		MAIN2(true){
			void setup(){
				components = new Component[]{
					new Button("Continue", 	3/8.0f, 7/8.0f, new Runnable(){public void run(){View.WORLD.set();}}),
					new Button("Worlds", 	3/8.0f, 5/8.0f, new Runnable(){public void run(){View.WORLDS.set();}}),
					new Button("Settings", 	3/8.0f, 3/8.0f, new Runnable(){public void run(){View.SETTINGS.set();}}),
					new Button("Exit", 		3/8.0f, 1/8.0f, new Runnable(){public void run(){Main.beenden = true;}})
				};
			}
		},
		WORLD(false){
			
			Quad health = new Quad(0.45f, 0.595f, 0.1f, 0.01f);
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
				components = new Component[4];

				int i = 0;
				components[i++] = new Button("Play!", 		3/8.0f, 7/8.0f, new Runnable(){public void run(){WORLD.set();}});
				components[i++] = new Button("Delete", 		3/8.0f, 5/8.0f, new Runnable(){public void run(){Menu.view = WORLDS;}});
				components[i++] = new Button("New World",	3/8.0f, 3/8.0f, new Runnable(){public void run(){Menu.view = SETTINGS;}});
				components[i++] = new Button("Back..",		3/8.0f, 1/8.0f, new Runnable(){public void run(){Menu.view = MAIN2;}});
			}
		},
		SETTINGS(true){
			void setup(){
				
			}
		},
		MAIN(true){
			@Override
			void setup(){
				components = new Component[]{
					new Button("New World", 3/16.0f, 15/16.0f, new Runnable(){@Override
					public void run(){
						String worldName =  View.MAIN.components[1].text;
						if(worldName != "" && worldName != null){
							World.load(worldName);
							WorldView.reset();
						}}}),
					new Textfield("WorldName", 8/16.0f, 15/16.0f, 300, 60, () -> {
						View.MAIN.components[0].action.run();
						View.MAIN.components[1].state = false;}),
					new Button("Continue", 3/16.0f, 13/16.0f, new Runnable(){@Override
					public void run(){Menu.view = WORLD;}}),
					new Button("Save World", 3/16.0f, 11/16.0f, new Runnable(){@Override
					public void run(){World.save();}}),
					new Button("Load World", 3/16.0f, 9/16.0f, new Runnable(){@Override
					public void run(){
						String worldName =  View.MAIN.components[5].text;
						if(worldName != "" && worldName != null){
							World.load(worldName);
							WorldView.reset();
						}}}),
					new Textfield("WorldName", 8/16.0f, 9/16.0f, 300, 60, () -> {
						View.MAIN.components[4].action.run();
						View.MAIN.components[5].state = false;}),
					new Button("Options", 3/16.0f, 4/16.0f, new Runnable(){@Override
					public void run(){Menu.view = OPTIONS;}}),
					new Button("Exit", 3/16.0f, 2/16.0f, new Runnable(){@Override
					public void run(){Main.beenden = true;}})
				};
			}
		},
		DEBUG(true){
			@Override
			void setup(){
				components = new Component[]{	new ToggleButton("Flying enabled", "Flying disabled", false,		1/2.0f, 	11/12.0f, () -> Settings.flying = View.DEBUG.components[0].state),
										new ToggleButton("Textures enabled", "Textures disabled", true,		1/2.0f, 	9/12.0f, () -> Settings.debugView = View.DEBUG.components[1].state),
										new ToggleButton("Hitbox shown", "Hitbox hidden", false,			1/2.0f, 	7/12.0f, () -> Settings.hitbox = View.DEBUG.components[2].state),
										new ToggleButton("Health shown", "Health hidden", false, 			1/2.0f, 	5/12.0f, () -> Settings.health = View.DEBUG.components[3].state),
										new ToggleButton("Creatures agressive", "Creatures friendly", true,	1/2.0f, 	3/12.0f, () -> Settings.agro = View.DEBUG.components[4].state),
										new ToggleButton("Shader active", "Shader inactive", false, 		1/2.0f, 	1/12.0f, () -> Settings.shader = View.DEBUG.components[5].state),
										
										new ToggleButton("Time running", "Time stopped", true, 				3/4.0f, 	9/12.0f, () -> Settings.time = View.DEBUG.components[6].state),
										new Button("Set morning",								 			3/4.0f, 	7/12.0f, () -> Calendar.setMorning()),
										new Button("Set evening",											3/4.0f, 	5/12.0f, () -> Calendar.setEvening()),
										new Button("Boost Health",											1/4.0f, 	5/12.0f, () -> World.sarah.health += 5)};
			}
		},
		OPTIONS(true){
			@Override
			void setup(){
				components = new Component[]{
						new Button("Controls", 1/2.0f, 5/8.0f, new Runnable(){@Override
						public void run(){CONTROLS.set();}}),
						new ToggleButton("Sound on", "Sound off", false, 1/2.0f, 3/8.0f, () -> {Settings.sound = !Settings.sound; /*Res.test.stop();*/}),
						new Button("Back", 1/2.0f, 1/8.0f, new Runnable(){@Override
						public void run(){MAIN.set();}})
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
				components = new Component[]{ new Button("Back", 6/8.0f, 1/8.0f, new Runnable(){@Override
				public void run(){MAIN.set();}})};
			}
			String text = 	"ESC : Main menu\n"
					+ 	"A : left\n"
					+ 	"D : right\n"
					+ 	"   + S : crouch\n"
					+ 	"   + LSHIFT : crouch\n"
					+ 	"W : jump\n"
					+ 	"SPACE : punch/kick\n";
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
				components = new Component[]{ new Button("Main Menu", -10000, -10000, new Runnable(){@Override
				public void run(){view = MAIN;}})};
			}
			
			Animator ani = new Animator(Res.SARAH_DEATH, () -> showButton(), new Animation(10, 0, false, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
			
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
				b.render();
			}
		}
		
		public void set(){
			Menu.view = this;
		}
	}
}
