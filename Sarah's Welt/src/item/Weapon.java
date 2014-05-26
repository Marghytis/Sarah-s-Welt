package item;

import resources.Texture;
import core.geom.Quad;

public class Weapon extends Item{

	public Weapon(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand,
			String name, int power) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name);
		this.power = power;
	}

	public int power;
	
}
