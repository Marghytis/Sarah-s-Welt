package resources;

public class StackedTexture extends Texture{
	
	public float widthS, heightS;//width/height of equals parts of the Texture

	public StackedTexture(String name, int xParts, int yParts, float xOffset, float yOffset){
		super(name, xOffset, yOffset);
		this.widthS = (float)width/xParts;
		this.heightS = (float)height/yParts;
	}
	
	public StackedTexture(String name, int xParts, int yParts) {
		this(name, xParts, yParts, 0, 0);
	}
}
