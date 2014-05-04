package util;

import static org.lwjgl.util.glu.GLU.gluNewTess;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

import world.Node;

public class Tessellator extends GLUtessellatorCallbackAdapter{
	
	public GLUtessellator tessellator;
	
	public Tessellator(){
		tessellator = gluNewTess();
		tessellator.gluTessCallback(GLU.GLU_TESS_BEGIN, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_END, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_VERTEX, this);
//		tessellator.gluTessCallback(GLU.GLU_TESS_EDGE_FLAG, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_ERROR, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_COMBINE, this);
//		tessellator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
//		tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_TRUE);
	}
	float texWidth, texHeight;
	
	public void tessellateOneNode(List<Node> nodes, float texWidth, float texHeight){
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		
		tessellator.gluTessBeginPolygon(null);
		{
			for(int i1 = 0; i1 < nodes.size(); i1++){
				if(nodes.get(i1) != null){
				tessellator.gluTessBeginContour();
				{
					Node n = nodes.get(i1);
					do {
						
						double[] coords = new double[]{n.p.x, n.p.y, 0};
						float[] data = new float[]{n.p.x, n.p.y, n.p.x/texWidth, -n.p.y/texHeight};
						tessellator.gluTessVertex(coords, 0, data);
						n = n.getNext();
					} while(n != nodes.get(i1));
				}
				tessellator.gluTessEndContour();
				}
			}
		}
		tessellator.gluTessEndPolygon();
	}
	
	public void begin(int mode){
		GL11.glBegin(mode);
	}
	
	public void vertex(Object data) {
		float[] coords = (float[]) data;

		GL11.glTexCoord2f(coords[2], coords[3]);
		GL11.glVertex2f(coords[0], coords[1]);
	}
	
	public void end(){
		GL11.glEnd();
	}
	

	public void combine(double[] coords, Object[] data, float[] weight, Object[] outData){
		outData = data;
	}
}
