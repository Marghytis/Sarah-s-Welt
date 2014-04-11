package world;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import util.Quad;

public abstract class Structure {
	
	public StructureType type;
	public Point pos;
	public Node worldLink;
	public boolean showInFront = false;

	public Structure(StructureType type, Point pos){
		this.type = type;
		this.pos = pos;
	}
	
	public Structure(StructureType type, float x, float y){
		this(type, new Point(x, y));
	}
	
	public void tick(float dTime){}
	
	/**
	 * World coordinates
	 */
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		type.box.draw(type.tex);
		
		
		GL11.glPopMatrix();
	}
	
	
	public static enum StructureType {
		CLOUD(new StackedTexture("Cloud", 1, 1), -0.5f, -0.5f),
		TREE_1(new StackedTexture("tree1", 1, 1), -0.5f, -0.3f),
		TREE_2(new StackedTexture("tree2", 1, 1), -0.5f, -0.3f),
		TREE_3(new StackedTexture("tree3", 1, 1), -0.5f, -0.3f),
		GRASS_TUFT(new StackedTexture("Grass_tuft", 4, 1), -0.5f, -0.2f);

		public StackedTexture tex;
		public Quad box;
		
		StructureType(StackedTexture tex, float xOffset, float yOffset){
			this.tex = tex;
			//xOffset*pieceWidth, yOffset*pieceHeight, pieceWidth, pieceHeight
			box = new Quad(xOffset*((StackedTexture)tex).widthP*tex.width, yOffset*((StackedTexture)tex).heightP*tex.height, ((StackedTexture)tex).widthP*tex.width, ((StackedTexture)tex).heightP*tex.height);
		}
	}
	
}
