package world.worldGen;

import java.util.Random;

import world.Node;
import world.World;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Cow;
import world.creatures.Creature;
import world.creatures.Panda;
import world.creatures.Rabbit;
import world.creatures.Snail;
import world.structures.Bamboo;
import world.structures.Bush;
import world.structures.Cloud;
import world.structures.Flower;
import world.structures.Grass_tuft;
import world.structures.Structure;
import world.structures.Tree;
import core.geom.Vec;

public enum Biome {
	FOREST{
		public void spawnThings(Node node){
			//Structures
			if(random.nextInt(100) < 20){
				spawnStructure(Tree.typeId, new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0);
			}
			if(random.nextInt(100) < 10){
				spawnStructure(Bush.typeId, new Bush(random.nextInt(2), new Vec(), null), node, 0);
			}
			if(random.nextInt(100) < 20){
				spawnStructure(Flower.typeId, new Flower(0, new Vec(), null), node, 0);
			}
			if(random.nextInt(100) < 20){
				spawnStructure(Grass_tuft.typeId, new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0);
			}
			if(random.nextInt(100) < 4){
				spawnStructure(Cloud.typeId, new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 200);
			}

			//Creatures
			if(random.nextInt(100) < 1){
				spawnCreature(Snail.typeId, new Snail(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Bird.typeId, new Bird(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
		}
	}, BAMBOO_FOREST{
		public void spawnThings(Node node){
			//Structures
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			if(random.nextInt(100) < 4){
				spawnStructure(Cloud.typeId, new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 200);
			}
			
			//Creatures
			if(random.nextInt(100) < 3){
				spawnCreature(Panda.typeId, new Panda(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 10){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
		}
	}, MEADOW{
		public void spawnThings(Node node){
			//Structures
			spawnStructure(Grass_tuft.typeId, new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0);
			spawnStructure(Grass_tuft.typeId, new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0);
			if(random.nextInt(100) < 10){
				spawnStructure(Bush.typeId, new Bush(random.nextInt(2), new Vec(), null), node, 0);
			}
			if(random.nextInt(100) < 1){
				spawnStructure(Tree.typeId, new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0);
			}
			if(random.nextInt(100) < 4){
				spawnStructure(Cloud.typeId, new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 200);
			}

			//Creatures
			if(random.nextInt(100) < 1){
				spawnCreature(Snail.typeId, new Snail(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 2){
				spawnCreature(Cow.typeId, new Cow(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Rabbit.typeId, new Rabbit(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 2){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
		}
	};
	public static Random random = new Random();
	
	

	public void spawnThings(Node node){}
	
	public static void spawnCreature(int typeId, Creature c, Node n, float yOffset){
		c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.creatures.get(typeId).add(c);
	}
	
	public static void spawnStructure(int typeId, Structure c, Node n, float yOffset){
		c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.structures.get(typeId).add(c);
		System.out.println("Spawn struc: " + World.structures.get(Bamboo.typeId).size());
	}
}
