package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;
import world.Thing;
import world.creatures.Butterfly;
import world.creatures.Rabbit;
import world.creatures.Sarah;
import world.creatures.Snail;
import world.otherThings.Heart;

public abstract class Structure extends Thing{
	
	public static void updateEveryStructure(int dTime){
		Butterfly.updateAll(dTime);
		Rabbit.updateAll(dTime);
		Snail.updateAll(dTime);
		Sarah.updateSarah(dTime);
		Heart.updateAll(dTime);
	}
	
	public static void renderEveryStructure(){
		Butterfly.renderAll();
		Rabbit.renderAll();
		Snail.renderAll();
		Sarah.renderSarah();
		Heart.renderAll();
	}

	public Structure(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		super(tex, defaultAni, pos, worldLink);
	}
}
