package main;

import org.lwjgl.input.Mouse;

import resources.Texture;
import world.World;

public class Menu{

	static Texture background = Texture.MENU_BACKGROUND;
	static MenuSet menu = MenuSet.MAIN;
	
	public static void refresh(){
		menu.setup();
	}
	
	public static void tick(float dTime){
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				
				if(Mouse.getEventButtonState()){
					for(Button b : menu.buttons){
						if(b.contains(Mouse.getEventX(), Mouse.getEventY())){
							b.state = 1;
						}
					}
				} else {
					for(Button b : menu.buttons){
						if(b.state == 1 && b.contains(Mouse.getEventX(), Mouse.getEventY())){
							System.out.println(b.name);
							switch(b.name){
							case "New World":
							case "Continue":
								Game.inWorld = true;
								World.load("Test");
								break;
							case "Exit":
								Game.closeRequested = true;
								break;
							}
						}
						b.state = 0;
					}
				}
				
			}
		}
	}
	
	public static void render(){
		background.bind();
		Game.window.fill();
		for(Button b : menu.buttons){
			b.render();
		}
	}
	
	private enum MenuSet{
		MAIN{
			void setup(){
				buttons = new Button[4];
				buttons[0] = new Button("New World", (Window.WIDTH*3/16) - 150, (Window.HEIGHT*5/8) - 20, 300, 60);
				buttons[1] = new Button("Continue", (Window.WIDTH*13/16) - 150, (Window.HEIGHT*5/8) - 20, 300, 60);
				buttons[2] = new Button("Options", (Window.WIDTH/2) - 150, (Window.HEIGHT*3/8) - 20, 300, 60);
				buttons[3] = new Button("Exit", (Window.WIDTH/2) - 150, (Window.HEIGHT*1/8) - 20, 300, 60);
			}
		};
		
		Button[] buttons;
		
		abstract void setup();
	}
}
