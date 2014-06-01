package world.worldObjects;

import resources.Res;
import util.Color;
import world.Node;
import core.geom.Vec;

public class CandyFlower extends Flower {

	public static int typeId;
	
	public int type;
	public static Color[] colors = {
			new Color(0.79f, 0.91f, 0.31f),
			new Color(0.86f, 0.64f, 0.27f),
			new Color(0.91f, 0.38f, 0.31f),
			new Color(0.86f, 0.64f, 0.27f),
			new Color(0.43f, 0.84f, 0.84f),
			new Color(0.84f, 0.43f, 0.84f)
	};
	
	public CandyFlower(int type, Vec pos, Node worldLink){
		super(type, pos, worldLink, Res.CANDY_FLOWER, colors[type]);
	}
}