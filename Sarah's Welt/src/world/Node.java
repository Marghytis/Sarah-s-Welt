package world;

import core.geom.Vec;


public class Node {
	static int indexIndex;//TODO read and save in database
	
	public Material mat;
	public Node next = null;//next should always be counter-clockwise
	public Node last = null;//clockwise
	public Node neighbour = null;
	
	public int _id;
	public Vec p = null;
	
	public Node(float x, float y, Material mat){
		this(new Vec(x, y), mat);
	}
	
	public Node(Vec p, Material mat){
		this(indexIndex++, p, mat);
	}
	
	public Node(int index, Vec p, Material mat){
		this._id = index;
		this.p = p;
	}

	public void connectReal(Node node2) {
		this.next = node2;
		node2.last = this;
	}
}
