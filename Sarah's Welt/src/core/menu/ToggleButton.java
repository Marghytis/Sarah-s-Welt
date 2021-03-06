package core.menu;

import org.lwjgl.input.Mouse;

import resources.Res;

public class ToggleButton extends Button {
	
	public String name1;
	public String name2;
	
	public ToggleButton(String name1, String name2, boolean state, float cX, float cY, Runnable onClick){
		super(name1, cX, cY, onClick);
		this.name1 = name1;
		this.name2 = name2;
		this.state = state;
	}

	@Override
	public void render(){
		if(state){
			name = name1;
		} else {
			name = name2;
		}
		super.render();
	}
	
	public boolean mousePressed() {
		if(contains(Mouse.getEventX(), Mouse.getEventY())){
			Res.buttonSound.play();
			state = !state;
			action.run();
			return true;
		}
		return false;
	}

	public boolean mouseReleased() {
		return false;
	}
}
