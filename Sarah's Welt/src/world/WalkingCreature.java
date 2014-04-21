package world;

import util.Geom;
import world.creatures.Creature;

public abstract class WalkingCreature extends Creature{
	
	public boolean g = false;
	public static final int framesPerStep = 10;
	public float maxSpeed = 15;
	public float velocityUnit = 0.00035f;
	public int vP;// distance per stepping frame (in acceleration
	
	public int frame;

	public WalkingCreature(Point p, Node worldLink){
		super(Creature.SARAH, p, worldLink);
	}
	
	public void accelerateFromGround(Point vec){
		Point linkVec = worldLink.p.minus(worldLink.next.p);
		if(linkVec.cross(vec) > 0){
			acc.set(vec);
			vP = 0;
			g = false;
		}
	}
	
	public void calculateSpeed(int acc){
		if(acc == 0){//decelerate
			if(vP > 0){
				vP--;
			} else if(vP < 0){
				vP++;
			}
		} else {//accelerate in the specific direction if it doesn't become greater than the max speed
			int newSpeed = vP + acc;
			if(Math.abs(newSpeed) < maxSpeed){
				vP = newSpeed;
			}
		}
	}
	
	public void doMotion(float dTime){
		doStepping(velocityUnit*vP*dTime);
	}
	
	/**
	 * changes vX and vY so the thing is walking on the terrain with pace vP
	 * @param d distance to step
	 */
	public void doStepping(float d){
		if(d == 0){
			vel.set(0, 0);
			return;
		}
		float v = d*WorldWindow.measureScale;
		Point intersection = null;
		
		Node node = worldLink.last.last.last.last.last.last.last.last;
		Node finalNode = null;
		if(node == null)return;//TODO
		for(int i = 0; i <= 16; i++){
			//TODO make circleIntersection relative to the sarah, so I can just add it to the nextPos
			if(node.p.x > node.next.p.x){
				Point[] inter = Geom.circleIntersection(node.p, node.next.p, pos, v);
	
				if(inter[0] != null){
					if(((inter[0].x - pos.x)*(v/Math.abs(v))) > 0){
						if(intersection == null){
							intersection = inter[0];
							finalNode = node;
						} else if(inter[0].y > intersection.y){
							intersection = inter[0];
							finalNode = node;
						}
					}
					if(inter[1] != null && ((inter[1].x-pos.x)*(v/Math.abs(v))) > 0){
						if(intersection == null){
							intersection = inter[1];
							finalNode = node;
						} else if(inter[1].y > intersection.y){
							intersection = inter[1];
							finalNode = node;
						}
					}
				}
			}
			
			node = node.next;
		}
		if(intersection != null){
			vel.set(intersection.minus(pos));
			worldLink = finalNode;
		}
	}
	
	public void collision(){
//		System.out.println("-- Sarah position: " + pos + "  Thought next position: " + nextPos);
		float[] intersection = null;
		for(Sector sector : WorldWindow.sectors){//	iterate columns
			if(sector != null) for(Material mat : Material.values()){//	iterate materials
				if(mat.solid){
					for(Node c : sector.areas[mat.ordinal()].cycles){//	iterate lines
						Node n = c;
						 do {
							n = n.next;
							Point inters = new Point();
							boolean found = Geom.intersectionLines(pos, pos.plus(vel), n.last.p, n.p, inters);
							if(found && (intersection == null || inters.y > intersection[1])){
								if(intersection == null)intersection = new float[2];
								intersection[0] = inters.x;
								intersection[1] = inters.y;
								pos.set(inters);
								worldLink = n;
								vel.set(0, 0);
								acc.set(0, 0);
								g = true;
							}
						} while (n != c);
					}
				}
			}
		}
	}
}
