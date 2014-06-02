package item;

import resources.Texture;
import world.creatures.Sarah;
import core.geom.Quad;

public class Sword extends Weapon{

	public Sword(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand, String name,
			int coolDownStart, int power) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name,
				coolDownStart, power, Sarah.swingWeapon);
	}

}
