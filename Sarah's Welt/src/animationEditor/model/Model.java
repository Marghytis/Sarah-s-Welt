package animationEditor.model;

import org.lwjgl.opengl.GL11;

public class Model {
	
	public Bone bones;
	public Texture tex;
	
	public Model(Texture tex){
		this.tex = tex;
	}

	public void update(int delta){
		
	}
	
	public void render(){
		tex.bind();
			GL11.glPushMatrix();
			GL11.glTranslatef(bones.origin.x, bones.origin.y, 0);
			GL11.glRotatef(bones.origin.rotation, 0, 0, 1);
			bones.render();
			GL11.glPopMatrix();
		Texture.bindNone();
		
	}
	
	public class Bone extends Quad{
		
		public Quad texBox;
		public Origin origin;

		public Bone[] childrenBelow;
		public Bone[] childrenOnTop;

		public Bone(float xParent, float yParent, float x, float y, float width, float height, int xT, int yT, int widthT, int heightT) {
			super(x, y, width, height);
			texBox = new Quad(xT, yT, widthT, heightT);
		}
		
		public void render(){
			
			renderChildren(childrenBelow);
			
			renderThisOne();
			
			renderChildren(childrenOnTop);
		}
		
		public void renderThisOne(){
			super.drawTexBox(texBox);
		}
		
		private void renderChildren(Bone[] children){
			for(Bone b : children){
				GL11.glPushMatrix();
				GL11.glTranslatef(b.origin.x, b.origin.y, 0);
				GL11.glRotatef(origin.rotation, 0, 0, 1);
					b.render();
				GL11.glPopMatrix();
			}
		}
		
	}
	
	public class Origin {
		public float x;//relative to parents origin (x increases right and y up)
		public float y;
		
		public float rotation;
	}
	
}
