package world;


public class MultiNodePoint extends Point{
	
	public Node[] nodes;
	
	public MultiNodePoint(float x, float y, Node... nodes){
		super(x, y);
		this.nodes = nodes;
	}
}
