package world;

import item.Item;
import item.WorldItem;

import java.util.Random;

import resources.Res;
import world.creatures.Bird;
import world.creatures.Butterfly;
import world.creatures.Cow;
import world.creatures.Creature;
import world.creatures.GiantCat;
import world.creatures.Gnat;
import world.creatures.Heart;
import world.creatures.Panda;
import world.creatures.Rabbit;
import world.creatures.Scorpion;
import world.creatures.Snail;
import world.creatures.Trex;
import world.creatures.Unicorn;
import world.creatures.Zombie;
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
import world.worldObjects.GiantGras;
import world.worldObjects.GiantPlant;
import world.worldObjects.Grass_tuft;
import world.worldObjects.Grave;
import world.worldObjects.GraveTree;
import world.worldObjects.House;
import world.worldObjects.JungleBush;
import world.worldObjects.JungleFlower;
import world.worldObjects.JunglePlants;
import world.worldObjects.JungleTree;
import world.worldObjects.PalmTree;
import world.worldObjects.Pyramide;
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
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {StructureType.ADER}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 333),
									new ThingSpawner((node, random) -> spawnCreature(new Trex(new Vec(), null), node, 5, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Flower(random.nextInt(3), new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Bush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		GIANTS( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {}, {}},
				new ThingSpawner[]{ 
								new ThingSpawner((node, random) -> spawnCreature(new GiantCat(new Vec(), null), node, 5, random), 1, 20),
//								new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(3), new Vec(), null, random.nextInt(Bird.flap.sequence.length)), node, 20, random), 1, 100),
//								new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 500),
//									
								new ThingSpawner((node, random) -> spawnObject(new GiantPlant(new Vec(), null) , node, 0, random), 1, 200),
								new ThingSpawner((node, random) -> spawnObject(new GiantGras(new Vec(), null) , node, 0, random), 1, 400),
//								new ThingSpawner((node, random) -> spawnObject(new JunglePlants(random.nextInt(5), new Vec(), null, 0.5f + random.nextFloat()) , node, 0, random), 1, 200),
//								new ThingSpawner((node, random) -> spawnObject(new JungleBush(new Vec(), node, 0.5f + random.nextFloat(), false) , node, 0, random), 1, 250),
								new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
								new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
		}),
		JUNGLE( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {}, {}},
				new ThingSpawner[]{ 
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(3), new Vec(), null), node, 20, random), 1, 100),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 500),
//									
									new ThingSpawner((node, random) -> spawnObject(new JungleTree(new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new JungleFlower(random.nextInt(5), new Vec(), null) , node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new JunglePlants(new Vec(), null) , node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new JungleBush(new Vec(), node, 0.5f + random.nextFloat()) , node, 0, random), 1, 250),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				}),
		DESERT( new AimLayer[]{new AimLayer(Material.SAND, 10, 0.4f, 80), new AimLayer(Material.SANDSTONE, 60, 2f, 70), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.RAISING}, {}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Scorpion(new Vec(), null), node, 5, random), 1, 30),
									
									new ThingSpawner((node, random) -> spawnObject(new Pyramide(random.nextInt(4), new Vec(), null), node, 0, random), 1, 10),
									new ThingSpawner((node, random) -> spawnObject(new Cactus(random.nextInt(3), new Vec(), null), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 100),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		CEMETERY( new AimLayer[]{new AimLayer(Material.SOIL, 10, 0.2f, 98), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.RAISING}, {}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Bird(3, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Zombie(new Vec(), null), node, 2, random), 4, 30),

									new ThingSpawner((node, random) -> spawnObject(new Grave(random.nextInt(7), new Vec(), null), node, 0, random), 1, 300),
									new ThingSpawner((node, random) -> spawnObject(new GraveTree(random.nextInt(2), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 300),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		LAKE( new AimLayer[]{new AimLayer(Material.WATER, 200, 20, 100), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT}, {}, {}},
				new ThingSpawner[]{ 
////											new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
////											new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(3), new Vec(), null, random.nextInt(Bird.flap.sequence.length)), node, 20, random), 1, 100),
										new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 200),
////													
////											new ThingSpawner((node, random) -> spawnObject(new JungleTree(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 200),
////											new ThingSpawner((node, random) -> spawnObject(new JungleFlower(random.nextInt(5), new Vec(), null) , node, 0, random), 1, 200),
////											new ThingSpawner((node, random) -> spawnObject(new JunglePlants(random.nextInt(5), new Vec(), null, 0.5f + random.nextFloat()) , node, 0, random), 1, 200),
////											new ThingSpawner((node, random) -> spawnObject(new JungleBush(new Vec(), node, 0.5f + random.nextFloat(), false) , node, 0, random), 1, 250),
////											new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
////											new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				}),
		BAMBOO_FOREST( new AimLayer[]{new AimLayer(Material.SOIL, 10, 0.2f, 98), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {}, {}},
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
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {}, {}},
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
		TOWN( new AimLayer[]{new AimLayer(Material.GROUND, 10, 0.5f, 100), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.RAISING}, {}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null), node, 20, random), 1, 40),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
//									new ThingSpawner((node, random) -> spawnCreature(new Unicorn(new Vec(), null), node, 2, random), 1, 10),
//									new ThingSpawner((node, random) -> spawnCreature(new Heart(1, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new House(random.nextInt(6), new Vec(), null), node, 0, random), 1, 100),
//									new ThingSpawner((node, random) -> spawnObject(new CandyTree(new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 70),
//									new ThingSpawner((node, random) -> spawnObject(new CandyBush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				}),
		MEADOW( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null), node, 20, random), 1, 10),
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
				}),
		OASIS( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.SANDSTONE, 60, 2f, 70), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.RAISING}, {}, {}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Scorpion(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 80),
									
									new ThingSpawner((node, random) -> spawnObject(new PalmTree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 80),
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Res.CRACK.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Res.FOSSIL.texs[0].length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),;
		
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