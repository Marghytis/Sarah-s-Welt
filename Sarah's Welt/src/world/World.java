package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Game;
import main.Window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import util.Quad;

public class World{
	/**The scale factor from meters to pixel*/
	public static final int measureScale = 50;
	
	/**The name of this World*/
	public String name = "Empty";
	/**The name of this World*/
	public Character player;

	public Point leftRimP, rightRimP;
	public int leftRim, rightRim;
	public WorldView view;
	
	public World(String name){
		this.name = name;
		view = new WorldView();
		if(!load()){
			create();
		}
	}
	
	/**
	 * Load this world from the world file
	 * @return if the world file exists
	 */
	public boolean load(){
		//TODO Evelyn? add method code
//		+set the position of the world view

//		File f = new File("worlds/" + name + "/" + x + ".field"); so kannst du testen, ob eine Datei vorhanden ist
//		if(!f.exists())return false;
		return false;
	}
	
	public void create(){
		player = new Character(10, 500);
		
		leftRimP = new Point(0, 300);
		rightRimP = leftRimP;
	}
	
	/**
	 * Generates the next sector to the right
	 */
	public Sector generateRight(int pos, Point lastRim){
		//TODO add method code here
//		rightRim = ...;
		return null;
	}
	
	/**
	 * Generates the next sector to the left
	 */
	public Sector generateLeft(int pos, Point lastRim){
		//TODO add method code here
//		leftRim = ...;
		return null;
	}
	
	public class WorldView { 
		
		public Sector[] sectors = new Sector[3];
		
		public int xSector;
		public float offsetX;
		public float offsetY;
		
		public List<Point> allPoints = new ArrayList<>();
		
		public List<Thing> things = new ArrayList<>();//the first thing always has to be the Character
		
		public Random leftRand = new Random(), rightRand = new Random();
		
		public void tick(float dTime){
			player.tick(dTime);
			tosectorsAt((int)(player.pos.x/fieldSize) - (player.pos.x < 0 ? 1 : 0));
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				save();
				Game.inWorld = false;
			}
		}
		
		public void render(){
			GL11.glLoadIdentity();
			GL11.glTranslatef(- player.pos.x + (Window.WIDTH/2.0f), - player.pos.y + (Window.HEIGHT/2.0f), 0);
			GL11.glColor3f(0, 0, 0);
			for(Sector c : sectors){
				c.render();
			}
	
			GL11.glLoadIdentity();
			(new Quad((Window.WIDTH/2)-10, (Window.HEIGHT/2)-10, 20, 20)).outline();
			(new Point((Window.WIDTH/2), (Window.HEIGHT/2))).draw();
		}
		
		public void load(String name){
			//TODO -- Add a file reading function
			WorldView.name = name;
			player = new Player();
			player.pos.set(10, 500);
			player.nextPos.set(10, 500);
			things = new ArrayList<>();
			things.add(player);
			(sectors[0] = new Sector(-1)).generateLeft(new Point(0, 300));
			(sectors[1] = new Sector(0)).generateRight(new Point(0, 300));
			(sectors[2] = new Sector(1)).generateRight(new Point(Sector.sectorWidth, 300));
		}
		
		public void tosectorsAt(int x){
			if(xSector == x) return;
			while(x > rightRim){
				createSector()
				rightRim++;
			}
			while(xSector < x){
				X++;
	//			sectors[0].save();
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				sectors[2] = createSector(xSector + 1);
			}
			while(xSector > x){
				X--;
	//			sectors[2].save();
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				sectors[0] = createSector(xSector - 1);
			}
		}
		
		public Sector createSector(int fX){
			Sector c = new Sector(fX);
			if(fX > rightRim){
				c.generate();//right
				rightRim++;
			} else if(fX < leftRim){
				c.generate();//left
				leftRim--;
			} else {
				c.read();
			}
			return c;
		}
		
		public Sector getSector(int fX){
			for(Sector c : sectors){
				if(c.x == fX){
					return c;
				}
			}
			return createSector(fX);
		}
		
		public void removeSector(int fX){
			//TODO
			for(int i = 0; i < lines.get(0).vertices.size() && lines.get(0).vertices.get(i).x < fX*fieldSize; i++){
				lines.get(fX).vertices.get(i).remove();
				i--;
			}
		}
		
		public void save(){
			//save sectors TODO
		}
	}
	
}
