package world.worldObjects;

import util.Animator;
import world.Node;
import world.Thing;
import core.geom.Vec;

public abstract class WorldObject extends Thing{

	public ObjectType type;
	
	public WorldObject(Animator ani, Vec pos, Node worldLink, boolean front, ObjectType type){
		super(ani, pos, worldLink, front);
		this.type = type;
	}
	
	public enum ObjectType {
		TREE(Tree::createNewObject),
		PALM_TREE(PalmTree::createNewObject),
		CANDY_TREE(CandyTree::createNewObject),
		BUSH(Bush::createNewObject),
		CANDY_BUSH(CandyBush::createNewObject),
		CACTUS(Cactus::createNewObject),
		FLOWER(Flower::createNewObject),
		CANDY_FLOWER(CandyFlower::createNewObject),
		BAMBOO(Bamboo::createNewObject),
		GRASS_TUFT(Grass_tuft::createNewObject),
		CLOUD(Cloud::createNewObject),
		CRACK(Crack::createNewObject),
		FOSSIL(Fossil::createNewObject),
		;
		
		public Creator create;
		
		ObjectType(Creator create){
			this.create = create;
		}
		
		public interface Creator {
			public abstract WorldObject create(float x, float y, Node worldLink, boolean front, String metaString);
		}
	}
}
