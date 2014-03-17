package world;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import resources.Texture;
import util.Tessellator;

public class Sector{
	public static Tessellator tessellator = new Tessellator();
	public static int columnWidth = 1000;
	
	public Random random;
	
	int x;
	public Point generationPoint;//for next Sector
	
	@SuppressWarnings("unchecked")
	public List<Line>[] lines = (List<Line>[]) new ArrayList<?>[Material.values().length];// Array of Lines for each Material
	
	public Sector(int x){
		this.x = x;
		random = new Random(x*2);
		for(int i = 0; i < lines.length; i++) lines[i] = new ArrayList<>();
	}
	
	public void generate(){
		if(x >= 0){
			generateRight(World.columns[1].generationPoint);
		} else {
			generateLeft(World.columns[1].generationPoint);
		}
	}
	
	public void generateLeft(Point start){
		//create the base outline of the world
		Line base = new Line();
		base.addPoints(start);
		Point point = start;
		float segmentLength = 20;
//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs too! :D)
		while(point.x >= x*columnWidth){
			float dx = -random.nextFloat()*20;
			float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);

			point.add(dx, dy);
			if(point.x >= x*columnWidth){
				base.addPoints(new Point(point));
			}
		}

		//create grass, earth and stone with base line
		generateFromBase(base);

//		System.out.println(base.end.p);
		generationPoint = base.end.p;
	}
	
	public Point generateRight(Point start){
		//create the base outline of the world
		Line base = new Line();
		base.addPoints(start);
		Point point = start;
		float segmentLength = 20;

//		while(base.end.p.x >= (x-0.5f)*columnWidth){ TODO add security for overhangs (+ overhangs too! :D)
		while(point.x <= (x+1)*columnWidth){
			float dx = random.nextFloat()*20;
			float dy = (float)Math.sqrt((segmentLength*segmentLength) - (dx*dx))*(random.nextBoolean() ? 1 : -1);

			point.add(dx, dy);
			if(point.x <= (x+1)*columnWidth){
				base.addPoints(new Point(point));
			}
		}

		//create grass, earth and stone with base line
		generateFromBase(base);

		return base.end.p;
	}
	
	public void generateFromBase(Line base){
		Line stone = new Line();
		Line earth = new Line();
		Line grass = new Line();

		if(x >= World.X){
			Node n = base.end;
			while(n.last != null) {
				if(n.p.x - n.last.p.x > 0){
					grass.addPoints(n.p);
					earth.addPoints(n.p.x, n.p.y - 30);
					stone.addPoints(n.p.x, n.p.y - 100);
				}
				n = n.last;
			}
		} else {
			Node n = base.start;
			
			while(n.next != null) {
				if(n.p.x - n.next.p.x > 0){
					grass.addPoints(n.p);
					earth.addPoints(n.p.x, n.p.y - 30);
					stone.addPoints(n.p.x, n.p.y - 100);
				} else {
					stone.addPoints(n.p.x, n.p.y);
				}
				n = n.next;
			}
		}

		//finalize the lines by adding the way back and closing each to a circle
		grass.appendLine(earth, true); grass.closeCircle();
		earth.appendLine(stone, true); earth.closeCircle();
		stone.addPoints(new Point(this.x*columnWidth, 0), new Point(this.x*columnWidth + columnWidth, 0)); stone.closeCircle();
		lines[2].add(grass);
		lines[1].add(earth);
		lines[0].add(stone);
	}
	
	public void render(){
		for(int mat = 1; mat < Material.values().length; mat++){
			GL11.glColor4f(1, 1, 1, 1);
			Texture tex = Material.values()[mat].texture;
			tex.bind();
			tessellator.tessellate(lines[mat-1], tex.width, tex.height);
			tex.release();
		}
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
		(new File("worlds/" + name)).mkdirs();
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("worlds/" + name + "/" + x + ".field")));
			writer.write(lines.length + ""); writer.newLine();
			
			for(int i = 0; i < lines.length; i++){
				writer.write(lines[i].mat.name.toUpperCase() + " ");
				
				for(int v = 0; v < lines[i].vertices.length; v++){
					if(v > 0)writer.write("; ");
					writer.write(lines[i].vertices[v].x + ";" + lines[i].vertices[v].y);
				}
				writer.newLine();
			}
			
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}