package world.worldGen;

import world.Material;

public enum WorldStructure {
	GRASSTOP(new Layer(Material.GRASS, 10), new Layer(Material.EARTH, 20), new Layer(Material.STONE, 1000)), DESERT;
	
	public Layer[] layers;
	
	WorldStructure(Layer... layers){
		this.layers = layers;
	}
	
	public static class Layer{
		public Material mat;
		public int thickness;
		
		public Layer(Material mat, int thickness){
			this.mat = mat;
			this.thickness = thickness;
		}
	}
	
}
