package world;


public class Node {
	static int indexIndex;//TODO read and save in database
	
	protected Node next = null; int nextIndex;//next should always be counter-clockwise
	protected Node last = null; int lastIndex;//clockwise
	
	protected int index;
	public Point p = null;
	
	public Node(Point p, Node last){
		this.p = p;
		setLast(last);
		index = indexIndex++;
	}
	
	public Node(Point p){
		this(p, null);
	}
	
	public Node(float x, float y){
		this(new Point(x, y));
	}
	
	public void setLast(Node n){
		last = n;
		if(n != null) lastIndex = n.index;
	}
	
	public void setNext(Node n){
		next = n;
		if(n != null) nextIndex = n.index;
	}
}
