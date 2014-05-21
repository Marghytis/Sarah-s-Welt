package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL20;

public enum Shader20 {
	UNICORN("res/shader/Unicorn.frag");
	
	public void bind(){
		GL20.glUseProgram(handle);
	}
	
	public void release(){
		GL20.glUseProgram(0);
	}
	
	public static void bindNone(){
		GL20.glUseProgram(0);
	}
	
	public int handle;
	
	Shader20(String srcName){
		//create program
		int program = GL20.glCreateProgram();
		
		//create shader
		int fShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		//add Source and compile shader
		try {
			GL20.glShaderSource(fShader, readFile(srcName));
			GL20.glCompileShader(fShader);
			System.out.println(GL20.glGetShaderInfoLog(fShader, 1000));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//add shader to program and delete the object
		GL20.glAttachShader(program, fShader);
		GL20.glDeleteShader(fShader);
		
		//link program
		GL20.glLinkProgram(program);
		
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
