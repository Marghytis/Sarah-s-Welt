package item;

import java.util.List;

import resources.Res;
import resources.Texture;
import world.World;
import core.geom.Quad;

public enum Item {
	SWORD(Res.WEAPONS, new Quad(0, 0, 27, 53), Res.WEAPONS, new Quad(0, 0, 27, 53), new Quad(-13, -45, 27, 53), 90, "Sword", 0, 0, 4);
	
	public static void renderItems(){
		for(List<WorldItem> list : World.items){
			if(list.size() > 0){
				list.forEach((c) -> c.render());
			}
		}
	}
	
	//in world, in inventory; in hand
	public Texture world;
	public Quad worldTexQuad;
	public Texture hand;
	public Quad handTexQuad;
	public Quad box;
	public int defaultRotationHand;

	public int xItemsPNG;
	public int yItemsPNG;
	
	public String name;
	
	public int damage;
	
	Item(Texture world, Quad worldTexQuad, Texture hand, Quad handTexQuad, Quad box, int defaultRotationHand, String name, int x, int y, int damage){
		this.world = world;
		this.worldTexQuad = worldTexQuad;
		this.hand = hand;
		this.handTexQuad = handTexQuad;
		this.box = box;
		this.defaultRotationHand = defaultRotationHand;
		
		this.name = name;
		
		this.xItemsPNG = x;
		this.yItemsPNG = y;
		
		this.damage = damage;
	}
}