package world.worldGen;

import world.Node;

public class Connection{
	public Node nodeL;
	public Node nodeR;
	
	boolean connected = false;
	
	public Connection(Node nodeL, Node nodeR){
		this.nodeL = nodeL;
		this.nodeR = nodeR;
	}
	
	public void link(){
		if(!connected){
			switchConnection();
		}
	}
	
	public void partiate(){
		if(connected){
			switchConnection();
		}
	}
	
	public void switchConnection(){
		Node otherL = nodeL.next;
		Node otherR = nodeR.next;
		
		nodeL.next = otherR;
		otherR.last = nodeL;

		nodeR.next = otherL;
		otherL.last = nodeR;
		connected = !connected;
	}
}