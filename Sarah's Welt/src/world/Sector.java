package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import resources.Texture;
import util.Quad;
import util.Tessellator;

public class Sector{
	/**The width of one sector, always the same*/
	public static final int WIDTH = 1000;
	public static Tessellator tessellator = new Tessellator();
	Texture cloud = new Texture("Cloud");
	
	public Random random;
	
	public int randomnr(int min , int max){
		int n = max - min + 1;
		int i = min + random.nextInt(n);
		return i;
	}
	
	int x;
	
	@SuppressWarnings("unchecked")
	public List<Line>[] lines = (List<Line>[]) new ArrayList<?>[Material.values().length];// Array of Lines for each Material
	
	public Sector(int x){
		this.x = x;
		random = new Random();
		for(int i = 0; i < lines.length; i++) lines[i] = new ArrayList<>();

		
		int breite = randomnr(130, 250);
		int hoehe = breite - randomnr(-20, 80);
		int cx = randomnr(x*Sector.WIDTH, (x+1)*Sector.WIDTH);
		int cy = randomnr(330, 500);
		
		quad = new Quad(cx, cy, breite, hoehe);
	}
	

	Quad quad;
	
	public void render(){
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(x*WIDTH, -1000);
		GL11.glVertex2f(x*WIDTH, 1000);
		GL11.glEnd();
		
		
		quad.draw(cloud); 
		
		
		for(int mat = 1; mat < Material.values().length; mat++){
			GL11.glColor4f(1, 1, 1, 1);
			Texture tex = Material.values()[mat].texture;
			tex.bind();
			tessellator.tessellate(lines[mat-1], tex.width, tex.height);
			tex.release();
		}
	}
	
	public Point generateRight(Point lastPoint){
		//create the base outline of the world
		Line base = new Line();
		base.addPoints(new Point(lastPoint));
		float segmentLength = 20;
		

//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs themselves too! :D)
		while(lastPoint.x <= (x+1)*WIDTH){
			float dx = 16 + (random.nextInt(5));
			float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);

			lastPoint.add(dx, dy);
			base.addPoints(new Point(lastPoint));
		}

		//create grass, earth and stone with base line
		Line bottom = new Line();
		Line stone = new Line();
		Line earth = new Line();
		Line soil = new Line();
		Line grass = new Line();
		
		
		Node n = base.end;
		
		while(n.last != null) {
//			if(n.p.x - n.last.p.x > 0){//TODO
			grass.addPoints(n.p);
			soil.addPoints(n.p.x, n.p.y - 10);
			earth.addPoints(n.p.x, n.p.y - 15);
			stone.addPoints(n.p.x, n.p.y - 100);
			bottom.addPoints(n.p.x, n.p.y - 1000);
			
//			}
			n = n.last;
		}
		grass.addPoints(n.p);
		soil.addPoints(n.p.x, n.p.y - 10);
		earth.addPoints(n.p.x, n.p.y - 15);
		stone.addPoints(n.p.x, n.p.y - 100);
		bottom.addPoints(n.p.x, n.p.y - 1000);
		

		//finalize the lines by adding the way back and closing each to a circle
		grass.appendLine(soil, true);
		grass.closeCircle();
		soil.appendLine(earth, true);
		soil.closeCircle();
		earth.appendLine(stone, true);
		earth.closeCircle();
//		stone.addPoints(new Point(this.x*WIDTH, 0), new Point(this.x*WIDTH + WIDTH, 0));
		stone.appendLine(bottom, true);
		stone.closeCircle();
		
		lines[2].add(grass);
		lines[3].add(soil);
		lines[1].add(earth);
		lines[0].add(stone);

		return lastPoint;
	}
	
	public Point generateLeft(Point lastPoint){
		//create the base outline of the world
		Line base = new Line();
		base.addPoints(new Point(lastPoint));
		float segmentLength = 20;
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs too! :D)
		while(lastPoint.x >= x*WIDTH){
			float dx = -18 - (random.nextInt(3));
		float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);

			lastPoint.add(dx, dy);
//			if(lastPoint.x >= x*WIDTH){
			base.addPoints(new Point(lastPoint));
//			}
		}

		//create grass, earth and stone with base line
		Line bottom = new Line();
		Line stone = new Line();
		Line earth = new Line();
		Line soil = new Line();
		Line grass = new Line();
		
		Node n = base.start;
		
		while(n.next != null) {
//			if(n.p.x - n.next.p.x > 0){//TODO re-add this
				grass.addPoints(n.p);
				soil.addPoints(n.p.x, n.p.y - 10);
				earth.addPoints(n.p.x, n.p.y - 15);
				stone.addPoints(n.p.x, n.p.y - 100);
				bottom.addPoints(n.p.x, n.p.y - 1000);
//			} else {
//				stone.addPoints(n.p.x, n.p.y);
//			}
			n = n.next;
		}
		grass.addPoints(n.p);
		soil.addPoints(n.p.x, n.p.y - 10);
		earth.addPoints(n.p.x, n.p.y - 15);
		stone.addPoints(n.p.x, n.p.y - 100);
		bottom.addPoints(n.p.x, n.p.y - 1000);

		//finalize the lines by adding the way back and closing each to a circle
		grass.appendLine(soil, true);
		grass.closeCircle();
		soil.appendLine(earth, true);
		soil.closeCircle();
		earth.appendLine(stone, true);
		earth.closeCircle();
		stone.appendLine(bottom, true);
		stone.closeCircle();
		
		lines[2].add(grass);
		lines[3].add(soil);
		lines[1].add(earth);
		lines[0].add(stone);

		return lastPoint;
	}

	/**
	 * Reads the field out of a File
	 * @return if the operation was successful
	 */
	public boolean read(){
		//TODO
		return false;
//		File f = new File("worlds/" + name + "/" + x + ".field");
//		if(!f.exists())return false;
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(f));
//			int lineCount = Integer.parseInt(reader.readLine());
//			
//			lines = new Line[lineCount];
//			
//			for(int line = 0; line < lineCount; line++){
//				String material = ""; for(int c = 0; (c = reader.read()) != ' ';material += (char)c);
//				
//				String[] vertices = reader.readLine().split(";");
//				lines[line] = new Line(Material.valueOf(material), vertices.length/2);
//				
//				for(int i = 0; i < vertices.length; i+=2){
//					lines[line].vertices[i/2] = new Vector2f(Float.parseFloat(vertices[i]), Float.parseFloat(vertices[i+1]));
//				}
//			}
//			
//			reader.close();
//			return true;
//		} catch (IOException e){
//			e.printStackTrace();
//			return false;
//		}
	}
	
	public void save(){
		//TODO
//		(new File("worlds/" + name)).mkdirs();
//		try{
//			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("worlds/" + name + "/" + x + ".field")));
//			writer.write(lines.length + ""); writer.newLine();
//			
//			for(int i = 0; i < lines.length; i++){
//				writer.write(lines[i].mat.name.toUpperCase() + " ");
//				
//				for(int v = 0; v < lines[i].vertices.length; v++){
//					if(v > 0)writer.write("; ");
//					writer.write(lines[i].vertices[v].x + ";" + lines[i].vertices[v].y);
//				}
//				writer.newLine();
//			}
//			
//			writer.close();
//		} catch(IOException e){
//			e.printStackTrace();
//		}
	}
}