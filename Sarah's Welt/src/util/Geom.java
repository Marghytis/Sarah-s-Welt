package util;

import core.geom.Vec;


public class Geom {

	/**
	 * This first tests, if the line from A1 to A2 intersects the one from B1 to B2.
	 * If the test succeeds, the method will put the computed intersection point into 'toIntersection'
	 * @param A1
	 * @param A2
	 * @param B1
	 * @param B2
	 * @param toIntersection
	 * @return
	 */
	public static boolean intersectionLines(Vec A1, Vec A2, Vec B1, Vec B2, Vec toIntersection){
		Vec Ad = A2.minus(A1);
		Vec Bd = B2.minus(B1);
		Vec CmA = B1.minus(A1);//C - A
		float BcD = Ad.cross(Bd);//B x D

		if(BcD != 0){
			float f1 = CmA.cross(Bd)/BcD;
			float f2 = CmA.cross(Ad)/BcD;

			if(0 <= f1 && f1 <= 1 && 0 <= f2 && f2 <= 1){
			
				toIntersection.set(A1.plus(Ad.scaledBy(f1)));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param p1 The first point of the line
	 * @param p2 The second point of the line
	 * @param c	Circle middle point
	 * @param r Radius of the circle
	 * @return
	 */
	public static Vec[] circleIntersection(Vec p1, Vec p2, Vec c, float r){
		Vec[] ausgabe = new Vec[2];
		float a, b;
		a = (p2.y - p1.y)/(p2.x - p1.x);
		b = (p1.y - c.y) - ((p1.x - c.x)*a);
		float quadSum = (((a*a)+1)*r*r)-(b*b);
		if(quadSum > 0){
			float xP1 = -(((float)Math.sqrt(quadSum)+(a*b))/((a*a)+1));
			float yP1 = ((a*xP1)+b) + c.y;
			xP1 += c.x;
			float xP2 = (((float)Math.sqrt(quadSum)-(a*b))/((a*a)+1));
			float yP2 = ((a*xP2)+b) + c.y;
			xP2 += c.x;
			int helpCounter = 0;
			if((p1.x <= xP1 && xP1 <= p2.x)||(p2.x <= xP1 && xP1 <= p1.x)){
				ausgabe[helpCounter] = new Vec(xP1, yP1);
				helpCounter++;
			}
			if((p1.x <= xP2 && xP2 <= p2.x)||(p2.x <= xP2 && xP2 <= p1.x)){
				ausgabe[helpCounter] = new Vec(xP2, yP2);
			}
		} else if(quadSum == 0){
			float xP = ((-a*b)/((a*a)+1));
			float yP = ((a*xP)+b) + c.y;
			xP += c.x;
			if((p1.x <= xP && xP <= p2.x)||(p2.x <= xP && xP <= p1.x)){
				ausgabe[0] = new Vec(xP, yP);
			}
		}
		return ausgabe;
	}
}
