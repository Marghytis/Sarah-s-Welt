package main;

import java.util.ArrayList;
import java.util.List;

import world.Material;
import world.Node;

public class TestingClass {
	public class Cycle{
		
		public Node handle;
	
		public Cycle[] appendCycle(Cycle cycle2, Node... links){
			Cycle[] out = new Cycle[links.length/2];
			for(int i = 0; i < links.length; i += 2){
				Node here = links[i];
				Node there = links[i+1];
				Node here2 = here.next;
				Node there2 = there.next;
				
				here.next = there2;
				there2.last = here;
				
				there.next = here2;
				here2.last = there;
			}
		}
	}
	public class WorldArea {
		public List<Node> cycles = new ArrayList<>();
		public int a_ID;
		public Material material;
	
		public void tryConnect(WorldArea area){
			
		}
	
		public static Node[] connectAreas(Node here, Node there){
			Node here2 = here.next;
			Node there2 = there.next;
			
			here.next = there2;
			there2.last = here;
			
			there.next = here2;
			here2.last = there;
	
			Node n = here;
			boolean hitThere = false;
			do {
				if(n == there){
					hitThere = true;
				}
				n = n.next;
			} while(n != here);
	
			if(!hitThere){
				return new Node[]{here, there};
			} else {
				return new Node[]{here};
			}
		}
	}
	public class Sector {
		public OneMatAreas[] matAreas = new OneMatAreas[Material.values().length];
	
		public void connectSector(Sector other){
			for(int mat = 0; mat < Material.values().length; mat++){
				for(WorldArea areaO : other.matAreas[mat].areas){
					for(WorldArea area : matAreas[mat].areas){
						area.tryConnect(areaO);
					}
				}
			}
		}
	}
	public class Connection{
		Node node;
		int id;
	}
	public class OneMatAreas {
		List<WorldArea> areas = new ArrayList<>();
	}
}