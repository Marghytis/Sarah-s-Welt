package world;

import item.Item;
import item.WorldItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import world.WorldView.Zone;
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
import world.worldObjects.JungleFlower;
import world.worldObjects.JunglePlants;
import world.worldObjects.JungleTree;
import world.worldObjects.PalmTree;
import world.worldObjects.Tree;
import world.worldObjects.WorldObject;
import core.geom.Vec;


/**this class handles all the world generation mechanisms*/
public class BasePoint {
	public static ZoneType startZone = ZoneType.JUNGLE;
	Random random;
	Zone zone;
	boolean right;
	Vec pos;
	Structure[] levels;
	public List<Layer> layers;
	
	public BasePoint(boolean right, Vec pos){
		this.right = right;
		this.pos = pos;
		levels = new Structure[startZone.possibleStructures.length];
		for(int s = 0; s < levels.length; s++) levels[s] = new Structure(s);
		layers = new ArrayList<>();
		random = new Random();
	}
	
	public BasePoint setupLayers(){
		BasePoint p2 = new BasePoint(!right, new Vec(pos));
		zone = new Zone(startZone);
		p2.zone = zone;
		WorldView.zones.add(zone);
		float y = pos.y;
		for(AimLayer aim : zone.type.finalLayers){
			Node n1 = new Node(pos.x, y);
			y -= aim.thickness;
			Node n2 = new Node(pos.x, y);
			
			n1.connect(n2);
			n2.connect(n1);

			layers.add(new Layer(aim, aim.thickness, n1, n2));
			p2.layers.add(p2.new Layer(aim, aim.thickness, n1, n2));

			WorldView.contours[aim.material.ordinal()].add(n1);
		}
		return p2;
	}

	float segmentLength = 20;
	public double nextAngle = 0;
	
	public void shift(){
		float dx = (float)(segmentLength*Math.cos(nextAngle));
		float dy = (float)(segmentLength*Math.sin(nextAngle));
		nextAngle();
		
		//shift this point
		pos.shift(new Vec(dx, dy));
		for(int s = 0; s < levels.length; s++){
			levels[s].next();
		}
		//create the final points out of this base one
		//TODO add a bit of randomness
		//TODO if a layer finished or started say it
		
		updateLayers();
		spawnThings();
	}
	
	public void nextAngle(){
		nextAngle = (right ? 0 : Math.PI);
		for(int l = 0; l < levels.length; l++){
			nextAngle += levels[l].next();
			if(levels[l].type == null && random.nextInt(100) < 10){
				levels[l].type = zone.type.possibleStructures[l][random.nextInt(zone.type.possibleStructures[l].length)];
			}
		}
		if(random.nextInt(100) == 0){
			if(zone.type==ZoneType.DESERT && random.nextInt(3) == 0){
				setZone(ZoneType.OASIS);
			}
			else if(zone.type==ZoneType.OASIS){
				setZone(ZoneType.DESERT);
			}
			else {
				setZone(ZoneType.values()[random.nextInt(ZoneType.values().length-1)]);
			}
		}
	}
	public class LayerSorter {
		int prio;
		public LayerSorter(int prio){ this.prio = prio;}
	}
	
	public void setZone(ZoneType z){
		if(right){
			zone.end = pos.x;
			zone = new Zone(z);
			zone.start = pos.x;
			WorldView.zones.add(zone);
		} else {
			zone.start = pos.x;
			zone = new Zone(z);
			zone.end = pos.x;
			WorldView.zones.add(zone);
		}
		for(Layer l : layers){
			l.shrimp = true;
		}
		for(int a = 0; a < z.finalLayers.length; a++){
			layers.add(new Layer(z.finalLayers[a], 0, null, null));
		}
		layers.sort((Layer l1, Layer l2) -> l2.aim.priority - l1.aim.priority);

		for(int l = 0; l < layers.size(); l++){
			if(layers.get(l).endNodeTop == null){
				Node nTop = null;
				int f = 1;
				while(l - f > -1){
					if(layers.get(l - f).endNodeBot != null){
						nTop = new Node(new Vec(pos.x, layers.get(l - f).endNodeBot.getPoint().y));
						break;
					}
					f++;
				}
				int f2 = 1;
				while(l + f2 < layers.size()){
					if(layers.get(l + f2).endNodeTop != null){
						nTop = new Node(new Vec(pos.x, layers.get(l + f2).endNodeTop.getPoint().y));
						break;
					}
					f2++;
				}
				Node nBot = new Node(new Vec(nTop.getPoint()));
				
				nTop.connectTrue(nBot);
				nBot.connectTrue(nTop);
				
				Layer thisOne = layers.get(l);
				thisOne.endNodeBot = nBot;
				thisOne.endNodeTop = nTop;
				WorldView.contours[thisOne.aim.material.ordinal()].add(nTop);
			}
		}
//		levels = new Structure[zone.type.possibleStructures.length];
//		int posY = (int)((float)a*layers.size()/z.finalLayers.length);
	}

