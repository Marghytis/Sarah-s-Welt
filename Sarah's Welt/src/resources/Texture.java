package resources;

import core.geom.Quad;

public class Texture {

	public TextureFile texFile;
	public Quad texSector;

	public Texture(TextureFile texFile, Quad texSector) {
		this.texFile = texFile;
		this.texSector = texSector;
	}
}
