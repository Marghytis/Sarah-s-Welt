package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.geom.Vec;


/**this class handles all the world generation mechanisms*/
public class BasePoint {
	Random random;
	Zone zone = Zone.FOREST;
	boolean right;
	Vec pos;
	Structure[] levels;
	public List<Layer> layers;
	
	public BasePoint(boolean right, Vec pos){
		this.right = right;
		this.pos = pos;
		levels = new Structure[zone.possibleStructures.length];
		for(int s = 0; s < levels.length; s++) levels[s] = new Structure();
		layers = new ArrayList<>();
		random = new Random();
	}
	
	public static void setupLayers(BasePoint p1, BasePoint p2){
		float y = p1.pos.y;
		for(AimLayer aim : p1.zone.finalLayers){
			Node n1 = new Node(p1.pos.x, y);
			y -= aim.thickness;
			Node n2 = new Node(p1.pos.x, y);
			
			n1.connect(n2);
			n2.connect(n1);

			p1.layers.add(p1.new Layer(aim, aim.thickness, n1, n2));
			p2.layers.add(p2.new Layer(aim, aim.thickness, n1, n2));

			WorldView.contours[aim.material.ordinal()].add(n1);
		}
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
			levels[s].next(s);
		}
		//create the final points out of this base one
		//TODO add a bit of randomness
		//TODO if a layer finished or started say it
		
		updateLayers();
	}
	
	public void nextAngle(){
		nextAngle = (right ? 0 : Math.PI);
		for(int l = 0; l < levels.length; l++){
			nextAngle += levels[l].next(l);
			if(levels[l].type == null && random.nextInt(100) < 10){
				levels[l].type = zone.possibleStructures[l][random.nextInt(zone.possibleStructures[l].length)];
			}
		}
//		if(random.nextInt(50) == 0){TODO
//			zone = Zone.values()[random.nextInt(Zone.values().length)];
//		}
	}

	/** Remove, start and resize layers*/
	public void updateLayers(){
		float y = pos.y;
		for(Layer layer : layers){
			//approche thickness to aim thickness
			if(layer.thickness + layer.aim.resizeStep < layer.aim.thickness){
				layer.thickness += layer.aim.resizeStep;
			} else if(layer.thickness - layer.aim.resizeStep > layer.aim.thickness){
				layer.thickness -= layer.aim.resizeStep;
			} else {
				//do nothing
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

	public class Structure {
		StructureType type;
		int stepPos;

		public float next(int level){
			if(type == null) return 0;
			if(right){
				stepPos++;
				if(stepPos >= type.steps.length){
					type = zone.possibleStructures[level][random.nextInt(zone.possibleStructures[level].length)];
					stepPos = 0;
				}
			} else {
				stepPos--;
				if(stepPos <= -1){
					type = zone.possibleStructures[level][random.nextInt(zone.possibleStructures[level].length)];
					stepPos = type.steps.length - 1;
				}
			}
			return (float)(type.steps[stepPos]*type.angleStep);
		}
	}

	public enum StructureType {
		RAISING(Math.PI/160, 0,1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0),
		HUBBEL(Math.PI/20, 10,9,6,3,0,-3,-6,-9,-10),
		UP(Math.PI/60, 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0),
		DOWN(Math.PI/60, 0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12,-13,-14,-15,-14,-13,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0);
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

		public Layer(AimLayer aim, float startThickness, Node n1, Node n2){
			this.aim = aim;
			this.thickness = startThickness;
			this.endNodeTop = n1;
			this.endNodeBot = n2;
		}

		public void attach(Node top, Node bot){
			if(right){
				endNodeBot.connect(bot);
				bot.connect(top);
				top.connect(endNodeTop);
	
				endNodeTop = top;
				endNodeBot = bot;
			} else {
				endNodeTop.connect(top);
				top.connect(bot);
				bot.connect(endNodeBot);
	
				endNodeTop = top;
				endNodeBot = bot;
			}
		}
	}
	public static class AimLayer {
		Material material;
		float thickness;
		float resizeStep;

		public AimLayer(Material material, float thickness, float resizeStep){
			this.material = material;
			this.thickness = thickness;
			this.resizeStep = resizeStep;
		}
	}
	/**Zones: recht große Einteilung. es gibt zwischenzones, wo jede Schicht versucht, sich baldmöglichst anzupassen (aufhören, neu beginnen)*/
	public enum Zone {
		FOREST( new AimLayer[]{new AimLayer(Material.GRASS, 10, 0.2f), new AimLayer(Material.EARTH, 30, 0.2f), new AimLayer(Material.STONE, 10000, 200)},
				new StructureType[][]{{StructureType.UP, StructureType.DOWN}});
		
		AimLayer[] finalLayers;
		StructureType[][] possibleStructures;//possible structure types for each level
		
		Zone(AimLayer[] finalLayers, StructureType[][] possibleStructures) {
			this.finalLayers = finalLayers;
			this.possibleStructures = possibleStructures;
		}
	}
}