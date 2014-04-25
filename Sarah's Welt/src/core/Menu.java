package core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import core.geom.Quad;
import resources.StackedTexture;
import resources.Texture;
import world.WorldWindow;

public class Menu {

	public static View view = View.EMPTY;
	
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
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				
				if(Mouse.getEventButtonState()){
					for(Button b : view.buttons){
						if(b.contains(Mouse.getEventX(), Mouse.getEventY())){
							if(b instanceof ToggleButton){
								b.state = !b.state;
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
		for(Button b : view.buttons){
			b.render();
		}
	}
	
	public enum View {
		EMPTY(false){
			void setup(){buttons = new Button[0];}
		},
		MAIN(true){
			void setup(){
				buttons = new Button[]{
					new Button("New World", 3/16.0f, 7/8.0f, () -> WorldWindow.load("TestWorld")),
					new Button("Continue", 3/16.0f, 5/8.0f, () -> view = EMPTY),
					new Button("Options", 3/16.0f, 3/8.0f, () -> view = OPTIONS),
					new Button("Exit", 3/16.0f, 1/8.0f, () -> Main.beenden = true)
				};
			}
		},
		DEBUG(true){
			public ToggleButton flying = new ToggleButton("Flying disabled", "Flying enabled", 				1/2.0f, 	11/12.0f);
			public ToggleButton landscape = new ToggleButton("Textures enabled", "Textures disabled", 		1/2.0f, 	9/12.0f);
			public ToggleButton hitbox = new ToggleButton("Hitbox hidden", "Hitbox shown", 					1/2.0f, 	7/12.0f);
			public ToggleButton health = new ToggleButton("Health hidden", "Health shown", 					1/2.0f, 	5/12.0f);
			public ToggleButton creatures = new ToggleButton("Creatures agressive", "Creatures friendly", 	1/2.0f, 	3/12.0f);
			public ToggleButton shader = new ToggleButton("Shader inactive", "Shader active", 				1/2.0f, 	1/12.0f);
			void setup(){
				buttons = new Button[]{flying, landscape, hitbox, health, creatures, shader};
			}
		},
		OPTIONS(true){
			void setup(){
				buttons = new Button[]{
						new Button("Controls", 1/2.0f, 5/8.0f, () -> view = CONTROLS),
						new Button("Sound", 1/2.0f, 3/8.0f),
						new Button("Back", 1/2.0f, 1/8.0f, () -> view = MAIN)
				};
			}
		},
		CONTROLS(true){
			void setup(){
				buttons = new Button[]{ new Button("Back", 6/8.0f, 1/8.0f, () -> view = MAIN)};
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
				Window.arial.drawString(20, Window.HEIGHT -100, text, 1, 1);
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
	}
	
	public class Button extends Quad{
		
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
			GL11.glTranslatef((x*Window.WIDTH) - size.x/2, (y*Window.HEIGHT) - size.y/2, 0);
			drawTex(tex, 0, state ? 1 : 0);
			float xText = x + (size.x/2) - (Res.font.getWidth(name)/3);
			float yText = y + (size.y/2) - (Res.font.getHeight()/2);
			Res.font.drawString(xText, yText, name, 1, 1);
		}
	}
	
	public class ToggleButton extends Button {
		
		public String name1;
		public String name2;
		
		public ToggleButton(String name1, String name2, float x, float y){
			super(name1, x, y, null);
			this.name1 = name1;
			this.name2 = name2;
		}

		public void render(){
			if(!state){
				name = name1;
			} else {
				name = name2;
			}
			super.render();
		}
		
	}
}
