package resources;

public class StackedTexture extends Texture{
	
	public int widthS, heightS;//width/height of equals parts of the Texture
	public float widthT, heightT;

	public StackedTexture(String name, int xParts, int yParts, float xOffset, float yOffset){
		super(name, xOffset, yOffset);
		widthT = 1.0f/xParts;
		heightT = 1.0f/yParts;
		this.widthS = width/xParts;
		this.heightS = height/yParts;
	}
	
	public StackedTexture(String name, int xParts, int yParts) {
		this(name, xParts, yParts, 0, 0);
	}
}
