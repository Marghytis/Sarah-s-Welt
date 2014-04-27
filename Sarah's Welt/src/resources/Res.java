package resources;

import java.awt.Font;

import org.lwjgl.openal.AL10;

import sound.Sound;

public class Res {

	public static SimpleText font;
	public static SimpleText arial;
	public static Sound test;
	
	public static void load(){
		font = new SimpleText(new Font("Times New Roman", Font.BOLD, 45), true);//Russel Write TT
		arial = new SimpleText(new Font("", Font.BOLD, 45), true);
		test = new Sound("69569__digifishmusic__syyyymphonica.wav");
	}
	
	public static void unload(){
		font.destroy();
		arial.destroy();
		AL10.alDeleteSources(test.source);
	}
}
