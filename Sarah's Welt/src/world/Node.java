package world;

import core.geom.Vec;


public class Node {
	static int indexIndex;//TODO read and save in database
	
	private Node next = null; int nextIndex = -1;//next should always be counter-clockwise
	private Node last = null; int lastIndex = -1;//clockwise
	
	public int index;
	private Vec p = new Vec();
	
	public Node(int index, Vec p, Node last){
		this(p, last);
		this.index = index;
	}
	
	public Node(Vec p, Node last){
		this.p = p;
		setLast(last);
		index = indexIndex++;
	}
	
	public Node(Vec p){
		this(p, null);
	}
	
	public Node(float x, float y){
		this(new Vec(x, y));
	}
	
	public void setLast(Node n){
		last = n;
		if(n != null){
			lastIndex = n.index;
		} else {
			lastIndex = -1;
		}
	}
	
	public void setNext(Node n){
		next = n;
		if(n != null){
			nextIndex = n.index;
		} else {
			nextIndex = -1;
		}
	}
	
	public Node getLast(){
		return last;
	}
	
	public Node getNext(){
		return next;
	}
	
	public Vec getPoint(){
		return p;
	}
	
	public void setPoint(float x, float y){
		p.set(x, y);
	}
	
	public void setPoint(Vec v){
		p.set(v);
	}

	public void connect(Node node2) {
		this.next = node2;
		node2.last = this;
	}

	public void connectTrue(Node node2) {
		setNext(node2);
		node2.setLast(this);
	}
}
