package resources;

import core.geom.Quad;


public class StackedTextures{
	
	public int widthEach, heightEach;//in pixels
	public Texture[][] texs;
	public TextureFile file;
	public Quad box;

	public StackedTextures(String filename, int xParts, int yParts, float xOffset, float yOffset){
		if(yParts <= 0 || xParts <= 0){
			(new Exception("Can't create a stacked Texture with negative or zero size!!")).printStackTrace();
			System.exit(1);
		}
		
		file = new TextureFile(filename);
		
		texs = new Texture[xParts][yParts];
		widthEach = file.width/xParts;
		heightEach = file.height/yParts;

		float widthEachRatioX = 1.0f/xParts;
		float widthEachRatioY = 1.0f/yParts;
		
		for(int x = 0; x < xParts; x++){
			for(int y = 0; y < yParts; y++){
				texs[x][y] = new Texture(file, new Quad(x*widthEachRatioX, y*widthEachRatioY, (x+1)*widthEachRatioX, (y+1)*widthEachRatioY), new Quad(xOffset*widthEach, yOffset*heightEach, widthEach, heightEach));
			}
		}
		
		box = texs[0][0].box;
	}
}	
