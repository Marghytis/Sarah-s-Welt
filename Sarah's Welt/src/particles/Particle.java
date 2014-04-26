package particles;

import util.Color;
import world.Point;


public class Particle {
	public Point pos = new Point();
	public Point vel = new Point();//per millis
	public Color col = new Color();
	public float rot;
	public float rad;
	public int live;//millis
	public boolean justSpawned = true;
}
