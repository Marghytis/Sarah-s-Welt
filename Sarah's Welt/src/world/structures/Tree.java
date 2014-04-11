package world.structures;

import world.Point;
import world.Structure;

public class Tree extends Structure{

	public Tree(int type, Point pos){
		super(StructureType.values()[StructureType.TREE_1.ordinal() + type], pos);
	}
	
}
