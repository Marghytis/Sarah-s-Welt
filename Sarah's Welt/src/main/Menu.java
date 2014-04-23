package main;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import world.WorldWindow;

public enum Menu{
	EMPTY(false){
		void setup(){buttons = new Button[0];}
		void buttonPressed(Button b){}//won't happen
	},
	MAIN(true){
		void setup(){
			buttons = new Button[]{
				new Button("New World", (Window.WIDTH*3/16) - 150, (Window.HEIGHT*5/8) - 20, 300, 60),
				new Button("Continue", (Window.WIDTH*13/16) - 150, (Window.HEIGHT*5/8) - 20, 300, 60),
				new Button("Options", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/8) - 20, 300, 60),
				new Button("Exit", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/8) - 20, 300, 60)
			};
		}
		
		void buttonPressed(Button b){
			System.out.println(b.name);
			switch(b.name){
			case "New World":
				WorldWindow.load("TestWorld");
				break;
			case "Continue":
				Game.menu = EMPTY;
				break;
			case "Options":
				Game.menu = OPTIONS;
				break;
			case "Exit":
				Game.closeRequested = true;
				break;
			}
		}
	},
	DEBUG(true){
		void setup(){
			buttons = new Button[]{
				new Button("Flying", (Window.WIDTH/2) - 150, (Window.HEIGHT*9/10) - 20, 300, 60){
					public void render(){
						if(WorldWindow.sarah.flying){
							name = "Flying enabled";
						} else {
							name = "Flying disabled";
						}
						super.render();
					}
				},
				new Button("Textures", (Window.WIDTH/2) - 150, (Window.HEIGHT*7/10) - 20, 300, 60){
					public void render(){
						if(Settings.debugView){
							name = "Textures enabled";
						} else {
							name = "Textures disabled";
						}
						super.render();
					}
				},
				new Button("Hitbox", (Window.WIDTH/2) - 150, (Window.HEIGHT*5/10) - 20, 300, 60){
					public void render(){
						if(Settings.debugView){
							name = "Hitbox shown";
						} else {
							name = "Hitbox hidden";
						}
						super.render();
					}
				},
				new Button("Health", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/10) - 20, 300, 60){
					public void render(){
						if(Settings.health){
							name = "Health shown";
						} else {
							name = "Health hidden";
						}
						super.render();
					}
				},
				new Button("Creatures", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/10) - 20, 300, 60){
					public void render(){
						if(Settings.agro){
							name = "Creatures agressive";
						} else {
							name = "Creatures friendly";
						}
						super.render();
					}
				}
			};
		}
		
		void buttonPressed(Button b){
			if(b.name.contains("Flying")){
				WorldWindow.sarah.flying = !WorldWindow.sarah.flying;
			} else if(b.name.contains("Textures")){
				Settings.switchDebugView();
			} else if(b.name.contains("Hitbox")){
				Settings.hitbox = !Settings.hitbox;
			} else if(b.name.contains("Health")){
				Settings.health = !Settings.health;
			} else if(b.name.contains("Creatures")){
				Settings.agro = !Settings.agro;
			}
		}
	},
	OPTIONS(true){
		void setup(){
			buttons = new Button[]{
					new Button("Controls", (Window.WIDTH/2) - 150, (Window.HEIGHT*5/8) - 20, 300, 60),
					new Button("Sound", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/8) - 20, 300, 60),
					new Button("Back", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/8) - 20, 300, 60)
			};
					
	}
		void buttonPressed(Button b){
			switch(b.name){
			case "Back":
				Game.menu = MAIN;
				break;
			case "Controls":
				Game.menu = CONTROLS;
				break;
			}
		}
	},
	CONTROLS(true){
		void setup(){
			buttons = new Button[]{ new Button("Back", (Window.WIDTH*6/8) - 150, (Window.HEIGHT*1/8) - 20, 300, 60)};
		}
		void buttonPressed(Button b){
			switch(b.name){
			case "Back":
				Game.menu = MAIN;
				break;
			}
		}
		String text = 	"ESC : Main menu\n"
				+ 	"A : left\n"
				+ 	"D : right\n"
				+ 	"   + S : crouch\n"
				+ 	"   + LSHIFT : crouch\n"
				+ 	"W : jump\n"
				+ 	"SPACE : punch/kick\n";
		public void render(){
			super.render();
			GL11.glColor3f(0.6f, 0.8f, 0.8f);
			Window.arial.drawString(20, Window.HEIGHT -100, text, 1, 1);
			GL11.glColor3f(1, 1, 1);
		}
	};
	
	
	Button[] buttons;
	
	boolean pauseWorld;
	
	Menu(boolean stopWorldTicking){
		this.pauseWorld = stopWorldTicking;
		setup();
	}
	
	public static void refresh(){
		for(Menu m : values()){
			m.setup();
		}
	}
	
	public void mouseListening(){
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				
				if(Mouse.getEventButtonState()){
					for(Button b : buttons){
						if(b.contains(Mouse.getEventX(), Mouse.getEventY())){
							b.state = 1;
						}
					}
				} else {
					for(Button b : buttons){
						if(b.state == 1 && b.contains(Mouse.getEventX(), Mouse.getEventY())){
							buttonPressed(b);
						}
						b.state = 0;
					}
				}
			}
		}
	}
	
	abstract void setup();
	
	abstract void buttonPressed(Button button);
	
	public void render(){
		GL11.glLoadIdentity();
		for(Button b : buttons){
			b.render();
		}
	}
}
