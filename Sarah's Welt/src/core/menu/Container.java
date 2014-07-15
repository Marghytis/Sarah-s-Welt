package core.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Container extends Component{

	public List<Component> children;
	
	public Container(String name, Component... children) {
		super(0, 0, 0, 0, name, "", null);
		if(children.length > 0){
			this.children = Arrays.asList(children);
		} else {
			this.children = new ArrayList<>();
		}
	}

	public void render() {
		for(Component c : children){
			c.render();
		}
	}

	public boolean contains(float x, float y){
		return false;
	}

	public boolean mousePressed() {
		boolean out = false;
		for(Component c : children){
			if(c.mousePressed()){
				children.forEach((ch) -> ch.state = false);
				c.state = true;
				out = true;
			}
		}
		return out;
	}

	public boolean mouseReleased() {
		for(Component c : children){
			c.mouseReleased();
		}
		return false;
	}
}
