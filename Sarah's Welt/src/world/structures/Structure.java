package world.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import resources.Res;
import resources.StackedTexture;
import resources.Texture;
import util.Animator;
import world.Node;
import world.Point;
import world.Thing;

public abstract class Structure extends Thing{

	protected static int typeIdCounter;
	
	public static Vector<ArrayList<Structure>> structures = new Vector<>();
	public static ArrayList<StackedTexture> structuresTextures = new ArrayList<>();
	
	static {
		int id = 0;
		Tree.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.TREE);
		Bush.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.BUSH);
		Flower.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.FLOWER);
		Bamboo.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.BAMBOO);
		Grass_tuft.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.GRASS_TUFT);
		Cloud.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.CLOUD);
		Crack.typeId = id++; structures.add(new ArrayList<>()); structuresTextures.add(Res.CRACK);
	}
	
	public static void renderStructures(boolean front){
		for(int i = 0; i < structures.size(); i++){
			if(structures.get(i).size() > 0) structuresTextures.get(i).bind();
			structures.get(i).forEach((c) -> {if(front == c.front){c.render();}});
		}
		Texture.bindNone();
	}
	
	public static void updateStructures(int delta){
		for(List<Structure> list : structures)
			list.forEach((c) -> c.update(delta));
	}

	public Structure(Animator ani, Point pos, Node worldLink){
		super(ani, pos, worldLink);
	}
}
