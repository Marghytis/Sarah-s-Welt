package world.structures;

import java.util.ArrayList;
import java.util.List;

import resources.Texture;
import util.Animator;
import world.Node;
import world.Thing;
import world.World;
import core.geom.Vec;

public abstract class Structure extends Thing{

	public static void renderStructures(boolean front){
		for(ArrayList<Structure> list : World.structures){
			if(list.size() > 0){
				list.get(0).animator.tex.bind();
				list.forEach((c) -> {if(front == c.front){c.render();}});
			}
		}
		Texture.bindNone();
	}
	
	public static void updateStructures(int delta){
		for(List<Structure> list : World.structures)
			list.forEach((c) -> c.update(delta));
	}

	public Structure(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
}
