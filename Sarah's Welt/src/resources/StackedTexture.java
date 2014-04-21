package resources;

public class StackedTexture extends Texture{
	
	public float widthP, heightP;//width/height of equals parts of the Texture

	public StackedTexture(String name, int xParts, int yParts, float xOffset, float yOffset){
		super(name, xOffset, yOffset);
		this.widthP = 1f/xParts;
		this.heightP = 1f/yParts;
	}
	
	public StackedTexture(String name, int xParts, int yParts) {
		this(name, xParts, yParts, 0, 0);
	}
}
