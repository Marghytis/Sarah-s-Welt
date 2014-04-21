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
				new Button("Flying", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/8) - 20, 300, 60){
					public void render(){
						if(WorldWindow.sarah.flying){
							name = "Flying enabled";
						} else {
							name = "Flying disabled";
						}
						super.render();
					}
				},
				new Button("Textures", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/8) - 20, 300, 60){
					public void render(){
						if(Settings.debugView){
							name = "Textures enabled";
						} else {
							name = "Textures disabled";
						}
						super.render();
					}
				},
			};
		}
		
		void buttonPressed(Button b){
			if(b.name.contains("Flying")){
				if(WorldWindow.sarah.flying){
					b.name = "Flying disabled";
				} else {
					b.name = "Flying enabled";
				}
				WorldWindow.sarah.flying = !WorldWindow.sarah.flying;
			} else if(b.name.contains("Textures")){
				Settings.switchDebugView();
			}
		}
	},
	OPTIONS(true){
		void setup(){
			buttons = new Button[]{
					new Button("Sound", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/8) - 20, 300, 60),
					new Button("Back", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/8) - 20, 300, 60)
			};
					
	}
		void buttonPressed(Button b){
			System.out.println(b.name);
			switch(b.name){
			case "Back":
				Game.menu = MAIN;
				break;
			}
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
