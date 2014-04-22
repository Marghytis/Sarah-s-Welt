package world.structures;

import resources.StackedTexture;
import util.Animation;
import util.Quad;
import world.Node;
import world.Point;
import world.Thing;

public abstract class Structure extends Thing{

	public static StackedTexture CLOUD = new StackedTexture("Cloud", 1, 1, -0.5f, -0.5f);
	public static StackedTexture GRASS_TUFT = new StackedTexture("Grass_tuft", 4, 1, -0.5f, -0.2f);
	public static StackedTexture[] TREE = {		new StackedTexture("tree1", 1, 1, -0.5f, -0.3f),
												new StackedTexture("tree2", 1, 1, -0.5f, -0.3f),
												new StackedTexture("tree3", 1, 1, -0.5f, -0.3f)
											};
	public static StackedTexture[] BUSH = {		new StackedTexture("bush1", 1, 1, -0.5f, -0.2f),
												new StackedTexture("bush2", 1, 1, -0.5f, -0.2f)
											};
	public static StackedTexture[] FLOWER = {	new StackedTexture("flower1", 1, 1, -0.5f, 0f),
												new StackedTexture("flower2", 1, 1, -0.5f, 0f),
												new StackedTexture("flower3", 1, 1, -0.5f, 0f)
											};
	
	public Structure(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		super(tex, defaultAni, pos, worldLink, new Quad(tex.xOffset*tex.widthS, tex.yOffset*tex.heightS, tex.widthS, tex.heightS));
	}
}
