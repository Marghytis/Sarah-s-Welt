package world.generation;

import world.Material;
import world.Node;

public class Layer {
	public AimLayer aim;
	public float thickness;

	public Node endNodeTop;
	public Node endNodeBot;
	
	public boolean reachedAim;
	public boolean shrimp;
	public boolean right;

	public Layer(AimLayer aim, float startThickness, boolean right){
		this.aim = aim;
		this.thickness = startThickness;
		this.right = right;
	}
	
	public Layer(AimLayer aim, float startThickness, boolean right, Node nodeT, Node nodeB){
		this.aim = aim;
		this.thickness = startThickness;
		this.right = right;
		this.endNodeTop = nodeT;
		this.endNodeBot = nodeB;
	}
	
	public Layer start(Node top, Node bot){
		this.endNodeTop = top;
		this.endNodeBot = bot;
		bot.connectReal(top);
		top.connectReal(bot);
		return this;
	}

	public void attach(Node top, Node bot){
		if(right){
			endNodeBot.connectReal(bot);
			bot.connectReal(top);
			top.connectReal(endNodeTop);

			endNodeTop = top;
			endNodeBot = bot;
		} else {
			endNodeTop.connectReal(top);
			top.connectReal(bot);
			bot.connectReal(endNodeBot);

			endNodeTop = top;
			endNodeBot = bot;
		}
	}
	
	/**
	 * 
	 * @return true, if it has vanished
	 */
	public boolean approachAim(){
		if(shrimp){
			if(thickness - aim.resizeStep >= 0){
				thickness -= aim.resizeStep;
			} else {
				return true;
			}
		} else if(!reachedAim){
			if(thickness + aim.resizeStep <= aim.thickness){
				thickness += aim.resizeStep;
			} else if(thickness - aim.resizeStep >= aim.thickness){
				thickness -= aim.resizeStep;
			} else {
				reachedAim = true;
			}
		}
		return false;
	}
	
	public static class AimLayer {
		public Material material;
		public float thickness;
		public float resizeStep;
		public int priority;//0 to 100

		public AimLayer(Material material, float thickness, float resizeStep, int priority){
			this.material = material;
			this.thickness = thickness;
			this.resizeStep = resizeStep;
			this.priority = priority;
		}
	}
}
