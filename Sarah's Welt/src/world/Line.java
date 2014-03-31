package world;



public class Line{
	public Node start = null;
	public Node end = null;
	
	int id;
	
	/*
	 * Adding means, that the line contains the added components afterwards
	 * Appending on the other hand just connects the line to the components 
	 */
	
	public Line(float... coords){
		addPoints(coords);
	}
	
	public void connect(Node from, Node to){
		from.next = to;
		to.last = from;
	}

	/**
	 * inserts the Node insert between the first and its next
	 * @param first
	 * @param insert
	 */
	public void insertNodeAfter(Node first, Node insert){
		if(first.next != null){
			Node second = first.next;
			connect(first, insert);
			connect(insert, second);
		} else {
			connect(first, insert);
		}
	}

	public void addPoints(float... coords){
		if(coords.length <= 1){
			System.out.println("ERROR while adding points");
			return;
		}
		if(start == null){
			start = new Node(new Point(coords[0], coords[1]), null);
			end = start;
			for(int p = 2; p < coords.length; p += 2){
				connect(end, new Node(coords[p], coords[p+1]));
				end = end.next;
			}
		} else {
			for(int p = 0; p < coords.length; p += 2){
				end.next = new Node(new Point(coords[p], coords[p+1]), end);
				end = end.next;
			}
		}
	}
	
	public void addPoints(Point... points){
		if(start == null){
			start = new Node(points[0], null);
			end = start;
			for(int p = 1; p < points.length; p++){
				end.next = new Node(points[p], end);
				end = end.next;
			}
		} else {
			for(int p = 0; p < points.length; p++){
				end.next = new Node(points[p], end);
				end = end.next;
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
				Node n = new Node(src.p);
				insertNodeAfter(end, n);
				end = n;
				src = src.next;
			}
			Node n = new Node(src.p);
			insertNodeAfter(end, n);
			end = n;
		} else {
			Node src = l.end;

			while(src != l.start){
				Node n = new Node(src.p);
				insertNodeAfter(end, n);
				end = n;
				src = src.last;
			}
			Node n = new Node(src.p);
			insertNodeAfter(end, n);
			end = n;
		}
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
		return end.next == start;
	}
	
	public boolean appendLine(Line l){
		if(isCircle() || l.isCircle())return false;
		connect(end, l.start);
		return true;
	}
	
	
	public String toString(){
		Node n = start;
		String string = n.p.toString() + "  ";
		while(n != end){
			n = n.next;
			string += n.p.toString() + "  ";
		}
		if(end.next == start){
			string += "  circle O";
		} else {
			if(end.next != null){
				string += "  -->  " + end.next.p + "  ...";
			}
			if(start.last != null){
				string = "...  " + start.last.p + "<--  " + string;
			}
		}
		return string;
	}
}