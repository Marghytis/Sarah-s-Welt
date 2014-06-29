package world.creatures;

import item.DistantWeapon;
import item.Item;
import util.Animator;
import util.Geom;
import world.Material;
import world.Node;
import world.World;
import world.WorldView;
import core.geom.Vec;

public abstract class WalkingCreature extends Creature{
	
	public boolean g = false;
	public float maxSpeed = 10;
	public float velocityUnit = 0.00035f;
	public int vP;// distance per stepping frame (in acceleration

	public WalkingCreature(Animator ani, Vec p, Node worldLink, boolean front, CreatureType type){
		super(ani, p, worldLink, front, type);
	}
	
	public void walkingAI(float dTime){}
	
	@Override
	public boolean hitBy(Creature c, Item weapon){
		if(super.hitBy(c, weapon)){
			if(g && !(weapon instanceof DistantWeapon)){
				float xVec = c.pos.x < pos.x ? 0.0002f : -0.0002f;
				float yVec = 0.0005f;
				accelerateFromGround(new Vec(xVec, yVec));
			}
			return true;
		} else {
			return false;
		}
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
			} else {
				if(vP > 0){
					vP--;
				} else if(vP < 0){
					vP++;
				}
			}
		}
	}
	
	public void accelerateFromGround(Vec vec){
		pos.y++;
		Vec linkVec = worldLink.p.minus(worldLink.next.p);
		if(linkVec.cross(vec) > 0){
			acc.set(vec);
			vP = 0;
			g = false;
		}
	}
	int adfg;
	/**
	 * changes vX and vY so the thing is walking on the terrain with pace vP
	 * @param d distance to step
	 */
	public void doStepping(float d){
		if(d == 0){
			vel.set(0, 0);
		}
		float v = d*World.measureScale;
		Vec intersection = null;

		Node finalNode = null;
		for(Material mat : Material.values()){//	iterate materials
			if(mat.solid){
				for(Node c : WorldView.contours[mat.ordinal()]){//	iterate lines
					Node node = c;
					//TODO make circleIntersection relative to the sarah, so I can just add it to the nextPos
					do{
						if(node.inside && node.p.x > node.next.p.x){
							Vec[] inter = Geom.circleIntersection(node.p, node.next.p, pos, v);
				
							if(inter[0] != null){
								if(((inter[0].x - pos.x)*(v/Math.abs(v))) > 0){
									if(intersection == null || inter[0].y > intersection.y){
										intersection = inter[0];
										finalNode = node;
									}
								}
								if(inter[1] != null && ((inter[1].x-pos.x)*(v/Math.abs(v))) > 0){
									if(intersection == null || inter[1].y > intersection.y){
										intersection = inter[1];
										finalNode = node;
									}
								}
							}
						}
						node = node.next;
					} while(node != c);
				}
			}
		}
		if(intersection != null){
			vel.set(intersection.minus(pos));
			worldLink = finalNode;
		}
	}
	
	public boolean collision(boolean onlySolid){
		float[] intersection = null;
		boolean foundOne = false;
		for(Material mat : Material.values()){//	iterate materials
			if(!onlySolid || mat.solid){
				for(Node c : WorldView.contours[mat.ordinal()]){//	iterate lines
					Node node = c;
					//TODO make circleIntersection relative to the sarah, so I can just add it to the nextPos
					do{
						if(node.inside && node.p.x > node.next.p.x){
							Vec inters = new Vec();
							boolean found = Geom.intersectionLines(pos, pos.plus(vel), node.p, node.next.p, inters);
							if(found && (intersection == null || inters.y > intersection[1])){
								if(intersection == null)intersection = new float[2];
								intersection[0] = inters.x;
								intersection[1] = inters.y;
								pos.set(inters);
								worldLink = node;
								vP = (int) (vel.x/(velocityUnit*World.measureScale*100));
								vel.set(0, 0);
								acc.set(0, 0);
								g = true;
								foundOne = true;
							}
						}
						node = node.next;
					} while (node != c);
				}
			}
		}
		return foundOne;
	}
}
