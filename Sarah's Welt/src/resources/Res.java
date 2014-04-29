package resources;

import java.awt.Font;

import org.lwjgl.openal.AL10;

import sound.Sound;

public class Res {

	public static StackedTexture CLOUD = new StackedTexture("structures/Cloud", 1, 1, -0.5f, -0.5f);
	public static StackedTexture TREE = new StackedTexture("structures/Tree", 3, 1, -0.5f, -0.3f);
	public static StackedTexture BAMBOO = new StackedTexture("structures/Bamboo", 1, 4, -0.5f, -0.02f);
	public static StackedTexture BUSH = new StackedTexture("structures/Bush", 2, 1, -0.5f, -0.2f);
	public static StackedTexture GRASS_TUFT = new StackedTexture("structures/Grass_tuft", 4, 1, -0.5f, -0.2f);
	public static StackedTexture FLOWER = new StackedTexture("structures/Flower", 3, 1, -0.5f, 0f);
	
	public static StackedTexture SNAIL  = new StackedTexture("creatures/Snail", 7, 3, -0.5f, -0.1f);
	public static StackedTexture BUTTERFLY  = new StackedTexture("creatures/Butterfly", 5, 2, -0.5f, -0.5f);
	public static StackedTexture HEART = new StackedTexture("creatures/Heart", 4, 1, -0.5f, -0.2f);
	public static StackedTexture RABBIT  = new StackedTexture("creatures/Rabbit", 5, 2, -0.5f, -0.2f);

	public static StackedTexture SARAH = new StackedTexture("creatures/Sarah", 11, 7, -0.5f, -0.1f);
	
	public static SimpleText font = new SimpleText(new Font("Times New Roman", Font.BOLD, 45), true);//Russel Write TT;
	public static SimpleText arial = new SimpleText(new Font("", Font.BOLD, 45), true);;
	public static Sound test = new Sound("69569__digifishmusic__syyyymphonica.wav");;
	
	public static void load(){
		//do nothing, classloader loads the resources (I hope)
	}
	
	public static void unload(){
		font.destroy();
		arial.destroy();
		AL10.alDeleteSources(test.source);
	}
}
