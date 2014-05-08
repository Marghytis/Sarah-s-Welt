package util;

import world.Node;
import core.geom.Vec;



public class Line{
	public Node start = null;
	public Node end = null;
	
	int id;
	
	/*
	 * Adding means, that the line contains the added components afterwards
	 * Appending on the other hand just connects the line to the components 
	 */
	
	public Line(float... coords){
		addVecs(coords);
	}
	
	public void empty(){
		start = null;
		end = null;
	}
	
	public void connect(Node from, Node to){
		from.setNext(to);
		to.setLast(from);
	}

	/**
	 * inserts the Node insert between the first and its getNext()
	 * @param first
	 * @param insert
	 */
	public void insertNodeAfter(Node first, Node insert){
		if(first.getNext() != null){
			Node second = first.getNext();
			connect(first, insert);
			connect(insert, second);
		} else {
			connect(first, insert);
		}
	}
	
	public void addVec(float x, float y){
		Node n = new Node(new Vec(x, y));
		if(end == null){
			end = n;
			start = n;
		} else {
			connect(end, n);
			end = end.getNext();
		}
	}
	
	public void addVec(Vec p){
		Node n = new Node(p);
		if(end == null){
			end = n;
			start = n;
		} else {
			connect(end, n);
			end = end.getNext();
		}
	}
	
	public void addVecBack(float x, float y){
		addVecBack(new Vec(x, y));
	}

	public void addVecBack(Vec Vec){
		Node n = new Node(Vec);
		if(end == null){
			end = n;
			start = n;
		} else {
			connect(n, start);
			start = n;
		}
	}

	public void addVecs(float... coords){
		if(coords.length <= 1){
			//ERROR
			return;
		}
		if(start == null){
			start = new Node(new Vec(coords[0], coords[1]), null);
			end = start;
			for(int p = 2; p < coords.length; p += 2){
				connect(end, new Node(coords[p], coords[p+1]));
				end = end.getNext();
			}
		} else {
			for(int p = 0; p < coords.length; p += 2){
				end.setNext(new Node(new Vec(coords[p], coords[p+1]), end));
				end = end.getNext();
			}
		}
	}
	
	public void addVecs(Vec... Vecs){
		if(start == null){
			start = new Node(Vecs[0], null);
			end = start;
			for(int p = 1; p < Vecs.length; p++){
				end.setNext(new Node(Vecs[p], end));
				end = end.getNext();
			}
		} else {
			for(int p = 0; p < Vecs.length; p++){
				end.setNext(new Node(Vecs[p], end));
				end = end.getNext();
			}
		}
	}
	
	/**
	 * clones the Line and either connects the clone the one way or the other at the end
	 * @param l
	 * @param inverted
	 */
	public void addNewLine(Line l, boolean inverted){
		if(!inverted){
			Node src = l.start;
			
			while(src != l.end){
				Node n = new Node(src.getPoint());
				insertNodeAfter(end, n);
				end = n;
				src = src.getNext();
			}
			Node n = new Node(src.getPoint());
			insertNodeAfter(end, n);
			end = n;
		} else {
			Node src = l.end;

			while(src != l.start){
				Node n = new Node(src.getPoint());
				insertNodeAfter(end, n);
				end = n;
				src = src.getLast();
			}
			Node n = new Node(src.getPoint());
			insertNodeAfter(end, n);
			end = n;
		}
	}
	
	public void removeLast(){
		end = end.getLast();
		end.setNext(null);
	}
	
	public void removeFirst(){
		start = start.getNext();
		start.setLast(null);
	}
	
	public void closeCircle(){
		connect(end, start);
	}
	
	public void addLine(Line l){
		appendLine(l);
		end = l.end;
	}
	
	public boolean circleWithLine(Line l){
		if(isCircle() || l.isCircle())return false;
		
		connect(end, l.start);
		connect(l.end, start);
		return true;
	}
	
	public boolean isCircle(){
//		return end.getNext() == start;
		return false;
	}
	
	public boolean appendLine(Line l){
		if(isCircle() || l.isCircle())return false;
		connect(end, l.start);
		return true;
	}
	
	
	public String toString(){
		Node n = start;
		String string = n.getPoint().toString() + "  ";
		while(n != end){
			n = n.getNext();
			string += n.getPoint().toString() + "  ";
		}
		if(end.getNext() == start){
			string += "  circle O";
		} else {
			if(end.getNext() != null){
				string += "  -->  " + end.getNext().getPoint() + "  ...";
			}
			if(start.getLast() != null){
				string = "...  " + start.getLast().getPoint() + "<--  " + string;
			}
		}
		return string;
	}
}