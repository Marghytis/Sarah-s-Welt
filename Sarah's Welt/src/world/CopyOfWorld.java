package world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.Quad;
import world.CopyOfWorld.Field.Line;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import world.Material;

public class CopyOfWorld{
	
	public static Field[][] fields = new Field[3][3];
	public static int fieldSize = 50;
	public static int fX, fY;
	public static Field field;
	public static String name = "Test";

	public static void tick(int delta){
		Player.tick(delta);
		if((int)Player.X/fieldSize != fX || (int)Player.Y/fieldSize != fY){
			updateFields(((int)Player.X/fieldSize) - fX, ((int)Player.Y/fieldSize) - fY);
		}
	}
	
	public static void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0, 0, 0);
		for(Field[] fds : fields){
			for(Field f : fds){
				for(Line l : f.lines){
					GL11.glBegin(GL11.GL_LINE_STRIP);
						for(Vector2f v: l.vertices){
							GL11.glVertex2f(v.x, v.y);
						}
					GL11.glEnd();
				}
			}
		}
		
		(new Quad(Player.X-10, Player.Y-10, 20, 20)).draw();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void refresh(){
		for(int x = 0; x < fields.length; x++){
			for(int y = 0; y < fields.length; y++){
				fields[x][y] = new Field(fX + x-1, fY + y-1);
			}
		}
	}
	
	public static void updateFields(int xChange, int yChange){
		if(Math.abs(xChange) > 2 || Math.abs(yChange) > 2){
			fX += xChange;
			fY += yChange;
			refresh();
		} else {
			while(xChange > 0){
				goRight();
				xChange--;
			}
			while(xChange < 0){
				goLeft();
				xChange++;
			}
			while(yChange > 0){
				goUp();
				yChange--;
			}
			while(yChange < 0){
				goDown();
				yChange++;
			}
		}
	}
	
	public static void goRight(){
		for(int y = 0; y < 3; y++){
//			fields[0][y].save();
			fields[0][y] = fields[1][y];
			fields[1][y] = fields[2][y];
			fields[2][y] = new Field(fX + 2, fY + y-1);
		}
		fX++;
	}
	
	public static void goLeft(){
		for(int y = 0; y < 3; y++){
//			fields[2][y].save();
			fields[2][y] = fields[1][y];
			fields[1][y] = fields[0][y];
			fields[0][y] = new Field(fX - 2, fY + y-1);
		}
		fX--;
	}
	
	public static void goUp(){
		for(int x = 0; x < 3; x++){
//			fields[x][0].save();
			fields[x][0] = fields[x][1];
			fields[x][1] = fields[x][2];
			fields[x][2] = new Field(fX + x-1, fY + 2);
		}
		fY++;
	}
	
	public static void goDown(){
		for(int x = 0; x < 3; x++){
//			fields[x][2].save();
			fields[x][2] = fields[x][1];
			fields[x][1] = fields[x][0];
			fields[x][0] = new Field(fX + x-1, fY - 2);
		}
		fY--;
	}
	
	public static class Field{
		int x, y;
		public Line[] lines;
		
		public Field(int x, int y){
			this.x = x;
			this.y = y;
			//either read one or generate one
			if(!read()) generate();
		}
		
		public void generate(){
			lines = new Line[4];
			lines[0] = new Line(Material.GRASS, fieldSize/10 + 1);
			lines[1] = new Line(Material.EARTH, fieldSize/10 + 1);
			lines[2] = new Line(Material.STONE, fieldSize/10 + 1);
			int i = 0;
			for(float x = this.x*fieldSize; x <= this.x*fieldSize + fieldSize; x += 10, i++){
				lines[0].vertices[i] = new Vector2f(x, yAt(x/20));
				lines[1].vertices[i] = new Vector2f(x, yAt(x/20) - 50);
				lines[2].vertices[i] = new Vector2f(x, yAt(x/20) - 100);
			}
			lines[3] = new Line(Material.GRASS, 4);
			lines[3].vertices[0] = new Vector2f(fieldSize*x, fieldSize*y);
			lines[3].vertices[1] = new Vector2f(fieldSize*(x + 1), fieldSize*y);
			lines[3].vertices[2] = new Vector2f(fieldSize*(x + 1), fieldSize*(y + 1));
			lines[3].vertices[3] = new Vector2f(fieldSize*x, fieldSize*(y + 1));
		}
		
		public float yAt(float x){
			return 100*(float)Math.sin(x) + 300;
		}
		
		/**
		 * Reads the field out of a File
		 * @return if the operation was successful
		 */
		public boolean read(){
			File f = new File("worlds/" + name + "/" + x + "_" + y + ".field");
			if(!f.exists())return false;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				int lineCount = Integer.parseInt(reader.readLine());
				
				lines = new Line[lineCount];
				
				for(int line = 0; line < lineCount; line++){
					String material = ""; for(int c = 0; (c = reader.read()) != ' ';material += (char)c);
					
					String[] vertices = reader.readLine().split(";");
					lines[line] = new Line(Material.valueOf(material), vertices.length/2);
					
					for(int i = 0; i < vertices.length; i+=2){
						lines[line].vertices[i/2] = new Vector2f(Float.parseFloat(vertices[i]), Float.parseFloat(vertices[i+1]));
					}
				}
				
				reader.close();
				return true;
			} catch (IOException e){
				e.printStackTrace();
				return false;
			}
		}
		
		public Field save(){
			(new File("worlds/" + name)).mkdirs();
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("worlds/" + name + "/" + x + "_" + y + ".field")));
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
			return this;
		}
		
		public class Line{
			public Vector2f[] vertices;
			Material mat;
			boolean closed; //Is that needed?????????????
			
			public Line(){
				this(null, 0);
			}
			
			public Line(Material mat, int size){
				this.mat = mat;
				vertices = new Vector2f[size];
			}
		}
	}
}
