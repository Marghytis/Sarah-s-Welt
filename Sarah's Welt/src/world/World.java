package world;

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
	public int leftRim = 0, rightRim = -1;
	public WorldView view;
	public boolean isActive = false;
	
	public World(String name){
		this.name = name;
		if(!load()){
			create();
		}
		view = new WorldView();
		view.goTo((int)(player.pos.x/Sector.WIDTH) - (player.pos.x < 0 ? 1 : 0));
	}
	
	/**
	 * Load this world from the world file
	 * @return if the world file exists
	 */
	private boolean load(){
		//TODO Evelyn? add method code
//		+set the position of the world view

//		File f = new File("worlds/" + name + "/" + x + ".field"); 
//		if(!f.exists())return false;	so kannst du testen, ob eine Datei vorhanden ist
		return false;
	}
	
	private void create(){
		player = new Character(10, 340);
		
		leftRimP = new Point(0, 300);
		rightRimP = new Point(0, 300);
	}
	
	/**
	 * if its outside the rims, generate fresh terrain, otherwise load it from the database
	 * @param pos
	 * @return
	 */
	private Sector sectorAt(int pos){
		if(pos > rightRim){
			
			Sector newSector = new Sector(pos);
			rightRimP = newSector.generateRight(rightRimP);
			return newSector;
			
		} else if(pos < leftRim){
			
			Sector newSector = new Sector(pos);
			leftRimP = newSector.generateLeft(leftRimP);
			return newSector;
			
		} else {
			//TODO call the sector from the database
			return null;//for now
		}
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
					step(false);
				} else if (playerX == xSector + 1){
					step(true);
				} else {
					goTo(playerX);
				}
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
		
		public void refresh(){
			
		}
		
		public void step(boolean rightwards){
			System.out.println("Step");
			if(rightwards){
				xSector++;
				sectors[0].save();
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				sectors[2] = sectorAt(xSector + 1);
			} else {
				xSector--;
				sectors[2].save();
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				sectors[0] = sectorAt(xSector - 1);
			}
		}
		
		public void goTo(int x){
			xSector = x;
			if(sectors[0] != null) sectors[0].save();
			if(sectors[1] != null) sectors[1].save();
			if(sectors[2] != null) sectors[2].save();
			sectors[0] = sectorAt(xSector - 1);
			sectors[1] = sectorAt(xSector);
			sectors[2] = sectorAt(xSector + 1);
		}
		
		public void save(){
			//save sectors TODO save player??
			sectors[0].save();
			sectors[1].save();
			sectors[2].save();
		}
	}
	
}
