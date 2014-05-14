public class WorldView {

	public static int WIDTH = 1000;

	List<Node> leftConns = new ArrayList<>();
	List<Node> middleAreas = new ArrayList<>();
	List<Node> rightConns = new ArrayList<>();

	Generator rightGenerator;
	Generator leftGenerator;

	float xL, xR;

	public void update(float x){

		float newXL = x - (WIDTH/2);
		float newXR = x + (WIDTH/2);

		if(newXL < xL){
			for(Node conn : leftConns){
				expand(conn, xpWh, false){
			}
		} else if(newXL > xL){
			for(Node conn : leftConns){
				contract(conn, xpWh, true){
			}
		}

		if(newXR > xR){
			for(Node conn : rightConns){
				expand(conn, xpWh, true){
			}
		} else if(newXR < xR){
			for(Node conn : rightConns){
				contract(conn, xpWh, false){
			}
		}
	}

	/**
	*	Takes the nodes from the database. generates new nodes if nessessary
	*/
	public void expand(float dest, boolean rightWards){

		ArrayList<Node> conns = rightWards ? rightConns : leftConns;

		//generate new terrain if nessessary
		if(rightwards && rightGenerator.x < dest){
			rightGenerator.generate(dest, middleAreas, conns);
		} else if(!rightwards && leftGenerator.x > dest){
			leftGenerator.generate(dest, middleAreas, conns);
		}

		for(int i = 0; i < conns.size(); i++){

			Node conn = conns.get(i);

			//create the line which starts at the connection
			Node n1 = conn;
			Node n2 = conn.getNext();//(because n1.next changes)
			while(rightWards ? n1.p.x < dest : n1.p.x > dest){
				if(n1.nextIndex == n2.index){
					middleAreas.add(conn);
					rightConns.remove(conn);
					return;
				}
				n1.next = DB.getNode(n1.nextIndex));
				n1.next.last = n1;
				n1 = n1.getNext();
			}

			//create the second line, which starts at the other part of the connection
			while(rightWards ? n2.p.x < dest : n2.p.x > dest){
				n2.last = DB.getNode(n2.lastIndex);
				n2.last.next = n2;
				n2 = n2.getLast();
			}

			//connect both lines
			n1.next = n2;//not set(), because the index should stay, the index is global and can refer to Nodes that aren't loaded
			n2.last = n1;

			//shift the connection outwards
			conn = n1;

			if(n1.nextIndex == n2.index && n2.lastIndex = n1.index){
				if(rightWards){
					middleAreas.add(conn);
					rightConns.remove(i);
				}
			}
		}
	}

	public void contract(Node conn, float dest, boolean rightWards){

		//follow the bottom line until the node is indise the boundaries
		Node n1 = conn;
		while(rightWards ? n1.p.x < dest : n1.p.x > dest){
			n1 = n1.getLast();
		}

		//follow the top line until the node is indise the boundaries
		Node n2 = conn.getNext();
		while(rightWards ? n2.p.x < dest : n2.p.x > dest){
			n1 = n1.getNext();
		}

		//connect the two shortened lines again (not their indices - they have to stay for the database)
		n1.next = n2;//not set(), because the index should stay, the index is global and can refer to Nodes that aren't loaded
		n2.last = n1;

		//shift the connection towards the center, thus removing all Nodes around it from the harddrive
		conn = n1;
	}
}

public class Generator {

	public boolean rightWards;

	public Biomer biomer;
	public Structurer structurer;
	public Landscaper landscaper;

	Random random;

	public void generate(float dest, List<Node> middleAreas, List<Node> rimConns){
		landscaper.shiftBase();
		structurer.shiftLayers();
	}

	public class Landscaper {
		Landscape landscape = Landscape.STAIRS;
		WorldGenObject[] levels;
		Vec base;

		float segmentLength = 20;
		double nextAngle = rightWards ? 0 : Math.PI;

		public void shiftBase(){
			base.x + = segmentLength*Math.cos(nextAngle);
			base.y += segmentLength*Math.sin(nextAngle);

			nextAngle();
		}

		public void nextAngle(){
			nextAngle = (dir ? 0 : Math.PI);

			for(WorldGenObject level : levels){
				if(level == null){
					level.newObjectPlease();
				}
				try {
					nextAngle += level_2.next(random);
				} catch (Exception e) {//the object is at its end
					level_2 = null;
				}
			}

		}
	}

	public class Biomer {
		Biome biome = Biome.FOREST;
	}

	public class Structurer {//TODO add extra objects (like boulders) and for smooth transition
		WorldStructure structure = WorldStructure.GRASSTOP;

		List<LayerRim> layers = new ArrayList<>();

		public void shiftLayers(Vec base){
			int y = 0;
			for(Layer layer : layers){
				Vec newPointTop = new Vec(base.plus(0, y));
				Vec newPointBottom = new Vec(base.plus(0, y -= layer.thickness));

				Node nodeTop = new Node(newPointTop, rightWards ? null : layer.topNode);
				Node nodeBottom = new Node(newPointBottom, rightWards ? layer.bottomNode : null);
				if(rightWards){
					nodeTop.setNext(layer.topNode);
					layer.topNode.setNext(nodeTop);

					nodeBottom.next = nodeTop;//!! not set, because of index
					nodeTop.last = nodeBottom;
				} else {
					nodeBottom.setLast(layer.bottomNode);
					layer.bottomNode.setNext(nodeBottom);

					nodeBottom.last = nodeTop;//!! not set, because of index
					nodeTop.next = nodeBottom;
				}

				DB.saveNode(layer.topNode);
				DB.saveNode(layer.bottomNode);

				layer.topNode = nodeTop;
				layer.bottomNode = nodeBottom;
			}
		}

		public void endLayer(LayerRim layer){
			if(rightWards){
				layer.topNode.setLast(layer.bottomNode);
				layer.bottomNode.setNext(layer.topNode);
			} else {
				layer.topNode.setNext(layer.bottomNode);
				layer.bottomNode.setLast(layer.topNode);
			}
		}

		public class LayerRim {
			Layer layer;
			Node topNode;
			Node bottomNode;
		}
	}
}
