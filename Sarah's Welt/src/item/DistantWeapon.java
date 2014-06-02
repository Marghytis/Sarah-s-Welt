package item;

import resources.Texture;
import util.Animation;
import world.World;
import core.geom.Quad;

public class DistantWeapon extends Item{

	public int distPower;
	
	public DistantWeapon(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand, String name,
			int coolDownStart, int distPower, Animation animation) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name,
				coolDownStart, animation);
		this.distPower = distPower;
	}
	
	public boolean use(float x, float y){
		if(super.use(x, y)){
			startEffect(x, y);
			World.sarah.useItem(this);
			return true;
		} else {
			return false;
		}
	}
	
	public void startEffect(float x, float y){}

}
