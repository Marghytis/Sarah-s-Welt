package resources;

import core.geom.Quad;
import core.geom.Vec;

public class Texture {

	public TextureFile file;
	public Quad quad;
	public Quad box;
	
	public Texture(String string) {
		this(new TextureFile(string));
	}
	
	public Texture(TextureFile texFile){
		this(texFile, 0, 0);
	}
	
	public Texture(String filename, float xOffset, float yOffset){
		this(new TextureFile(filename), xOffset, yOffset);
	}

	public Texture(TextureFile texFile, float xOffset, float yOffset) {
		this(texFile, new Vec(xOffset, yOffset));
	}

	public Texture(TextureFile texFile, Vec offset) {
		this(texFile, new Quad(0, 0, 1, 1), offset);
	}

	public Texture(TextureFile texFile, Quad texSector, Vec offset) {
		this(texFile, texSector, new Quad(offset, new Vec(texFile.width, texFile.height)));
	}

	public Texture(TextureFile texFile, Quad texSector, Quad box) {
		this.file = texFile;
		this.quad = texSector;
		this.box = box;
	}
}
