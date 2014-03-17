package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Game;
import main.Window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import util.Quad;

public class WorldView{
	
	public static final int fieldSize = 2000;
	public static final int measureScale = 50;
	
	public static Sector[] Sectors = new Sector[3];
	
	public static int X;
	public static float offsetX;
	public static float offsetY;
	public static String name = "Test";
	
	public static List<Point> allPoints = new ArrayList<>();
	
	public static List<Thing> things = new ArrayList<>();//the first thing always has to be the Player
	public static Player player;
	
	public static int leftRim, rightRim;
	public static Random leftRand = new Random(), rightRand = new Random();
	
	public static void tick(float dTime){
		player.tick(dTime);
		toSectorsAt((int)(player.pos.x/fieldSize) - (player.pos.x < 0 ? 1 : 0));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			save();
			Game.inWorld = false;
		}
	}
	
	public static void render(){
		GL11.glLoadIdentity();
		GL11.glTranslatef(- player.pos.x + (Window.WIDTH/2.0f), - player.pos.y + (Window.HEIGHT/2.0f), 0);
		GL11.glColor3f(0, 0, 0);
		for(Sector c : Sectors){
			c.render();
		}

		GL11.glLoadIdentity();
		(new Quad((Window.WIDTH/2)-10, (Window.HEIGHT/2)-10, 20, 20)).outline();
		(new Point((Window.WIDTH/2), (Window.HEIGHT/2))).draw();
	}
	
	public static void load(String name){
		//TODO -- Add a file reading function
		World.name = name;
		player = new Player();
		player.pos.set(10, 500);
		player.nextPos.set(10, 500);
		things = new ArrayList<>();
		things.add(player);
		(Sectors[0] = new Sector(-1)).generateLeft(new Point(0, 300));
		(Sectors[1] = new Sector(0)).generateRight(new Point(0, 300));
		(Sectors[2] = new Sector(1)).generateRight(new Point(Sector.SectorWidth, 300));
	}
	
	public static void toSectorsAt(int x){
		if(X == x) return;
		while(x > rightRim){
			createSector()
			rightRim++;
		}
		while(X < x){
			X++;
//			Sectors[0].save();
			Sectors[0] = Sectors[1];
			Sectors[1] = Sectors[2];
			Sectors[2] = createSector(X + 1);
		}
		while(X > x){
			X--;
//			Sectors[2].save();
			Sectors[2] = Sectors[1];
			Sectors[1] = Sectors[0];
			Sectors[0] = createSector(X - 1);
		}
	}
	
	public static Sector createSector(int fX){
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
	
	public static Sector getSector(int fX){
		for(Sector c : Sectors){
			if(c.x == fX){
				return c;
			}
		}
		return createSector(fX);
	}
	
	public static void removeSector(int fX){
		//TODO
		for(int i = 0; i < lines.get(0).vertices.size() && lines.get(0).vertices.get(i).x < fX*fieldSize; i++){
			lines.get(fX).vertices.get(i).remove();
			i--;
		}
	}
	
	public static void save(){
		//save Sectors TODO
	}
	
}