	/** Remove, start and resize layers*/
	public void updateLayers(){
		float y = pos.y;
		for(int l = 0; l < layers.size(); l++){
			Layer layer = layers.get(l);
			//approche thickness to aim thickness
			if(layer.shrimp){
				if(layer.thickness - layer.aim.resizeStep >= 0){
					layer.thickness -= layer.aim.resizeStep;
				} else {
					endLayer(layer);
					l--;
				}
			} else if(!layer.reachedAim){
				if(layer.thickness + layer.aim.resizeStep <= layer.aim.thickness){
					layer.thickness += layer.aim.resizeStep;
				} else if(layer.thickness - layer.aim.resizeStep >= layer.aim.thickness){
					layer.thickness -= layer.aim.resizeStep;
				} else {
					layer.reachedAim = true;
				}
			}
			Node nTop = new Node(pos.x, y);
			y -= layer.thickness;
			Node nBot = new Node(pos.x, y);

			layer.attach(nTop, nBot);
		}
	}

	public void endLayer(Layer layer){
		//TODO shift Points to other list in WorldView
		layers.remove(layer);
	}
	
	public void spawnThings(){
		for(ThingSpawner s : zone.type.spawners){
			s.doYourTask(right ? layers.get(0).endNodeTop : layers.get(0).endNodeTop.getLast(), random);
		}
	}

	public class Structure {
		StructureType type = StructureType.FLAT;
		int level;
		int index;
		int stepPos;
		
		public Structure(int level){
			this.level = level;
		}

		public float next(){
			if(type == null) return 0;
			if(right){
				stepPos++;
				if(stepPos >= type.steps.length){
					if(random.nextInt(10) < 1){
						index = random.nextInt(zone.type.possibleStructures[level].length);
						type = zone.type.possibleStructures[level][index];
					} else {
						type = StructureType.FLAT;
					}
					stepPos = 0;
				}
			} else {
				stepPos--;
				if(stepPos <= -1){
					type = zone.type.possibleStructures[level][random.nextInt(zone.type.possibleStructures[level].length)];
					stepPos = type.steps.length - 1;
				}
			}
			return (float)(type.steps[stepPos]*type.angleStep);
		}
	}

	public enum StructureType {
		FLAT(0, 0),
		RAISING(Math.PI/160, 0,1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0),
		HUBBEL(Math.PI/20, 10,9,6,3,0,-3,-6,-9,-10),
		UP(Math.PI/60, 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0),
		DOWN(Math.PI/60, 0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12,-13,-14,-15,-14,-13,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0),
		ADER(Math.PI/40, 0, 1, 2, 4, 7, 9, 5, 1, -4, -9, -7, -4, -2, -1, 0);
		double angleStep;//e.g. = Math.PI/160
		public int[] steps;

		StructureType(double angleStep, int... steps){
			this.angleStep = angleStep;
			this.steps = steps;
		}
	}
	public class Layer {
		AimLayer aim;
		float thickness;

		Node endNodeTop;
		Node endNodeBot;
		
		boolean reachedAim;
		boolean shrimp;

		public Layer(AimLayer aim, float startThickness, Node nTop, Node nBot){
			this.aim = aim;
			this.thickness = startThickness;
			this.endNodeTop = nTop;
			this.endNodeBot = nBot;
		}

		public void attach(Node top, Node bot){
			if(right){
				endNodeBot.connectTrue(bot);
				bot.connectTrue(top);
				top.connectTrue(endNodeTop);
	
				endNodeTop = top;
				endNodeBot = bot;
			} else {
				endNodeTop.connectTrue(top);
				top.connectTrue(bot);
				bot.connectTrue(endNodeBot);
	
				endNodeTop = top;
				endNodeBot = bot;
			}
		}
	}
	public static class AimLayer {
		Material material;
		float thickness;
		float resizeStep;
		int priority;//0 to 100

		public AimLayer(Material material, float thickness, float resizeStep, int priority){
			this.material = material;
			this.thickness = thickness;
			this.resizeStep = resizeStep;
			this.priority = priority;
		}
	}
	public static class ThingSpawner {
		Spawner spawner;
		int maxCreatsPerLine;
		int probInPercent;
		
		public ThingSpawner(Spawner spawner, int maxAmount, int prob){
			this.spawner = spawner;
			this.maxCreatsPerLine = maxAmount;
			this.probInPercent = prob;
		}
		
		public void doYourTask(Node n, Random random){
			int perc = probInPercent;
			int luck = random.nextInt(1000);
			for(int i = 0; i < maxCreatsPerLine; i++){
				if(luck < perc){
					spawner.spawn(n, random);
					perc = (int)(perc * ((float)perc/1000.0f));
				} else {
					break;
				}
			}
		}
	}
	public interface Spawner {public abstract void spawn(Node node, Random random);}
	
