package world;

import util.Geom;
import world.World.Column;

public abstract class CopyOfWalkingThing extends Thing{
	
	public boolean g = false;
	public float aP;
	public static final int framesPerStep = 10;
	public float maxSpeed = 15;
	public float velocityUnit = 0.0005f;
	public int vP;// distance per stepping frame (in acceleration
	
	public int frame;

	public CopyOfWalkingThing(float AX, float AY){
		super(AX, AY);
	}
	
	public void accelerateFromGround(Point vec){
		Point linkVec = link.getVec();
		if(linkVec.cross(vec) < 0){
			acc.set(vec);
			vP = 0;
			g = false;
		}
	}
//	
//	public void applyForceP(float strength){
//		aP += strength/m;//	aY = FY / m
//	}
	
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
		if(d == 0)return;
		float v = d*World.measureScale;
		Point intersection = null;
		int index = 0;
		Line line = World.getColumn(link.fX).lines[link.mat-1].get(link.lineIndex);
		int p1 = link.nextPoint(link.pointIndex1, -8);
		int p2 = link.nextPoint(link.pointIndex2, -8);
		
		for(int i = 0; i <= 16; i++){
			//TODO make circleIntersection relative to the character, so I can just add it to the nextPos
			Point[] inter = Geom.circleIntersection(line.vertices.get(p1), line.vertices.get(p2), pos, v);

			if(inter[0] != null){
				if(((inter[0].x - pos.x)*(v/Math.abs(v))) > 0){
					if(intersection == null){
						intersection = inter[0];
						index = p1;
					} else if(inter[0].y > intersection.y){
						intersection = inter[0];
						index = p1;
					}
				}
				if(inter[1] != null && ((inter[1].x-pos.x)*(v/Math.abs(v))) > 0){
					if(intersection == null){
						intersection = inter[1];
						index = p1;
					} else if(inter[1].y > intersection.y){
						intersection = inter[1];
						index = p1;
					}
				}
			}
//			System.out.println(link);
			p1 = link.nextPoint(p1, 1);
			p2 = link.nextPoint(p2, 1);
		}
		if(intersection != null){
			nextPos.set(intersection);
			link.pointIndex1 = index;
			link.pointIndex2 = link.nextPoint(index, 1);
		}
	}
	
	public boolean collision(Point nextPos){
		System.out.println("-- Character position: " + pos + "  Thought next position: " + nextPos);
		float[] intersection = lineCollision(pos, nextPos);
		if(intersection != null){
			System.out.println("-- Found collision: " + "( " + intersection[0] + " | " + intersection[1] + " )");
			nextPos.set(intersection[0], intersection[1]);
			link.set((int)intersection[2], (int)intersection[3], (int)intersection[4], (int)intersection[5], (int)intersection[6]);
			vel.set(0, 0);
			acc.set(0, 0);
			g = true;
			return true;
		}
		return false;
	}
	
	public float[] lineCollision(Point p1, Point p2){
		float[] intersection = null;
		int columnFX = World.columns[0].x;//TODO die Anzahl der durchsuchten Spalten noch verkleinern
		for(SectorBefore sector : World.columns){//	iterate columns
			for(Material mat : Material.values()){//	iterate materials
				if(mat.solid){
					int lineIndex = 0;
					for(Line l : sector.lines[mat.ordinal()-1]){//	iterate lines
						for(int point2 = 0; point2 < l.vertices.size(); point2++){//	iterate points
							int point1 = point2 == 0 ? l.vertices.size() - 1 : point2 - 1;
							//do intersecting
							Point inters = new Point();
							boolean found = Geom.intersectionLines(p1, p2, l.vertices.get(point1), l.vertices.get(point2), inters);
							if(found && (intersection == null || inters.y > intersection[1])){
								if(intersection == null)intersection = new float[7];
								intersection[0] = inters.x;
								intersection[1] = inters.y;
								intersection[2] = columnFX;
								intersection[3] = mat.ordinal();
								intersection[4] = lineIndex;
								intersection[5] = point1;
								intersection[6] = point2;
							}
						}
						lineIndex++;
					}
				}
			}
			columnFX++;
		}
		return intersection;
	}
}
