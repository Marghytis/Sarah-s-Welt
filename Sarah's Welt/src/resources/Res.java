package resources;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sound.Sound;

public class Res {

	//Structures
	public static final StackedTextures CLOUD = new StackedTextures("worldObjects/Cloud", 1, 1, -0.5f, -0.5f);
	public static final StackedTextures TREE = new StackedTextures("worldObjects/Tree", 1, 3, -0.5f, -0.3f);
	public static final StackedTextures PALM_TREE = new StackedTextures("worldObjects/PalmTree", 1, 3, -0.5f, -0.03f);
	public static final StackedTextures CANDY_TREE = new StackedTextures("worldObjects/CandyTree", 1, 1, -0.5f, -0.2f);
	public static final StackedTextures BAMBOO = new StackedTextures("worldObjects/Bamboo", 1, 4, -0.5f, -0.02f);
	public static final StackedTextures BUSH = new StackedTextures("worldObjects/Bush", 1, 2, -0.5f, -0.02f);
	public static final StackedTextures CANDY_BUSH = new StackedTextures("worldObjects/CandyBush", 1, 2, -0.5f, -0.2f);
	public static final StackedTextures CACTUS = new StackedTextures("worldObjects/Cactus", 1, 3, -0.5f, -0.05f);
	public static final StackedTextures GRASS_TUFT = new StackedTextures("worldObjects/Grass_tuft", 4, 1, -0.5f, -0.2f);
	public static final StackedTextures FLOWER = new StackedTextures("worldObjects/Flower", 1, 3, -0.5f, 0f);
	public static final StackedTextures CANDY_FLOWER = new StackedTextures("worldObjects/Candy", 1, 6, -0.5f, 0f);
	public static final StackedTextures CRACK = new StackedTextures("worldObjects/Crack", 1, 4, -0.5f, -0.5f);
	public static final StackedTextures FOSSIL = new StackedTextures("worldObjects/Fossil", 1, 3, -0.5f, -0.5f);

	public static final Texture FLOWER_LIGHT = new Texture("Light_dimmed");
	
	//Creatures
	public static final StackedTextures SNAIL  = new StackedTextures("creatures/Snail", 7, 3, -0.5f, -0.1f);
	public static final StackedTextures BUTTERFLY  = new StackedTextures("creatures/Butterfly", 5, 2, -0.5f, -0.5f);
	public static final StackedTextures HEART = new StackedTextures("creatures/Heart", 4, 2, -0.5f, -0.2f);
	public static final StackedTextures RABBIT  = new StackedTextures("creatures/Rabbit", 5, 3, -0.5f, -0.2f);
	public static final StackedTextures BIRD  = new StackedTextures("creatures/Bird", 5, 2, -0.5f, -0.2f);
	public static final StackedTextures PANDA  = new StackedTextures("creatures/Panda", 6, 2, -0.5f, -0.1f);
	public static final StackedTextures SCORPION  = new StackedTextures("creatures/Scorpion", 7, 2, -0.5f, -0.1f);
	public static final StackedTextures COW  = new StackedTextures("creatures/Cow", 7, 1, -0.5f, -0.1f);
	public static final StackedTextures UNICORN  = new StackedTextures("creatures/Unicorn", 6, 3, -0.5f, -0.1f);
	public static final StackedTextures UNICORN_HAIR  = new StackedTextures("creatures/Unicorn_hair", 6, 3, -0.5f, -0.1f);
	public static final StackedTextures TREX  = new StackedTextures("creatures/Trex", 9, 4, -0.5f, -0.05f);

	public static final StackedTextures SARAH = new StackedTextures("creatures/Sarah", 11, 10, -0.5f, -0.1f);
	public static final List<int[][]> SARAH_ITEMCOORDS = readTextureCoordinator("res/creatures/Sarah.txt");
	public static final StackedTextures SARAH_ON_COW = new StackedTextures("creatures/Sarah_riding_cow", 7, 2, -0.5f, -0.1f);
	public static final StackedTextures SARAH_DEATH = new StackedTextures("creatures/Sarah_death", 14, 1, -0.5f, -0.5f);
	
	public static final StackedTextures MENU_BUTTON = new StackedTextures("Button", 1, 2, -0.5f, -0.5f);
	public static final StackedTextures INVENTORY = new StackedTextures("items/Inventory", 1, 2);
	public static final StackedTextures ITEMS_WORLD = new StackedTextures("items/ItemsWorld", 5, 1, -0.5f, -0.5f);
	public static final StackedTextures ITEMS_HAND = new StackedTextures("items/ItemsHand", 6, 1, -0.5f, -0.5f);
	public static final StackedTextures ITEMS_INV = new StackedTextures("items/ItemsInv", 6, 1, -0.5f, -0.5f);
	public static final StackedTextures ITEMS_WEAPONS = new StackedTextures("items/Weapons", 1, 6, -0.5f, -0.5f);
	
	public static final SimpleText font = new SimpleText(new Font("Times New Roman", Font.BOLD, 45), true);//Russel Write TT;
	public static final SimpleText arial = new SimpleText(new Font("", Font.BOLD, 45), true);
	
	public static final Sound buttonSound = new Sound("63531__florian-reinke__click1.wav");
	public static final Sound death = new Sound("FuneralMarch.wav");
	
	public static void load(){
		//do nothing, classloader loads the resources (I hope)
	}
	
	public static void unload(){
		font.destroy();
		arial.destroy();
//		AL10.alDeleteSources(test.source);
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
