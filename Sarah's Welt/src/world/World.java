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
	
	public String name = "Empty";
	public Character player;

	public Point leftRimP, rightRimP;
	public int leftRim, rightRim;
	public WorldView view;
	public boolean isActive = false;
	
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

//		File f = new File("worlds/" + name + "/" + x + ".field"); 
//		if(!f.exists())return false;	so kannst du testen, ob eine Datei vorhanden ist
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
	
	public Sector sectorAt(int pos){
		//TODO call the sector from the database
		//if its outside the rims, generate fresh terrain
		return null;
	}
	
	public class WorldView { 
		
		public Sector[] sectors = new Sector[3];
		
		public int xSector;
		public float offsetX;
		public float offsetY;
		
		public void tick(float dTime){
			player.tick(dTime);
			int playerX = (int)(player.pos.x/Sector.WIDTH) - (player.pos.x < 0 ? 1 : 0);
			if(playerX != xSector){
				if(playerX == xSector - 1){
					step(-1);
				} else if (playerX == xSector + 1){
					step(-1);
				}
				goTo(playerX);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				save();
				isActive = false;
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
		
		public void step(int one_or_minus_one){
			if(one_or_minus_one == 1){
				xSector++;
				sectors[0].save();
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				sectors[2] = sectorAt(xSector + 1);
			} else if(one_or_minus_one == -1){
				xSector--;
				sectors[2].save();
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				sectors[0] = sectorAt(xSector - 1);
			} else {
				System.out.println("ERROR: Method 'step(int one_or_minus_one)' in class 'WorldView'");
			}
		}
		
		public void goTo(int x){
			xSector = x;
			sectors[0].save();
			sectors[1].save();
			sectors[2].save();
			sectors[0] = sectorAt(x - 1);
			sectors[1] = sectorAt(x);
			sectors[2] = sectorAt(x + 1);
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
