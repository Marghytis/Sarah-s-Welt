package item;

import java.util.List;

import resources.Res;
import resources.Texture;
import world.World;
import core.geom.Quad;

public enum Item {
	SWORD(Res.WEAPONS, new Quad(0, 0, 27, 53), Res.WEAPONS, new Quad(0, 0, 27, 53), 90, "Sword");
	
	public static void renderItems(){
		for(List<WorldItem> list : World.items){
			if(list.size() > 0){
				list.forEach((c) -> c.render());
			}
		}
	}
	
	//in world, in inventory; in hand
	Texture world;
	Quad worldTexQuad;
	Texture hand;
	Quad handTexQuad;
	int defaultRotationHand;
	
	String name;
	
	Item(Texture world, Quad worldTexQuad, Texture hand, Quad handTexQuad, int defaultRotationHand, String name){
		this.world = world;
		this.worldTexQuad = worldTexQuad;
		this.hand = hand;
		this.handTexQuad = handTexQuad;
		this.defaultRotationHand = defaultRotationHand;
		
		this.name = name;
	}
}