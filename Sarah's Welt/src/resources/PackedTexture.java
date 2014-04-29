package resources;

import util.QuadOld;

public class PackedTexture extends Texture {

	public QuadOld[] parts;
	
	PackedTexture(String name, QuadOld... parts) {
		super(name);
		this.parts = parts;
	}

}
