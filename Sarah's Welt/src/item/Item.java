package item;

import resources.Res;
import resources.Texture;
import core.geom.Quad;

public enum Item {
	SWORD(Res.WEAPONS, new Quad(0, 0, 27, 53), Res.WEAPONS, new Quad(0, 0, 27, 53), 90, "Sword");
	
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