package util;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;

import com.sun.scenario.effect.impl.BufferUtil;

public class G {

	public static int createVBO(float[] data){

		FloatBuffer buffer = BufferUtil.newFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		int handle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return handle;
	}
	
}
