package item;

import particles.BasicMagicEffect;
import particles.MagicEffect;
import resources.Texture;
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
	
	public boolean use(float x, float y){
		return WorldView.sarah.mana - manaUse >= 0 && super.use(x, y);
	}
	
	public void startEffect(float x, float y){
		WorldView.particleEffects.add(new BasicMagicEffect(WorldView.sarah.pos.minus(WorldView.sarah.animator.box).plus(new Vec(WorldView.sarah.getHandPosition()[0], WorldView.sarah.getHandPosition()[1])), new Vec(x - (WorldView.sarah.animator.box.middle().x + WorldView.sarah.pos.x), y - (WorldView.sarah.animator.box.middle().y + WorldView.sarah.pos.y)).normalise(), WorldView.sarah));
		WorldView.sarah.mana -= manaUse;
	}
	
}
