package world.worldGen;

import world.Material;

public enum Landscape {
	SOMEHILLS(new Layer(Material.GRASS, 10), new Layer(Material.EARTH, 20), new Layer(Material.STONE, 1000)), DESERT;
	
	public Layer[] layers;
	
	Landscape(Layer... layers){
		this.layers = layers;
	}
	
	public static class Layer{
		public Material mat;
		public int height;
		
		public Layer(Material mat, int height){
			this.mat = mat;
			this.height = height;
		}
	}
	
}
