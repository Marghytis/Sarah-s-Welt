package world.worldGen;

import java.util.Random;

import world.Node;
import world.World;
import world.creatures.Butterfly;
import world.creatures.Creature;
import world.creatures.Snail;
import world.structures.Bamboo;
import world.structures.Structure;
import world.structures.Tree;
import core.geom.Vec;

public enum Biome {
	FOREST{
		public void spawnThings(Node node){
			int rand = random.nextInt(1000);
			if(rand < 200){
				spawnStructure(Tree.typeId, new Tree(random.nextInt(3), new Vec(), null), node, 0);
			} else if(rand < 230){
				spawnCreature(Snail.typeId, new Snail(new Vec(), null), node, 5);
			} else if(rand < 260){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
		}
	}, BAMBOO_FOREST{
		public void spawnThings(Node node){
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			spawnStructure(Bamboo.typeId, new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0);
			
			int rand = random.nextInt(100);
			if(rand < 10){
				spawnCreature(Snail.typeId, new Snail(new Vec(), null), node, 5);
			} else if(rand < 20){
				spawnCreature(Butterfly.typeId, new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20);
			}
		}
	};
	public static Random random = new Random();

	public void spawnThings(Node node){}
	
	private static void spawnCreature(int typeId, Creature c, Node n, float yOffset){
		c.pos.set(n.p.plus(n.getNext().p.minus(n.p).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.creatures.get(typeId).add(c);
	}
	
	private static void spawnStructure(int typeId, Structure c, Node n, float yOffset){
		c.pos.set(n.p.plus(n.getNext().p.minus(n.p).scaledBy(random.nextFloat())).plus(0, yOffset));
		World.structures.get(typeId).add(c);
	}
}
