package world;

import main.Window;

import org.lwjgl.opengl.GL11;

import resources.Texture;
import util.Tessellator;
import world.creatures.Creature;

public class WorldWindow {
		/**The scale factor from meters to pixel*/
		public static final int measureScale = 50;
		public static Tessellator tessellator;
		
		public static String worldName;
		
		public static int xSector;
		public static WorldGenerator generator;
//		public static WorldDatabase database;
		public static Character character;
		
		public static Sector[] sectors = new Sector[3];
	
		public static void load(String worldName){
			WorldWindow.worldName = worldName;
			tessellator = new Tessellator();

//			database = new WorldDatabase(worldName);
//			database.loadWorld();
			load();
		}
		
		private static void load(){
			character = new Character(10, 340);
			generator = new WorldGenerator();//already creates the first 3 sectors
			
			sectors[0].switchConnection(sectors[1], true);
			sectors[1].switchConnection(sectors[2], true);
		}
		
		public static void tick(float dTime){
			character.tick(dTime);
			int playerX = (int)(character.pos.x/Sector.WIDTH) - (character.pos.x < 0 ? 1 : 0);
			if(playerX != xSector){
				if(playerX == xSector - 1){
					step(false);
				} else if (playerX == xSector + 1){
					step(true);
				} else {
					//What??
				}
			}
			for(Sector sec : sectors){
				if(sec != null) {
					for(Structure s : sec.structures){
						s.tick(dTime);
					}
					for(Creature c : sec.creatures){
						c.tick(dTime);
					}
				}
			}
		}
		
		public static void mouseListening(){
			//add later
		}
		
		public static void render(){
			GL11.glLoadIdentity();
			GL11.glTranslatef(- character.pos.x + (Window.WIDTH/2.0f), - character.pos.y + (Window.HEIGHT/2.0f), 0);
			GL11.glColor4f(1, 1, 1, 1);
			
			for(Sector sec : sectors){
				if(sec != null) {
					for(Structure s : sec.structures){
						if(!s.showInFront)s.render();
					}
					for(int mat = 0; mat < Material.values().length; mat++){
						
						Texture tex = Material.values()[mat].texture;
						tex.bind();
						{
							tessellator.tessellateOneNode(sec.areas[mat].cycles, tex.width, tex.height);
						}
						tex.release();
					}
				}
			}

			for(Sector sec : sectors){
				for(Structure s : sec.structures){
					if(s.showInFront)s.render();
				}
				for(Creature c : sec.creatures){
					c.render();
				}
			}
			
//			for(Sector sec: sectors){
//				for(int mat = 1; mat < Material.values().length; mat++){
//					GL11.glColor4f(1, 1, 1, 1);
//					
//					Texture tex = Material.values()[mat].texture;
//					tex.bind();
//					{
//						tessellator.tessellate(sec.lines[mat], tex.width, tex.height);
//					}
//					tex.release();
//				}
//			}
//	
			character.render();
		}
				
		public static void step(boolean rightwards){
			//if !load(x)
			if(rightwards){
				//expand rightwards
				//move rim etc.
				xSector++;
				sectors[0].switchConnection(sectors[1], true);
				sectors[0] = sectors[1];
				sectors[1] = sectors[2];
				if(xSector > generator.rimR-2){
					sectors[2] = generator.generateRight();//TODO or load
//				}
				sectors[1].switchConnection(sectors[2], true);
				}
			} else {
				xSector--;
				sectors[1].switchConnection(sectors[2], true);
				sectors[2] = sectors[1];
				sectors[1] = sectors[0];
				if(xSector < generator.rimL+1){
				sectors[0] = generator.generateLeft();//TODO or load
//				}
				sectors[0].switchConnection(sectors[1], true);
				} else {
//					database.loadSectorAt();TODO
				}
			}
		}
}
