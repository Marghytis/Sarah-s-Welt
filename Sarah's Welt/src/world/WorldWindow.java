package world;

import java.util.ArrayList;
import java.util.List;

import main.Window;

import org.lwjgl.opengl.GL11;

import resources.Texture;
import util.Tessellator;
import world.WorldGenerator.Sector;

public class WorldWindow {
		/**The scale factor from meters to pixel*/
		public static final int measureScale = 50;
		public static Tessellator tessellator = new Tessellator();
		
		public int xSector;
		public WorldGenerator generator = new WorldGenerator();
		
		@SuppressWarnings("unchecked")
		public List<Line>[] lines = (List<Line>[]) new ArrayList<?>[Material.values().length];// Array of Lines for each Material
		
		public Sector[] sectors = new Sector[3];

		public Character character;
		public String worldName;
	
		public WorldWindow(String worldName){
			this.worldName = worldName;
			for(int i = 0; i < lines.length; i++) lines[i] = new ArrayList<>();
			if(!load()){
				create();
				sectors[0] = generator.generateLeft();
				sectors[1] = generator.generateRight();
				sectors[2] = generator.generateRight();
			}
//			loadPosition((int)(character.pos.x/Sector.WIDTH) - (character.pos.x < 0 ? 1 : 0));
		}
		
		public void plugSectorRight(Sector sec, Sector plug){
			for(int n = 0; n < sec.openEndingsRight.length; n++){
				if(sec.inOutRight[n]){
					sec.openEndingsRight[n].last = plug.openEndingsLeft[n];
					plug.openEndingsLeft[n].next = sec.openEndingsRight[n];
				} else {
					sec.openEndingsRight[n].next = plug.openEndingsLeft[n];
					plug.openEndingsLeft[n].last = sec.openEndingsRight[n];
				}
			}
		}
		
		public void plugSectorLeft(Sector sec, Sector plug){
			for(int n = 0; n < sec.openEndingsRight.length; n++){
				if(sec.inOutRight[n]){
					sec.openEndingsLeft[n].last = plug.openEndingsRight[n];
					plug.openEndingsRight[n].next = sec.openEndingsLeft[n];
				} else {
					sec.openEndingsLeft[n].next = plug.openEndingsRight[n];
					plug.openEndingsRight[n].last = sec.openEndingsLeft[n];
				}
			}
		}
		
		/**
		 * Load this world from the world file
		 * @return if the world file exists
		 */
		private boolean load(){
			//TODO Evelyn? add method code
//			+set the position of the world view

//			File f = new File("worlds/" + name + "/" + x + ".field"); 
//			if(!f.exists())return false;	so kannst du testen, ob eine Datei vorhanden ist
			return false;
		}
		
		private void create(){
			character = new Character(10, 340);
		}
		
		public void tick(float dTime){
			character.tick(dTime);
			int playerX = (int)(character.pos.x/Sector.WIDTH) - (character.pos.x < 0 ? 1 : 0);
			if(playerX != xSector){
				if(playerX == xSector - 1){
					step(false);
				} else if (playerX == xSector + 1){
					step(true);
				} else {
					loadPosition(playerX);
				}
			}
		}
		
		public void mouseListening(){
			//add later
		}
		
		public void render(){
			GL11.glLoadIdentity();
			GL11.glTranslatef(- character.pos.x + (Window.WIDTH/2.0f), - character.pos.y + (Window.HEIGHT/2.0f), 0);
			GL11.glColor3f(0, 0, 0);

			for(int mat = 1; mat < Material.values().length; mat++){
				
				GL11.glColor4f(1, 1, 1, 1);
				
				Texture tex = Material.values()[mat].texture;
				tex.bind();
				{
					tessellator.tessellate(lines[mat-1], tex.width, tex.height);
				}
				tex.release();
			}
	
			character.render();
		}
		
		public void refresh(){
			
		}
		
		public void step(boolean rightwards){
			//if !load(x)
			if(rightwards){
				//expand rightwards
				//move rim etc.
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				sectors[2] = generator.generateRight();
				plugSectorRight(sectors[1], sectors[2]);
			} else {
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				sectors[0] = generator.generateLeft();
				plugSectorLeft(sectors[1], sectors[0]);
			}
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
		
		public void loadPosition(int x){
			xSector = x;
			if(sectors[0] != null) sectors[0].save();
			if(sectors[1] != null) sectors[1].save();
			if(sectors[2] != null) sectors[2].save();
			sectors[0] = sectorAt(xSector - 1); lines = sectors[0].lines;
			sectors[1] = sectorAt(xSector); plugSectorRight(sectors[0], sectors[1]);
			sectors[2] = sectorAt(xSector + 1); plugSectorRight(sectors[1], sectors[2]);
		}
		
		public void save(){
			//save sectors TODO save character??
			sectors[0].save();
			sectors[1].save();
			sectors[2].save();
		}
	
	
}
