package resources;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.util.ResourceLoader;

public class ResLoader {
	
	public static SimpleText font;
	public static TrueTypeFont font2;

	public static Audio forest;
	public static Audio branch;
	public static Audio wolf;
	public static Audio[] hurt;
	
	public static void load(){
		setupFont();
		try {
			forest = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/forest.wav"));
			wolf = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/wolf.wav"));
			branch = AudioLoader.getAudio("AIF", ResourceLoader.getResourceAsStream("res/astBrechen.aiff"));
			hurt = new Audio[]{
					AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/hurt_1.wav")),
					AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/hurt_2.wav")),
					AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/hurt_3.wav"))
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setupFont(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		String fontPath = "res/albertus mt light.ttf";
//		fontPath = "res/albertus mt light.ttf";
		font = null;

		Font awtFont = new Font("Russel Write TT", Font.BOLD, 45);
		font = new SimpleText(awtFont, true);
		font2 = new TrueTypeFont(awtFont, false);
		// load font from a .ttf file
//		try {
//			InputStream inputStream	= ResourceLoader.getResourceAsStream("res/albertus mt light.ttf");
//			
//			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
//			awtFont2 = awtFont2.deriveFont(24f); // set font size
//			font = new TrueTypeFont(awtFont2, false);
//				
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static int loadPNGTexture(String name) {
		if(name.equals(""))return 0;
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream("res/" + name + ".png");
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);
			
			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();
			
			
			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
			buf.flip();
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
				
		// Setup what to do when the texture has to be scaled
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		return texId;
	}
}
