package core;

import core.geom.Quad;

public class Textfield extends Quad{

	public StringBuilder text;
	
	public void insert(int pos, char c){
		text.insert(pos, c);
	}
}
