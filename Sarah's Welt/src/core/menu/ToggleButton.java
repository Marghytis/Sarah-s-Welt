package core.menu;

public class ToggleButton extends Button {
	
	public String name1;
	public String name2;
	
	public ToggleButton(String name1, String name2, boolean state, float x, float y, Runnable onClick){
		super(name1, x, y, onClick);
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
}
