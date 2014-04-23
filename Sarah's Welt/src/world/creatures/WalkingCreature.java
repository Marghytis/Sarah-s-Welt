package world.creatures;

import resources.StackedTexture;
import util.Animation;
import util.Geom;
import world.Material;
import world.Node;
import world.Point;
import world.Sector;
import world.WorldWindow;

public abstract class WalkingCreature extends Creature{
	
	public boolean g = false;
	public float maxSpeed = 10;
	public float velocityUnit = 0.00035f;
	public int vP;// distance per stepping frame (in acceleration

	public WalkingCreature(StackedTexture tex, Animation defaultAni, Point p, Node worldLink){
		super(tex, defaultAni, p, worldLink);
	}
	
	public void walkingAI(float dTime){}
	
	public boolean hitBy(Creature c){
		if(super.hitBy(c)){
			if(g){
				float xVec = c.pos.x < pos.x ? 0.0005f : -0.0005f;
				float yVec = 0.0007f;
				accelerateFromGround(new Point(xVec, yVec));
			}
			return true;
		}
		return false;
	}
	
	protected void applyDirection(int dir){
		if(dir == 0){//decelerate
			if(vP > 0){
				vP--;
			} else if(vP < 0){
				vP++;
			}
		} else {//accelerate in the specific direction if it doesn't become greater than the max speed
			int newSpeed = vP + dir;
			if(Math.abs(newSpeed) < maxSpeed){
				vP = newSpeed;
			}
		}
	}
	
	public void accelerateFromGround(Point vec){
		pos.y++;
		Point linkVec = worldLink.p.minus(worldLink.next.p);
		if(linkVec.cross(vec) > 0){
			acc.set(vec);
			vP = 0;
			g = false;
		}
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
	
	public boolean collision(){
		float[] intersection = null;
		boolean foundOne = false;
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
								foundOne = true;
							}
						} while (n != c);
					}
				}
			}
		}
		return foundOne;
	}
}
