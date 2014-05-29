package item;

import resources.Texture;
import core.geom.Quad;

public class DistantWeapon extends Weapon{

	public int distPower;
	
	public DistantWeapon(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand, String name,
			int distPower) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name,
				0);
		this.distPower = distPower;
	}

}
