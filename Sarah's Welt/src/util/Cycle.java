package util;

import world.Line;
import world.Node;

public class Cycle {

	public Node start;
	
	public Cycle(Line... lines){
		if(lines.length == 0){
			start = null;
		} else {
			start = lines[0].start;
			for(int l = 1; l < lines.length; l++){
				lines[l-1].end.next = lines[l].start;
				lines[l].start.last = lines[l-1].end;
			}
			start.last = lines[lines.length - 1].end;
			lines[lines.length - 1].end.next = start;
		}
	}
	
	public static void fuseCycles(Node here, Node there){
		
		Node here2 = here.next;
		Node there2 = there.next;
		
		here.next = there2;
		there2.last = here;
		
		there.next = here2;
		here2.last = there;
	}
	
	public void cut(Node from, Node to){
		from.next = to;
		to.last = from;
		start = to;
	}
	
	public String toString(){
		Node n = start;
		String string = "";
		do {
			string += n.p.toString();
			n = n.next;
		} while(n != start);
		return string;
	}
}
