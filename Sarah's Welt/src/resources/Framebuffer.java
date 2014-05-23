package resources;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import core.Window;

public class Framebuffer {

	
	public int handle;
	public TextureFile textureFile;
	
	public void bind(){
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, handle);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public void release(){
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
	}
	
	/**
	 * Draws the (hopefully) updated frame buffer on the screen
	 */
	public void draw(){
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
		Window.fill(textureFile.handle);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public Framebuffer(TextureFile tex){
		//create frame buffer object
		handle = EXTFramebufferObject.glGenFramebuffersEXT();
		
		//create texture
		textureFile = tex;
		textureFile.set(GL11.glGenTextures(), textureFile.width, textureFile.height);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureFile.handle);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,  textureFile.width, textureFile.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		//create render buffer
		int rbo = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, rbo);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL11.GL_DEPTH_COMPONENT, textureFile.width, textureFile.height);
		
		//bind texture to frame buffer
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, handle);
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, rbo);
			EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, textureFile.handle, 0);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

		//check for errors
		check();
	}

	public void check(){
		int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT ); 
		switch ( framebuffer ) {
			case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
				break;
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
			case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
				throw new RuntimeException( "FrameBuffer: " + handle
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
			default:
				throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
		}
	}

}
