package world;

import util.Cycle;

/**
 * Eine Area ist ein Bereich, der von einem Material ausgefüllt wird.
 * Sie kann auf mehrere Sektoren verteilt sein und wird deswegen als
 * AreaParts gespeichert und gehandelt
 * @author Mario
 *
 */
public class Area {
	int id;
	Cycle current;
	Material material;
	AreaPart[] parts = new AreaPart[3];

	public void shiftR(int sX){
		parts[0].disconnectR(parts[1]);

		parts[0] = parts[1];
		parts[1] = parts[2];
		parts[2] = WorldWindow.database.getAreaPart(this, sX);

		parts[1].connectR(parts[2]);
	}

	public void render(){
		Node n = current.start;
		do{

			n = n.next;
		} while(n != current.start);
	}

	public class AreaPart {
		Cycle[] cycles;

		Node[] leftEnds;
		Node[] rightEnds;

		/**
		*Must fit together!!!
		*/
		public void connectR(AreaPart rightPart){
			for(int n = 0; n < rightEnds.length; n++){
				Node here = rightEnds[n];
				Node there = rightPart.leftEnds[n];
				
				Cycle.fuseCycles(cycle, rightPart.cycle, here, there);
			}
		}

		public void disconnectR(AreaPart rightPart){

		}

		public void remove(){}
	}
}