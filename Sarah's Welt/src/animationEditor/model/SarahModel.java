package animationEditor.model;

public class SarahModel extends Model{

	public SarahModel(){//TODO set texture positions correctly - dimensions are about right already
		Bone body = new Bone(0, 0, -10, -10, 20, 80, 0, 0, 20, 80);
			Bone head = new Bone(0, 75, -5, -5, 10, 20, 0, 0, 10, 20);
		
			Bone armBehind = new Bone(0, 70, -4, -4, 8, 30, 0, 0, 8, 30);
			Bone armBefore = new Bone(0, 70, -4, -4, 8, 30, 0, 0, 8, 30);
			
			Bone legBehind = new Bone(0, 0, -5, -4, 10, 60, 0, 0, 8, 30);
			Bone legBefore = new Bone(0, 0, -5, -4, 10, 60, 0, 0, 8, 30);
			
		body.childrenBelow = new Bone[]{armBehind, legBehind};
		body.childrenOnTop = new Bone[]{armBefore, legBefore};
	}
	
}
