package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import world.Material;
import world.Node;
import core.geom.Vec;

public class SW_Methods {
public static class WorldView {

	static BasePoint rightGenerator, leftGenerator;
	static Column rightBorder, leftBorder;
	static float posX;
	
	public static void reset(){
		rightGenerator = new BasePoint();
		leftGenerator = new BasePoint();
		rightBorder = new Column();
		leftBorder = new Column();
		posX = 0;
	}

	public static void move(float deltaX){
		float destination = posX + deltaX;
		if(deltaX > 0){

			while(rightGenerator.pos.x < destination){
				rightGenerator.shift(rightBorder);
			}
			//TODO add remove and load code later
		} else if(deltaX < 0){

			while(leftGenerator.pos.x > destination){
				leftGenerator.shift(leftBorder);
			}
			//TODO add remove and load code later
		}
	}
}

public static class Column {
	List<Node> nodes;
	
	public Column(){
		nodes = new ArrayList<>();
	}

	public void attach(Integer[] end, Integer[] start, Vec[] points, boolean right){
		//register changes in the structure of the layers
		for(int i : end) nodes.remove(i);
		for(int i : start) nodes.add(i, null);

		//shifting of openings
		if(right){
			for(int n = 0; n < nodes.size(); n++){
				//all relevant nodes in real order
				Node node2 = nodes.get(n).getNext();Node node3 = new Node(points[n*2]);
				Node node1 = nodes.get(n);			Node node4 = new Node(points[n*2+1]);

				//connect nodes the right way
				node1.connect(node4);
				node4.connect(node3);
				node3.connect(node2);

				//shift ending to the new node
				nodes.set(n, node4);
			}
		} else {
			for(int n = 0; n < nodes.size(); n++){
				//all relevant nodes in real order
				Node node3 = new Node(points[n*2]);		Node node1 = nodes.get(n);
				Node node4 = new Node(points[n*2+1]);	Node node2 = nodes.get(n).getNext();

				//connect nodes the right way
				node1.connect(node3);
				node3.connect(node4);
				node4.connect(node2);

				//shift ending to the new node
				nodes.set(n, node3);
			}
		}
	}
}

/**this class handles all the world generation mechanisms*/
public static class BasePoint {
	Random random;
	Zone zone = Zone.FOREST;
	boolean right;
	Vec pos;
	Structure[] levels;
	List<Layer> layers;
	
	public BasePoint(boolean right, Vec pos){
		this.right = right;
		this.pos = pos;
		levels = new Structure[zone.possibleStructures.length];
		layers = new ArrayList<>();
		random = new Random();
	}

	public void shift(Column borderToShift){
		//shift this point
		pos.shift(new Vec(right ? 10 : -10, 10));
		for(int s = 0; s < levels.length; s++){
			levels[s].next(s);
		}
		//create the final points out of this base one
		//TODO add a bit of randomness
		//TODO if a layer finished or started say it
		
		if(layers.size() == 0){
			for(int i = 0; i < zone.finalLayers.length; i++) layers.add(zone.finalLayers[i]);
		}
		
		List<Integer> layersEnded = new ArrayList<>();
		List<Integer> layersStarted = new ArrayList<>();

		float y = pos.y;
		Vec[] points = new Vec[layers.size()*2];
		for(int i = 0; i < points.length; i += 2){
			points[i] = new Vec(pos.x, y);
			y -= layers.get(i).thickness;
			points[i+1] = new Vec(pos.x, y);
		}
		//attach the computed points to the border and shift it too
		borderToShift.attach(layersEnded.toArray(new Integer[0]), layersStarted.toArray(new Integer[0]), points, right);
	}
	public class Structure {
		StructureType type;
		int stepPos;

		public void next(int level){
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
		}
	}

	public enum StructureType {
		HILL;
		double angleStep;//e.g. = Math.PI/160
		public int[] steps;
	}
	public class Layer {
		Material material;
		float thickness;

		Node endNode;
	}
	/**Zones: recht große Einteilung. es gibt zwischenzones, wo jede Schicht versucht, sich baldmöglichst anzupassen (aufhören, neu beginnen)*/
	public enum Zone {
		FOREST( new Layer[]{},
				new StructureType[][]{});
		
		Layer[] finalLayers;
		StructureType[][] possibleStructures;//possible structure types for each level
		
		Zone(Layer[] finalLayers, StructureType[][] possibleStructures) {
			this.finalLayers = finalLayers;
			this.possibleStructures = possibleStructures;
		}
	}
}
}