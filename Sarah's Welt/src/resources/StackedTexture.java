package resources;

public class StackedTexture extends Texture{
	
	public float widthP, heightP;//width/height of equals parts of the Texture

	StackedTexture(String name, int xParts, int yParts) {
		super(name);
		this.widthP = 1f/xParts;
		this.heightP = 1f/yParts;
	}
}
