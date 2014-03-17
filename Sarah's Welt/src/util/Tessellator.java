package util;

import static org.lwjgl.util.glu.GLU.gluNewTess;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

import world.Line;
import world.Node;

public class Tessellator {
	
	public GLUtessellator tessellator;
	
	public Tessellator(){
		tessellator = gluNewTess();
		Callback callback = new Callback();
		tessellator.gluTessCallback(GLU.GLU_TESS_BEGIN, callback);
		tessellator.gluTessCallback(GLU.GLU_TESS_END, callback);
		tessellator.gluTessCallback(GLU.GLU_TESS_VERTEX, callback);
		tessellator.gluTessCallback(GLU.GLU_TESS_EDGE_FLAG, callback);
		tessellator.gluTessCallback(GLU.GLU_TESS_ERROR, callback);
		tessellator.gluTessCallback(GLU.GLU_TESS_COMBINE, callback);
//		tessellator.gluTessProperty(GLU.GLU_TESS_BOUNDARY_ONLY, GL11.GL_TRUE);
//		tessellator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
	}
	
	public void tessellate(List<Line> lines, float texWidth, float texHeight){
		tessellator.gluTessBeginPolygon(null);
		for(int i1 = 0; i1 < lines.size(); i1++){
			tessellator.gluTessBeginContour();
			Node n = lines.get(i1).start;
			do {
				double[] coords = new double[]{n.p.x, n.p.y, 0};
				float[] vertexData = new float[]{n.p.x, n.p.y, 0, n.p.x/texWidth, -n.p.y/texHeight};
				n = n.next;
				tessellator.gluTessVertex(coords, 0, vertexData);
			} while(!(n.equals(lines.get(i1).start) || n.next == null));
			tessellator.gluTessEndContour();
		}
		tessellator.gluTessEndPolygon();
//		GL11.glColor3f(0, 0, 0);
//		for(Line line : lines){
//			GL11.glBegin(GL11.GL_LINE_LOOP);
//			for(Point p : line.vertices){
//				GL11.glVertex2f(p.x, p.y);
//			}
//			GL11.glEnd();
//		}
	}
	
	public class Callback extends GLUtessellatorCallbackAdapter{
		
		public void begin(int type) {
			GL11.glBegin(type);
		}
		public void end(){
			GL11.glEnd();
		}
	
		public void vertex(Object vertexData) {
			float[] data = (float[])vertexData;
			GL11.glTexCoord2f(data[3], data[4]);
			GL11.glVertex3f(data[0], data[1], data[2]);
		}
		

		public void combine(double[] coords, Object[] data, float[] weight, Object[] outData){
			double[] newVertex = new double[5];

            newVertex[0] = coords[0];
            newVertex[1] = coords[1];
            newVertex[2] = coords[2];
            newVertex[3] = ((float[]) data[0])[3];
            newVertex[4] = ((float[]) data[0])[4];
            outData = new Object[]{newVertex};

		}
	}
}
