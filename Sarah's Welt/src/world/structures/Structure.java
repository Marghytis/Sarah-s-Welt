package world.structures;

import java.util.function.Consumer;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;
import world.Thing;

public abstract class Structure extends Thing{
	
	public static void updateEveryStructure(int dTime){
		Tree.updateAll(dTime);
		Grass_tuft.updateAll(dTime);
		Flower.updateAll(dTime);
		Cloud.updateAll(dTime);
		Bush.updateAll(dTime);
		Bamboo.updateAll(dTime);
	}
	
	public static void renderEveryStructure(){
		Tree.renderAll();
		Grass_tuft.renderAll();
		Flower.renderAll();
		Cloud.renderAll();
		Bush.renderAll();
		Bamboo.renderAll();
	}
	
	public static void forEach(Consumer<Structure> consumer){
		Tree.l_i_s_t.forEach(consumer);
		Grass_tuft.l_i_s_t.forEach(consumer);
		Flower.l_i_s_t.forEach(consumer);
		Cloud.l_i_s_t.forEach(consumer);
		Bush.l_i_s_t.forEach(consumer);
		Bamboo.l_i_s_t.forEach(consumer);
	}

	public Structure(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		super(tex, defaultAni, pos, worldLink);
	}
}
