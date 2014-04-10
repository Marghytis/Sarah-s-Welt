package world;

import java.util.Random;



public abstract class Thing {
	public Point pos = new Point(), vel = new Point(), acc = new Point();
	public float surfaceX, surfaceY;
//	public WorldLink link = new WorldLink();
	public Node worldLink;
	
	public Random random = new Random();
	
	public Thing(float surfaceX, float surfaceY){
		this.surfaceX = surfaceX;
		this.surfaceY = surfaceY;
	}
	
	public void updateVel(float dTime){
		applyFriction(Material.AIR);
		vel.add(acc.scaledBy(dTime));
		//add test for to slow motion?
		nextPos.add(vel.scaledBy(WorldWindow.measureScale*dTime));
		acc.set(0, 0);
	}
	
	protected Point nextPos = new Point();
	
	public void updatePos(){
		pos.set(nextPos);
	}
	
	public void accelerate(float x, float y){
//		acc.x += vec.x;
//		acc.y += vec.y;
		acc.add(x, y);//   Isn't this great? :D
	}
	
	/**
	 * Applies the friction of the specified material to the velocity
	 * @param mat
	 */
	public void applyFriction(Material mat){
		//apply forces
		acc.x += surfaceX*mat.decelerationFactor*(vel.x*vel.x) * (vel.x > 0 ? -1 : 1);
		acc.y += surfaceY*mat.decelerationFactor*(vel.y*vel.y) * (vel.y > 0 ? -1 : 1);
	}
}
