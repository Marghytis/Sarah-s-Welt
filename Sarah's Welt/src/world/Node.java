package world;

import core.geom.Vec;


public class Node {
	
	public Material mat;
	public Node next = null;//next should always be counter-clockwise
	public Node last = null;//clockwise
	public Node neighbour = null;
	
	public int _id;
	public Vec p = null;
	
	public boolean inside;
	
	public Node(float x, float y, Material mat){
		this(new Vec(x, y), mat);
	}
	
	public Node(Vec p, Material mat){
		this(mat.highestNodeIndex + 1, p, mat);
	}
	
	public Node(int index, Vec p, Material mat){
		this._id = index;
		this.p = p;
		this.mat = mat;
		World.nodes[mat.ordinal()].add(this);
		mat.highestNodeIndex = Math.max(index, mat.highestNodeIndex);
	}

	public void connectReal(Node node2) {
		this.next = node2;
		node2.last = this;
	}
}
