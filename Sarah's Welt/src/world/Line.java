package world;



public class Line{
	public Node start = null;
	public Node end = null;
	
	int id;
	
	public void appendLine(Line l, boolean inverted){
		if(!inverted){
			Node src = l.start;
			
			while(src.next != null){
				Node n = new Node(src.p, end);
				end.next = n;
				end = n;
				src = src.next;
			}
			Node n = new Node(src.p, end);
			end.next = n;
			end = n;
		} else {
			Node src = l.end;

			while(!src.equals(l.start) || src.last != null){
				Node n = new Node(src.p, end);
				end.next = n;
				end = n;
				src = src.last;
			}
			Node n = new Node(src.p, end);
			end.next = n;
			end = n;
		}
	}
	
	public void addPoints(float... coords){
		if(start == null){
			start = new Node(new Point(coords[0], coords[1]), null);
			end = start;
			for(int p = 1; p < coords.length/2; p += 2){
				end.next = new Node(new Point(coords[p], coords[p+1]), end);
				end = end.next;
			}
		} else {
			for(int p = 0; p < coords.length/2; p += 2){
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
	
	public void closeCircle(){
		start.last = end;
		end.next = start;
	}
	
	public String toString(){
		String string = "";
		Node n = start;
		do{
			string += n.p.toString() + "  ";
			n = n.next;
		} while(!(n == null || n.next == start));
		return string;
	}
}