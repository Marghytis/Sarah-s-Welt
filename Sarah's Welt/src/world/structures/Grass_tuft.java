package world.structures;

import org.lwjgl.opengl.GL11;

import resources.StackedTexture;
import world.Point;
import world.Structure;

public class Grass_tuft extends Structure{

	public Grass_tuft(Point pos){
		super(StructureType.GRASS_TUFT, pos);
		showInFront = true;
	}
	
	int waveF = 0;
	int[] wave = {0, 1, 2, 3, 2, 1};
	
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		type.box.draw((StackedTexture)type.tex, wave[waveF/14], 0);
		
		waveF++;
		if(waveF/14 >= wave.length) waveF = 0;
		
		GL11.glPopMatrix();
	}
	
}
