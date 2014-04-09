package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

public class Texture {

	public static final Texture TITEL = new Texture("Titel");
	public static final Texture MENU_LIGHTMAP = new Texture("");
	public static final StackedTexture MENU_BUTTON = new StackedTexture("Button", 1, 2);
	
	public int handle;
	public int width, height;
	
	public Texture(String name){
		if(name.equals("") || name.equals(null)){
			handle = 0;
		} else {
			ByteBuffer buf = null;
			try {
				// Open the PNG file as an InputStream
				InputStream in = new FileInputStream("res/" + name + ".png");
				// Link the PNG decoder to this stream
				PNGDecoder decoder = new PNGDecoder(in);
				
				// Get the width and height of the texture
				width = decoder.getWidth();
				height = decoder.getHeight();
				
				
				// Decode the PNG file in a ByteBuffer
				buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
				decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
				buf.flip();
				
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Create a new texture object in memory and bind it
			handle = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
			
			// All RGB bytes are aligned to each other and each component is 1 byte
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			// Upload the texture data and generate mip maps (for scaling)
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
					
			// Setup what to do when the texture has to be scaled
	//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}
	}
	
	public void set(int handle, int width, int height){
		this.handle = handle;
		this.width = width;
		this.height = height;
	}
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
	}
	
	public void release(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
}
