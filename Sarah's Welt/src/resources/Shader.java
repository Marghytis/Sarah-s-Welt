package resources;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;

import core.Window;

public enum Shader {
	Test("Test.frag"), SMOOTH("LightingShader.frag"), POCKET_LAMP("LightingShader2.frag"), BRIGHT("AngelShader.frag"){
		public void drawLight(float... data){
			ARBShaderObjects.glUseProgramObjectARB(handle);
				ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(handle, "lightLocation"), data[0], data[1]);
				ARBShaderObjects.glUniform3fARB(glGetUniformLocationARB(handle, "lightColor"), data[2], data[3], data[4]);
				Window.fill(0);
			glUseProgramObjectARB(0);
		}
	}, PARTICLE("ParticleShader.frag"){
		public void drawLight(float... data){
			ARBShaderObjects.glUseProgramObjectARB(handle);
				ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(handle, "location"), data[0], data[1]);
				ARBShaderObjects.glUniform4fARB(glGetUniformLocationARB(handle, "color"), data[2], data[3], data[4], data[5]);
				ARBShaderObjects.glUniform1fARB(glGetUniformLocationARB(handle, "radius"), data[6]);
				Window.fill(0);
			glUseProgramObjectARB(0);
		}
	};
	
	public void bind(){
		ARBShaderObjects.glUseProgramObjectARB(handle);
	}
	
	public void release(){
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	public int handle;
	
	Shader(String srcName){
		setup(srcName);
	}
	
	public void drawLight(float... data){
		ARBShaderObjects.glUseProgramObjectARB(handle);
			ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(handle, "lightLocation"), data[0], data[1]);
			ARBShaderObjects.glUniform1fARB(glGetUniformLocationARB(handle, "lightStrength"), data[2]);
			Window.fill(0);
		glUseProgramObjectARB(0);
	}
	
	public void setup(String name){
		//create program
		int program = ARBShaderObjects.glCreateProgramObjectARB();
		
		//create shader
		int fShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		
		//add Source and compile shader
		try {
			ARBShaderObjects.glShaderSourceARB(fShader, readFile("res/shader/" + name));glSlang_GetInfoLog(fShader);
			ARBShaderObjects.glCompileShaderARB(fShader);glSlang_GetInfoLog(fShader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//add shader to program and delete the object
		ARBShaderObjects.glAttachObjectARB(program, fShader);
		ARBShaderObjects.glDeleteObjectARB(fShader);
		
		//link program
		ARBShaderObjects.glLinkProgramARB(program);
		
		handle = program;
	}
	
	public void glSlang_GetInfoLog(int shader){
//		System.out.println(ARBShaderObjects.glGetInfoLogARB(shader, 1000));
	}

	public String readFile(String name) throws IOException{
		File f = new File(name);
		FileReader fReader = new FileReader(f);
		BufferedReader reader = new BufferedReader(fReader);
		String line = reader.readLine();
		
		String output = "";
		while(line != null){
			output += line;
			line = reader.readLine();
		}
		
		reader.close();
		return output;
	}
}