	/**Zones: recht große Einteilung. es gibt zwischenzones, wo jede Schicht versucht, sich baldmöglichst anzupassen (aufhören, neu beginnen)*/
	public static enum ZoneType {
		FOREST( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {StructureType.ADER}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 330),
									new ThingSpawner((node, random) -> spawnCreature(new Trex(new Vec(), null), node, 5, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Flower(random.nextInt(3), new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Bush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		JUNGLE( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{ 
//									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(3), new Vec(), null, random.nextInt(Bird.flap.sequence.length)), node, 20, random), 1, 100),
//									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
//									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 330),
//									
									new ThingSpawner((node, random) -> spawnObject(new JungleTree(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new JungleFlower(random.nextInt(5), new Vec(), null) , node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new JunglePlants(random.nextInt(5), new Vec(), null, 0.5f + random.nextFloat()) , node, 0, random), 1, 200),
//									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0, random), 1, 200),
//									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
//									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
//									
//									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		DESERT( new AimLayer[]{new AimLayer(Material.SAND, 10, 0.4f, 80), new AimLayer(Material.SANDSTONE, 60, 2f, 70), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Scorpion(new Vec(), null), node, 5, random), 1, 30),
									
									new ThingSpawner((node, random) -> spawnObject(new Cactus(random.nextInt(3), new Vec(), null), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 100),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		BAMBOO_FOREST( new AimLayer[]{new AimLayer(Material.SOIL, 10, 0.2f, 98), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Panda(new Vec(), null), node, 5, random), 1, 30),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 100),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Bamboo(random.nextInt(4), new Vec(), null, random.nextFloat() + 0.5f), node, 0, random), 2, 800),
									new ThingSpawner((node, random) -> spawnObject(new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400, random), 1, 40),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 10, 100),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(4), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		CANDY( new AimLayer[]{new AimLayer(Material.CANDY, 10, 0.2f, 100), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 40),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Unicorn(new Vec(), null), node, 2, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(1, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new CandyFlower(random.nextInt(6), new Vec(), null), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new CandyTree(new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 70),
									new ThingSpawner((node, random) -> spawnObject(new CandyBush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		MEADOW( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.EARTH, 30, 1f, 90), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Snail(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Bird(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Cow(new Vec(), null), node, 5, random), 1, 20),
									new ThingSpawner((node, random) -> spawnCreature(new Rabbit(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Butterfly(random.nextInt(2), new Vec(), null, random.nextInt(Butterfly.flap1.sequence.length)), node, 20, random), 1, 20),
									new ThingSpawner((node, random) -> spawnCreature(new Heart(0, new Vec(), null), node, 100, random), 1, 10),
									
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0, random), 2, 800),
									new ThingSpawner((node, random) -> spawnObject(new Bush(random.nextInt(2), new Vec(), node), node, 0, random), 1, 100),
									new ThingSpawner((node, random) -> spawnObject(new Tree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 10),
									new ThingSpawner((node, random) -> spawnObject(new Cloud(new Vec(), null, 0.5f + random.nextFloat(), random.nextBoolean()), node, 400, random), 1, 40),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
				
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),
		OASIS( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f, 99), new AimLayer(Material.SANDSTONE, 60, 2f, 70), new AimLayer(Material.STONE, 10000, 200, 0)},
				new StructureType[][]{{StructureType.FLAT, StructureType.UP, StructureType.DOWN}, {StructureType.ADER}},
				new ThingSpawner[]{	new ThingSpawner((node, random) -> spawnCreature(new Scorpion(new Vec(), null), node, 5, random), 1, 10),
									new ThingSpawner((node, random) -> spawnCreature(new Gnat(new Vec(), null), node, 40, random), 1, 80),
									
									new ThingSpawner((node, random) -> spawnObject(new PalmTree(random.nextInt(3), new Vec(), null, 0.5f + random.nextFloat()), node, 0, random), 1, 80),
									new ThingSpawner((node, random) -> spawnObject(new Grass_tuft(new Vec(), null, random.nextInt(Grass_tuft.wave.sequence.length)), node, 0, random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Crack(random.nextInt(Crack.crack.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									new ThingSpawner((node, random) -> spawnObject(new Fossil(random.nextInt(Fossil.fossil.length), new Vec(), null, 0.5f + random.nextFloat(), random.nextInt(360)), node, -200 - random.nextInt(1000), random), 1, 200),
									
									new ThingSpawner((node, random) -> spawnItem(new WorldItem(Item.sword, new Vec(), null), node, 0, random), 1, 5),
				}),;
		
		AimLayer[] finalLayers;
		StructureType[][] possibleStructures;//possible structure types for each level
		ThingSpawner[] spawners;
		
		ZoneType(AimLayer[] finalLayers, StructureType[][] possibleStructures, ThingSpawner[] spawners) {
			this.finalLayers = finalLayers;
			this.possibleStructures = possibleStructures;
			this.spawners = spawners;
		}

		public static void spawnCreature(Creature c, Node n, float yOffset, Random random){
			c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
			WorldView.creatures[c.id].add(c);
		}

		public static void spawnObject(WorldObject c, Node n, float yOffset, Random random){
			c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
			WorldView.worldObjects[c.id].add(c);
		}

		public static void spawnItem(WorldItem c, Node n, float yOffset, Random random){
			c.pos.set(n.getPoint().plus(n.getNext().getPoint().minus(n.getPoint()).scaledBy(random.nextFloat())).plus(0, yOffset));
			WorldView.items[c.item.id].add(c);
		}
	}
}