package resources;

import util.Quad;

public class PackedTexture extends Texture {

	public Quad[] parts;
	
	PackedTexture(String name, Quad... parts) {
		super(name);
		this.parts = parts;
	}

}
