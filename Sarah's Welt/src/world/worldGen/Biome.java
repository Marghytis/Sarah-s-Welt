package world.worldGen;

import item.Item;
import item.WorldItem;

import java.util.Random;

import world.Node;
import world.World;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Cow;
import world.creatures.Creature;
import world.creatures.Gnat;
import world.creatures.Heart;
import world.creatures.Panda;
import world.creatures.Rabbit;
import world.creatures.Scorpion;
import world.creatures.Snail;
import world.creatures.Trex;
import world.creatures.Unicorn;
import world.structures.Bamboo;
import world.structures.Bush;
import world.structures.Cactus;
import world.structures.CandyBush;
import world.structures.CandyFlower;
import world.structures.CandyTree;
import world.structures.Cloud;
import world.structures.Crack;
import world.structures.Flower;
import world.structures.Fossil;
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
				spawnStructure(Bush.typeId, new Bush(random.nextInt(2), new Vec(), node), node, 0);
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
			if(random.nextInt(10) < 2){
				spawnStructure(Crack.typeId, new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			if(random.nextInt(10) < 1){
				spawnStructure(Fossil.typeId, new Fossil(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
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
			if(random.nextInt(100) < 1){
				spawnCreature(Heart.typeId, new Heart(0, new Vec(), null), node, 100);
			}
			if(random.nextInt(100) < 33){
				spawnCreature(Gnat.typeId, new Gnat(new Vec(), null), node, 40);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Trex.typeId, new Trex(new Vec(), null), node, 5);
			}
		}
	}, BAMBOO_FOREST{
		public void spawnThings(Node node){
			//Structures
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			if(random.nextInt(100) < 4){
				spawnStructure(Cloud.typeId, new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400);
			}
			if(random.nextInt(10) < 2){
				spawnStructure(Crack.typeId, new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			if(random.nextInt(10) < 1){
				spawnStructure(Fossil.typeId, new Fossil(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			
			//Creatures
			if(random.nextInt(100) < 3){
				spawnCreature(Panda.typeId, new Panda(new Vec(), null), node, 5);
			}
			if(random.nextInt(100) < 10){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Heart.typeId, new Heart(0, new Vec(), null), node, 100);
			}
		}
	}, MEADOW{
		public void spawnThings(Node node){
			//Structures
			spawnStructure(Grass_tuft.typeId, new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0);
			spawnStructure(Grass_tuft.typeId, new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0);
			if(random.nextInt(100) < 10){
				spawnStructure(Bush.typeId, new Bush(random.nextInt(2), new Vec(), node), node, 0);
			}
			if(random.nextInt(100) < 1){
				spawnStructure(Tree.typeId, new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0);
			}
			if(random.nextInt(100) < 4){
				spawnStructure(Cloud.typeId, new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400);
			}
			if(random.nextInt(10) < 2){
				spawnStructure(Crack.typeId, new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			if(random.nextInt(10) < 1){
				spawnStructure(Fossil.typeId, new Fossil(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
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
			if(random.nextInt(100) < 1){
				spawnCreature(Heart.typeId, new Heart(0, new Vec(), null), node, 100);
			}
		}
	}, DESERT{
		public void spawnThings(Node node){
			//Structures
//			if(random.nextInt(100) < 10){
//				spawnStructure(PalmTree.typeId, new PalmTree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0);
//			}
			if(random.nextInt(100) < 10){
				spawnStructure(Cactus.typeId, new Cactus(random.nextInt(3), new Vec(), null), node, 0);
			}
			if(random.nextInt(10) < 2){
				spawnStructure(Crack.typeId, new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			if(random.nextInt(10) < 1){
				spawnStructure(Fossil.typeId, new Fossil(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}

			//Creatures
//			if(random.nextInt(100) < 1){
//				spawnCreature(Snail.typeId, new Snail(new Vec(), null), node, 5);
//			}
//			if(random.nextInt(100) < 2){
//				spawnCreature(Cow.typeId, new Cow(new Vec(), null), node, 5);
//			}
//			if(random.nextInt(100) < 1){
//				spawnCreature(Rabbit.typeId, new Rabbit(new Vec(), null), node, 5);
//			}
//			if(random.nextInt(100) < 2){
//				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
//			}
			if(random.nextInt(100) < 1){
				spawnCreature(Heart.typeId, new Heart(0, new Vec(), null), node, 100);
			}
			if(random.nextInt(100) < 3){
				spawnCreature(Scorpion.typeId, new Scorpion(new Vec(), null), node, 5);
			}
		}
	}, CANDY{
		public void spawnThings(Node node){
			//Structures
			if(random.nextInt(100) < 20){
				spawnStructure(CandyFlower.typeId, new CandyFlower(random.nextInt(6), new Vec(), null), node, 0);
			}
			if(random.nextInt(100) < 7){
				spawnStructure(CandyTree.typeId, new CandyTree(new Vec(), null, 0.5f + random.nextFloat()), node, 0);
			}
			if(random.nextInt(100) < 20){
				spawnStructure(CandyBush.typeId, new CandyBush(random.nextInt(2), new Vec(), node), node, 0);
			}
			if(random.nextInt(10) < 2){
				spawnStructure(Crack.typeId, new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}
			if(random.nextInt(10) < 1){
				spawnStructure(Fossil.typeId, new Fossil(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000));
			}

			//Creatures
			if(random.nextInt(100) < 5){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Bird.typeId, new Bird(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Unicorn.typeId, new Unicorn(new Vec(), null), node, 2);
			}
			if(random.nextInt(100) < 1){
				spawnCreature(Heart.typeId, new Heart(1, new Vec(), null), node, 100);
			}
			
			//items
			if(random.nextInt(100) < 1){
				spawnItem(new WorldItem(Item.SWORD, new Vec(), null), node, -10);
			}
		}
	};
	public static Random random = new Random();
	
	

	public void spawnThings(Node node){}
	
	public static void spawnCreature(int typeId, Creature c, Node n, float yOffset){
		c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.creatures[typeId].add(c);
	}
	
	public static void spawnStructure(int typeId, Structure c, Node n, float yOffset){
		c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.structures[typeId].add(c);
	}
	
	public static void spawnItem(WorldItem item, Node n, float yOffset){
		item.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.items[item.item.ordinal()].add(item);
	}
}
