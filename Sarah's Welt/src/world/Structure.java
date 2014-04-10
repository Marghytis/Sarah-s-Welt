package world;

import org.lwjgl.opengl.GL11;

import resources.Texture;
import util.Quad;

public class Structure {
	
	public StructureType type;
	public Point pos;
	public Node worldLink;

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
	
	
	public enum StructureType {
		CLOUD(new Texture("Cloud"), -0.5f, -0.5f),
		TREE_1(new Texture("tree1"), -0.5f, -0.3f),
		TREE_2(new Texture("tree2"), -0.5f, -0.3f),
		TREE_3(new Texture("tree3"), -0.5f, -0.3f);

		public Texture tex;
		public Quad box;
		
		StructureType(Texture tex, float xOffset, float yOffset){
			this.tex = tex;
			box = new Quad(xOffset*tex.width, yOffset*tex.height, tex.width, tex.height);
		}
	}
	
}
