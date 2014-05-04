package world;

import java.util.ArrayList;
import java.util.List;

import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Creature;
import world.creatures.Rabbit;
import world.creatures.Snail;
import world.otherThings.Heart;
import world.structures.Bamboo;
import world.structures.Bush;
import world.structures.Cloud;
import world.structures.Crack;
import world.structures.Flower;
import world.structures.Grass_tuft;
import world.structures.Structure;
import world.structures.Tree;

@SuppressWarnings("unchecked")
public class World {

	public static List<Node>[] contours;

	public static List<ArrayList<Creature>> creatures = new ArrayList<>();
	public static List<ArrayList<Structure>> structures = new ArrayList<>();
	
	static {
		contours = (ArrayList<Node>[]) new ArrayList<?>[Material.values().length];
		for(int i = 0; i < contours.length; i++){
			contours[i] = new ArrayList<>();
		}
		
		int s_id = 0;
		Tree.typeId = s_id++; structures.add(new ArrayList<>());
		Bush.typeId = s_id++; structures.add(new ArrayList<>());
		Flower.typeId = s_id++; structures.add(new ArrayList<>());
		Bamboo.typeId = s_id++; structures.add(new ArrayList<>());
		Grass_tuft.typeId = s_id++; structures.add(new ArrayList<>());
		Cloud.typeId = s_id++; structures.add(new ArrayList<>());
		Crack.typeId = s_id++; structures.add(new ArrayList<>());

		int c_id = 0;
		Snail.typeId = c_id++; creatures.add(new ArrayList<>());
		Butterfly.typeId = c_id++; creatures.add(new ArrayList<>());
		Heart.typeId = c_id++; creatures.add(new ArrayList<>());
		Rabbit.typeId = c_id++; creatures.add(new ArrayList<>());
		Bird.typeId = c_id++; creatures.add(new ArrayList<>());
	}
}
