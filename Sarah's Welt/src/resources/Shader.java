package resources;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.Game;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;

public enum Shader {
	SMOOTH("LightingShader.frag"), POCKET_LAMP("LightingShader2.frag"), BRIGHT("AngelShader.frag"){
		public void drawLight(float... data){
			ARBShaderObjects.glUseProgramObjectARB(handle);
				ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(handle, "lightLocation"), data[0], data[1]);
				ARBShaderObjects.glUniform3fARB(glGetUniformLocationARB(handle, "lightColor"), data[2], data[3], data[4]);
				Game.window.fill();
			glUseProgramObjectARB(0);
		}
	};
	
	public int handle;
	
	Shader(String srcName){
		setup(srcName);
	}
	
	public void drawLight(float... data){
		ARBShaderObjects.glUseProgramObjectARB(handle);
			ARBShaderObjects.glUniform2fARB(glGetUniformLocationARB(handle, "lightLocation"), data[0], data[1]);
			ARBShaderObjects.glUniform1fARB(glGetUniformLocationARB(handle, "lightStrength"), data[2]);
			Game.window.fill();
		glUseProgramObjectARB(0);
	}
	
	public void setup(String name){
		//create shaders
		int fShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		
		//add Source and compile shaders
		try {
			ARBShaderObjects.glShaderSourceARB(fShader, readFile("res/shader/" + name));
			ARBShaderObjects.glCompileShaderARB(fShader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//create program
		int program = ARBShaderObjects.glCreateProgramObjectARB();

		//add shaders to program
		ARBShaderObjects.glAttachObjectARB(program, fShader);
		
		//link program
		ARBShaderObjects.glLinkProgramARB(program);
		
		handle = program;
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
