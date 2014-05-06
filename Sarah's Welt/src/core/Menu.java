package core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.StackedTexture;
import resources.Texture;
import world.Calendar;
import world.World;
import core.geom.Quad;

public class Menu {

	public static View view = View.MAIN;
	
	public static void set(View newView){
		view = newView;
	}
	
	public static boolean pauseWorld(){
		return view.pauseWorld;
	}
	
	public static void keyListening(){
		while(Keyboard.next()){
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
				if(view == View.MAIN){
					view = View.EMPTY;
				} else {
					view = View.MAIN;
				}
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState()){
				if(view == View.DEBUG){
					view = View.EMPTY;
				} else {
					view = View.DEBUG;
				}
			}
		}
	}
	
	public static void mouseListening(){
		GL11.glLoadIdentity();
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				if(Mouse.getEventButtonState()){
					for(Button b : view.buttons){
						if(b.contains(Mouse.getEventX(), Mouse.getEventY())){
							Res.buttonSound.play();
							if(b instanceof ToggleButton){
								b.state = !b.state;
								b.onClick.run();
							} else {
								b.state = true;
							}
						}
					}
				} else {
					for(Button b : view.buttons){
						if(!(b instanceof ToggleButton)){
							if(b.contains(Mouse.getEventX(), Mouse.getEventY()) && b.state == true){
								b.onClick.run();
							}
							b.state = false;
						}
					}
				}
			}
		}
	}

	public static void render(){
		view.render();
	}
	
	public enum View {
		EMPTY(false){
			void setup(){buttons = new Button[0];}
		},
		MAIN(true){
			void setup(){
				buttons = new Button[]{
					new Button("New World", 3/16.0f, 7/8.0f, new Runnable(){public void run(){World.load("TestWorld");}}),
					new Button("Continue", 3/16.0f, 5/8.0f, new Runnable(){public void run(){Menu.view = EMPTY;}}),
					new Button("Options", 3/16.0f, 3/8.0f, new Runnable(){public void run(){Menu.view = OPTIONS;}}),
					new Button("Exit", 3/16.0f, 1/8.0f, new Runnable(){public void run(){Main.beenden = true;}})
				};
			}
		},
		DEBUG(true){
			void setup(){
				buttons = new Button[]{	new ToggleButton("Flying enabled", "Flying disabled", false,		1/2.0f, 	11/12.0f, () -> Settings.flying = View.DEBUG.buttons[0].state),
										new ToggleButton("Textures enabled", "Textures disabled", true,		1/2.0f, 	9/12.0f, () -> Settings.debugView = View.DEBUG.buttons[1].state),
										new ToggleButton("Hitbox shown", "Hitbox hidden", false,			1/2.0f, 	7/12.0f, () -> Settings.hitbox = View.DEBUG.buttons[2].state),
										new ToggleButton("Health shown", "Health hidden", false, 			1/2.0f, 	5/12.0f, () -> Settings.health = View.DEBUG.buttons[3].state),
										new ToggleButton("Creatures agressive", "Creatures friendly", true,	1/2.0f, 	3/12.0f, () -> Settings.agro = View.DEBUG.buttons[4].state),
										new ToggleButton("Shader active", "Shader inactive", false, 			1/2.0f, 	1/12.0f, () -> Settings.shader = View.DEBUG.buttons[5].state),
										
										new ToggleButton("Time running", "Time stopped", true, 				3/4.0f, 	9/12.0f, () -> Settings.time = View.DEBUG.buttons[6].state),
										new Button("Set morning",								 			3/4.0f, 	7/12.0f, () -> Calendar.setMorning()),
										new Button("Set evening",											3/4.0f, 	5/12.0f, () -> Calendar.setEvening())};
			}
		},
		OPTIONS(true){
			void setup(){
				buttons = new Button[]{
						new Button("Controls", 1/2.0f, 5/8.0f, new Runnable(){public void run(){Menu.view = CONTROLS;}}),
						new ToggleButton("Sound on", "Sound off", false, 1/2.0f, 3/8.0f, () -> {Settings.sound = !Settings.sound; Res.test.stop();}),
						new Button("Back", 1/2.0f, 1/8.0f, new Runnable(){public void run(){Menu.view = MAIN;}})
				};
			}
		},
		CONTROLS(true){
			void setup(){
				buttons = new Button[]{ new Button("Back", 6/8.0f, 1/8.0f, new Runnable(){public void run(){view = MAIN;}})};
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
		};
		
		Button[] buttons;
		boolean pauseWorld;
		
		View(boolean stopWorldTicking){
			this.pauseWorld = stopWorldTicking;
			setup();
		}
		
		abstract void setup();
		
		public void render(){
			for(Button b : buttons){
				b.render();
			}
		}
	}
	
	public static class Button extends Quad{
		
		static StackedTexture tex = Texture.MENU_BUTTON;
		
		public String name;
		public boolean state;
		Runnable onClick;
		
		public Button(String name, float x, float y, Runnable onClick){
			super(x, y, 300, 60);
			this.name = name;
			this.onClick = onClick;
		}
		
		public void render(){
			GL11.glPushMatrix();
			GL11.glTranslatef((x*Window.WIDTH) - (size.x/2), (y*Window.HEIGHT) - (size.y/2), 0);
			drawTex(tex, 0, state ? 1 : 0);
			float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
			float yText = y + (size.y/2) - (Res.font.getHeight()/2);
			Res.font.drawString(xText, yText, name, 1, 1);
			GL11.glPopMatrix();
		}
		
		public boolean contains(float x, float y){
			float realX = (this.x*Window.WIDTH) - (size.x/2);
			float realY = (this.y*Window.HEIGHT) - (size.y/2);
			return x > realX && x < realX + size.x && y > realY && y < realY + size.y;
		}
	}
	
	public static class ToggleButton extends Button {
		
		public String name1;
		public String name2;
		
		public ToggleButton(String name1, String name2, boolean state, float x, float y, Runnable onClick){
			super(name1, x, y, onClick);
			this.name1 = name1;
			this.name2 = name2;
			this.state = state;
		}

		public void render(){
			if(state){
				name = name1;
			} else {
				name = name2;
			}
			super.render();
		}
		
	}
}
