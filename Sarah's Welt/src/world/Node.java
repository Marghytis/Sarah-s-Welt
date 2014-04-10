package world;


public class Node {
	public Node next = null;//next should always be counter-clockwise
	public Node last = null;//clockwise
	
	public Point p = null;
	
	public Node(Point p, Node last){
		this.p = p;
		this.last = last;
	}
	
	public Node(Point p){
		this.p = p;
	}
	
	public Node(float x, float y){
		this.p = new Point(x, y);
	}
}
