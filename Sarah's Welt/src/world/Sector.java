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
	public static final int WIDTH = 200;
	public MatArea[] areas = new MatArea[Material.values().length];
	
	public List<Connection> connsR = new ArrayList<>(); public int indexR = 0;
	public List<Connection> connsL = new ArrayList<>(); public int indexL = 0;
	
	Texture cloud = new Texture("Cloud");
	
	public Sector(){
		for(int i = 0; i < Material.values().length; i++){
			areas[i] = new MatArea();
		}
	}
	
	public void switchConnection(Sector other, boolean right){
		if(right){
			for(Connection c : connsR){
				c.switchConnection();
			}
		} else {
			for(Connection c : connsL){
				c.switchConnection();
			}
		}
	}
	
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
}
