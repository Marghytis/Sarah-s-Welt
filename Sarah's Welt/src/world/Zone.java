package world;

import item.Item;
import item.WorldItem;

import java.util.Random;

import resources.Res;
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
import world.generation.Generator.StructureType;
import world.generation.Layer.AimLayer;
import world.generation.ThingSpawner;
import world.worldObjects.Bamboo;
import world.worldObjects.Bush;
import world.worldObjects.Cactus;
import world.worldObjects.CandyBush;
import world.worldObjects.CandyFlower;
import world.worldObjects.CandyTree;
import world.worldObjects.Cloud;
import world.worldObjects.Crack;
import world.worldObjects.Flower;
import world.worldObjects.Fossil;
import world.worldObjects.Grass_tuft;
import world.worldObjects.Tree;
import world.worldObjects.WorldObject;
import core.geom.Vec;


public class Zone {
	public ZoneType type;
	public float start;
	public float end;
	
	public Zone(ZoneType type){
		this.type = type;
	}
	
	/**Zones: recht große Einteilung. es gibt zwischenzones, wo jede Schicht versucht, sich baldmöglichst anzupassen (aufhören, neu beginnen)*/
	public static enum ZoneType {
		FOREST( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {StructureType.ADER}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 330),
									new ThingSpawner((node, random) -> spawnCreature(new Trex(new Vec(), null), node, 5, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Bush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Flower(0, new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 200, random), 1, 40),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		DESERT( new AimLayer[]{new AimLayer(Material.SAND, 10, 0.4f, 80), new AimLayer(Material.SANDSTONE, 60, 2f, 70), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Scorpion(new Vec(), null), node, 5, random), 1, 30),
									
									new ThingSpawner((node, random) -> spawnObject(new Cactus(random.nextInt(3), new Vec(), null), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 100),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		BAMBOO_FOREST( new AimLayer[]{new AimLayer(Material.SOIL, 10, 0.2f, 98), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Panda(new Vec(), null), node, 5, random), 1, 30),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 100),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0, random), 2, 800),
									new ThingSpawner((node, random) -> spawnObject(new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400, random), 1, 40),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 10, 100),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		CANDY( new AimLayer[]{new AimLayer(Material.CANDY, 10, 0.2f, 100), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 40),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Unicorn(new Vec(), null), node, 2, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(1, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new CandyFlower(random.nextInt(6), new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new CandyTree(new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 70),
									new ThingSpawner((node, random) -> spawnObject(new CandyBush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		MEADOW( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Cow(new Vec(), null), node, 5, random), 1, 20),
									new ThingSpawner((node, random) -> spawnCreature(new Rabbit(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 20),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null), node, 0, random), 2, 800),
									new ThingSpawner((node, random) -> spawnObject(new Bush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 10),
									new ThingSpawner((node, random) -> spawnObject(new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400, random), 1, 40),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				});
		
		public AimLayer[] finalLayers;
		public StructureType[][] possibleStructures;//possible structure types for each level
		public ThingSpawner[] spawners;
		
		ZoneType(AimLayer[] finalLayers, StructureType[][] possibleStructures, ThingSpawner[] spawners) {
			this.finalLayers = finalLayers;
			this.possibleStructures = possibleStructures;
			this.spawners = spawners;
		}

		public static void spawnCreature(Creature c, Node n, float yOffset, Random random){
			c.pos.set(n.p.plus(n.next.p.minus(n.p).scaledBy(random.nextFloat())).plus(0, yOffset));
			World.creatures[c.type.ordinal()].add(c);
		}

		public static void spawnObject(WorldObject c, Node n, float yOffset, Random random){
			c.pos.set(n.p.plus(n.next.p.minus(n.p).scaledBy(random.nextFloat())).plus(0, yOffset));
			World.worldObjects[c.type.ordinal()].add(c);
		}

		public static void spawnItem(WorldItem c, Node n, float yOffset, Random random){
			c.pos.set(n.p.plus(n.next.p.minus(n.p).scaledBy(random.nextFloat())).plus(0, yOffset));
			World.items[c.item.id].add(c);
		}
	}
}