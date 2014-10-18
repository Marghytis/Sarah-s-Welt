package world.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import world.Material;
import world.Node;
import world.World;
import world.Zone;
import world.Zone.ZoneType;
import world.generation.Layer.AimLayer;
import core.geom.Vec;


/**this class handles all the world generation mechanisms*/
public class Generator {
	public static ZoneType startZone = ZoneType.CANDY;
	Random random;
	public Zone zone;
	boolean right;
	public Vec pos;
	public Structure[] levels;
	public List<Layer> layers;
	
	public static Generator[] createStartGenerators(Vec pos){
		Generator g1 = new Generator(true, pos);
		Generator g2 = new Generator(false, new Vec(pos));

		Zone zone = new Zone(startZone);
		g1.zone = zone;
		g2.zone = zone;
		World.zones.add(zone);
		
		float y = pos.y;
		for(AimLayer aim : zone.type.finalLayers){
			Node n1 = new Node(pos.x, y, aim.material);
			y -= aim.thickness;
			Node n2 = new Node(pos.x, y, aim.material);

			g1.layers.add(new Layer(aim, aim.thickness, true).start(n1, n2));
			g2.layers.add(new Layer(aim, aim.thickness, false).start(n1, n2));
		}
		
		return new Generator[]{g1, g2};
	}
	
	public Generator(boolean right, Vec pos){
		this(right, pos, startZone);
	}
	
	public Generator(boolean right, Vec pos, ZoneType zoneType){
		this.right = right;
		this.pos = pos;
		levels = new Structure[zoneType.possibleStructures.length];
		for(int s = 0; s < levels.length; s++) levels[s] = new Structure(s);
		layers = new ArrayList<>();
		random = new Random();
		nextAngle = right ? 0 : Math.PI;
	}

	float segmentLength = 20;
	public double nextAngle;
	
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
				ZoneType newZone = ZoneType.values()[random.nextInt(ZoneType.values().length-1)];
				setZone(newZone);
				if(newZone == ZoneType.LAKE){
					yLake = pos.y;
				}
			}
//			if(zone.type == ZoneType.LAKE){
//				levels[0].stepPos = right ? 1000000 : -10;
//			}
		}
	}
	
	public void setZone(ZoneType z){
		if(right){
			zone.end = pos.x;
			zone = new Zone(z);
			zone.start = pos.x;
			World.zones.add(zone);
		} else {
			zone.start = pos.x;
			zone = new Zone(z);
			zone.end = pos.x;
			World.zones.add(zone);
		}
		for(Layer l : layers){
			l.shrimp = true;
		}
		for(int a = 0; a < z.finalLayers.length; a++){
			layers.add(new Layer(z.finalLayers[a], 0, right));
		}
		layers.sort((Layer l1, Layer l2) -> l2.aim.priority - l1.aim.priority);

		//give the new layers starting point in between the other layers
		for(int l = 0; l < layers.size(); l++){
			if(layers.get(l).endNodeTop == null){
				Node nTop = null;
				//search the next already existing layer to the top or bottom and take its point as a starting point
				int f = 1;
				while(l - f > -1){
					if(layers.get(l - f).endNodeBot != null){
						nTop = new Node(new Vec(pos.x, layers.get(l - f).endNodeBot.p.y), layers.get(l).aim.material);
						break;
					}
					f++;
				}
				if(nTop == null){
					int f2 = 1;
					while(l + f2 < layers.size()){
						if(layers.get(l + f2).endNodeTop != null){
							nTop = new Node(new Vec(pos.x, layers.get(l + f2).endNodeTop.p.y), layers.get(l).aim.material);
							break;
						}
						f2++;
					}
				}
				Node nBot = new Node(new Vec(nTop.p), nTop.mat);
				
				layers.get(l).start(nTop, nBot);
			}
		}
	}
	
	public float yLake;

	/** Remove, start and resize layers*/
	public void updateLayers(){
		float y = pos.y;
		for(int l = 0; l < layers.size(); l++){
			Layer layer = layers.get(l);
			//approche thickness to aim thickness
			if(layer.approachAim()){
				layers.remove(layer);
				l--;
			}
			
			if(layer.aim.material == Material.WATER){
				y = yLake;
			}
			Node nTop = new Node(pos.x, y, layer.aim.material);
			y -= layer.thickness;
			Node nBot = new Node(pos.x, y, layer.aim.material);
			
			layer.attach(nTop, nBot);
		}
	}
	
	public void spawnThings(){
		if(layers.get(0).aim.material != Material.WATER){
			for(ThingSpawner s : zone.type.spawners){
				s.doYourTask(right ? layers.get(0).endNodeTop : layers.get(0).endNodeTop.last, random);
			}
		}
	}

	public class Structure {
		public StructureType type = StructureType.FLAT;
		public int level;
		public int stepPos;
		
		public Structure(int level){
			this.level = level;
		}

		public float next(){
			if(type == null) return 0;
			if(right){
				stepPos++;
				if(stepPos >= type.steps.length){
					if(random.nextInt(10) < 1 && zone.type.possibleStructures[level].length != 0){
						type = zone.type.possibleStructures[level][random.nextInt(zone.type.possibleStructures[level].length)];
					} else {
						type = StructureType.FLAT;
					}
					stepPos = 0;
				}
			} else {
				stepPos--;
				if(stepPos <= -1){
					if(random.nextInt(10) < 1 && zone.type.possibleStructures[level].length != 0){
						type = zone.type.possibleStructures[level][random.nextInt(zone.type.possibleStructures[level].length)];
					} else {
						type = StructureType.FLAT;
					}
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
}