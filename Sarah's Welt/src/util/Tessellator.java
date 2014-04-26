package util;

import static org.lwjgl.util.glu.GLU.gluNewTess;

import java.util.ArrayList;
import java.util.List;

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
		tessellator.gluTessCallback(GLU.GLU_TESS_EDGE_FLAG, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_ERROR, this);
		tessellator.gluTessCallback(GLU.GLU_TESS_COMBINE, this);
//		tessellator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
//		tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_TRUE);
	}
	List<Float> vertices;
	List<Integer> indices;
	float texWidth, texHeight;
	int count = 0;
	
	public float[] tessellateOneNode(List<Node> nodes, float texWidth, float texHeight, List<Integer> indices){
		vertices = new ArrayList<>();
		this.indices = indices;
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
						vertices.add(n.p.x/texWidth);
						vertices.add(-n.p.y/texHeight);
						vertices.add(n.p.x);
						vertices.add(n.p.y);
						tessellator.gluTessVertex(coords, 0, count++);
						n = n.next;
					} while(n != nodes.get(i1));
				}
				tessellator.gluTessEndContour();
				}
			}
		}
		tessellator.gluTessEndPolygon();
		float[] vertices2 = new float[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)vertices2[i] = vertices.get(i);
		return vertices2;
	}
	
	public void begin(int type){}
	
	public void end(){}
	
	public void vertex(Object index) {
		indices.add((int)index);
	}
	

	public void combine(double[] coords, Object[] data, float[] weight, Object outData){
		vertices.add((float)coords[0]/texWidth);
		vertices.add(-(float)coords[1]/texHeight);
		vertices.add((float)coords[0]);
		vertices.add((float)coords[1]);
		outData = count++;
	}
}
