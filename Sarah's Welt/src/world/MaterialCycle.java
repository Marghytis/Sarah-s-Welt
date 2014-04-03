package world;

import util.Cycle;

public class MaterialCycle extends Cycle{

	public Material mat = Material.AIR;

	public Node[] cutsRight;
	public Node[] cutsLeft;
	
	public MaterialCycle(Material mat, Line... lines){
		super(lines);
		this.mat = mat;
	}
	
}
