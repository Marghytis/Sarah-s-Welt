package resources;

import core.geom.Quad;


public class StackedTextures{
	
	public int widthEach, heightEach;//in pixels
	public Texture[][] texs;

	public StackedTextures(TextureFile texFile, int xParts, int yParts){
		texs = new Texture[xParts][yParts];
		widthEach = texFile.width/xParts;
		heightEach = texFile.height/yParts;
		
		for(int x = 0; x < xParts; x++){
			for(int y = 0; y < yParts; y++){
				texs[x][y] = new Texture(texFile, new Quad(x*widthEach, y*heightEach, widthEach, heightEach));
			}
		}
	}
}
