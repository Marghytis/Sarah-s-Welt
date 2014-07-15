package item;

import java.util.List;

import resources.Texture;
import util.Animation;
import world.World;
import world.creatures.Creature;
import world.creatures.Gnat;
import core.geom.Quad;

public class Weapon extends Item{

	public Weapon(Texture texWorld, Texture texHand, Texture texinv,
			Quad boxWorld, Quad boxHand, int defaultRotationHand,
			String name, int coolDownStart, int value, int power, Animation animation) {
		super(texWorld, texHand, texinv, boxWorld, boxHand, defaultRotationHand, name, coolDownStart, value, animation);
		this.power = power;
	}

	public int power;
	
	@Override
	public boolean use(float x, float y){
		if(super.use(x, y)){
			for(List<Creature> list : World.creatures) for(Creature c : list){
				if(!(c instanceof Gnat) && (c.pos.x + c.animator.tex.box.x < x && c.pos.x + c.animator.tex.box.x + c.animator.tex.box.size.x > x) && (c.pos.y + c.animator.tex.box.y < y && c.pos.y + c.animator.tex.box.y + c.animator.tex.box.size.y > y)){
					c.hitBy(World.sarah, this);
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
