package item;

import particles.BasicMagicEffect;
import particles.MagicEffect;
import resources.Texture;
import world.World;
import world.WorldView;
import world.creatures.Sarah;
import core.geom.Quad;
import core.geom.Vec;

public class MagicWeapon extends DistantWeapon{

	public MagicEffect effect;
	public int manaUse;
	
	public MagicWeapon(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand, String name,
			int coolDownStart, int distPower, int manaUse) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name,
				coolDownStart, distPower, Sarah.castSpell);
		this.manaUse = manaUse;
	}
	
	@Override
	public boolean use(float x, float y){
		return World.sarah.mana - manaUse >= 0 && super.use(x, y);
	}
	
	@Override
	public void startEffect(float x, float y){
		WorldView.particleEffects.add(new BasicMagicEffect(World.sarah.pos.minus(World.sarah.animator.box).plus(new Vec(World.sarah.getHandPosition()[0], World.sarah.getHandPosition()[1])), new Vec(x - (World.sarah.animator.box.middle().x + World.sarah.pos.x), y - (World.sarah.animator.box.middle().y + World.sarah.pos.y)).normalise(), World.sarah));
		World.sarah.mana -= manaUse;
	}
	
}
