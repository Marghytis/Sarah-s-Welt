package resources;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;

import sound.Sound;

public class Res {

	public static StackedTexture CLOUD = new StackedTexture("structures/Cloud", 1, 1, -0.5f, -0.5f);
	public static StackedTexture TREE = new StackedTexture("structures/Tree", 1, 3, -0.5f, -0.3f);
	public static StackedTexture PALM_TREE = new StackedTexture("structures/PalmTree", 1, 3, -0.5f, -0.03f);
	public static StackedTexture CANDY_TREE = new StackedTexture("structures/CandyTree", 1, 1, -0.5f, -0.2f);
	public static StackedTexture BAMBOO = new StackedTexture("structures/Bamboo", 1, 4, -0.5f, -0.02f);
	public static StackedTexture BUSH = new StackedTexture("structures/Bush", 1, 2, -0.5f, -0.02f);
	public static StackedTexture CANDY_BUSH = new StackedTexture("structures/CandyBush", 1, 2, -0.5f, -0.2f);
	public static StackedTexture CACTUS = new StackedTexture("structures/Cactus", 1, 3, -0.5f, -0.05f);
	public static StackedTexture GRASS_TUFT = new StackedTexture("structures/Grass_tuft", 4, 1, -0.5f, -0.2f);
	public static StackedTexture FLOWER = new StackedTexture("structures/Flower", 1, 3, -0.5f, 0f);
	public static StackedTexture CANDY_FLOWER = new StackedTexture("structures/Candy", 1, 6, -0.5f, 0f);
	public static StackedTexture CRACK = new StackedTexture("structures/Crack", 1, 4, -0.5f, -0.5f);
	
	public static StackedTexture SNAIL  = new StackedTexture("creatures/Snail", 7, 3, -0.5f, -0.1f);
	public static StackedTexture BUTTERFLY  = new StackedTexture("creatures/Butterfly", 5, 2, -0.5f, -0.5f);
	public static StackedTexture HEART = new StackedTexture("creatures/Heart", 4, 1, -0.5f, -0.2f);
	public static StackedTexture RABBIT  = new StackedTexture("creatures/Rabbit", 5, 3, -0.5f, -0.2f);
	public static StackedTexture BIRD  = new StackedTexture("creatures/Bird", 5, 2, -0.5f, -0.2f);
	public static StackedTexture PANDA  = new StackedTexture("creatures/Panda", 6, 2, -0.5f, -0.1f);
	public static StackedTexture COW  = new StackedTexture("creatures/Cow", 7, 1, -0.5f, -0.1f);

	public static StackedTexture SARAH = new StackedTexture("creatures/Sarah", 11, 8, -0.5f, -0.1f);
	public static List<int[][]> SARAH_ITEMCOORDS = readTextureCoordinator("res/creatures/Sarah.txt");
	public static StackedTexture SARAH_ON_COW = new StackedTexture("creatures/Sarah_riding_cow", 7, 2, -0.5f, -0.1f);
	public static StackedTexture SARAH_DEATH = new StackedTexture("creatures/Sarah_death", 14, 1, 0, 0);
	
	public static SimpleText font = new SimpleText(new Font("Times New Roman", Font.BOLD, 45), true);//Russel Write TT;
	public static SimpleText arial = new SimpleText(new Font("", Font.BOLD, 45), true);
	
	public static Sound test = new Sound("69569__digifishmusic__syyyymphonica.wav");
	public static Sound buttonSound = new Sound("63531__florian-reinke__click1.wav");
	
	public static void load(){
		//do nothing, classloader loads the resources (I hope)
	}
	
	public static void unload(){
		font.destroy();
		arial.destroy();
		AL10.alDeleteSources(test.source);
	}

	public static List<int[][]> readTextureCoordinator(String file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			String line;
			
			List<int[][]> kA = new ArrayList<>();
			
			while((line = reader.readLine()) != null){
				String[] vertices = line.split(";");
				
				int[][] output = new int[vertices.length][3];
				
				for(int i = 0; i < vertices.length; i++){
					String[] coords = vertices[i].split(",");
					output[i] = new int[]{Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])};
				}
				
				kA.add(output);
			}
			
			reader.close();
			
			return kA;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
