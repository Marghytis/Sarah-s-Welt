package world;

import util.Animator;
import util.Geom;
import core.geom.Vec;

public abstract class MovableThing extends Thing{
	public float velocityUnit = 0.00035f;

	public Vec vel = new Vec();
	public Vec acc = new Vec();
	public boolean g;

	public MovableThing(Animator ani, Vec pos, Node worldLink, boolean front) {
		super(ani, pos, worldLink, front);
	}
	
	public void update(float delta){
		if(!g){
			acc.shift(0, -0.00003f);
			applyFriction(Material.AIR);
			collision(true);
		}
		pos.shift(vel);
		
		vel.shift(acc.scaledBy(delta).scaledBy(World.measureScale*delta));
		acc.set(0, 0);
	}
	
	/**
	 * Applies the friction of the specified material to the acceleration
	 * @param mat
	 */
	public void applyFriction(Material mat){
		acc.x -= mat.decelerationFactor*(vel.x*vel.x) * (vel.x > 0 ? 1 : -1);
		acc.y -= mat.decelerationFactor*(vel.y*vel.y) * (vel.y > 0 ? 1 : -1);
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
