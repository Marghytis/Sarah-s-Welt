package world;

import java.util.List;

public class Break {
	
	public class WorldView {
		
		Border right, left;
		
		public void moveTo(float x){
			right.connect();
		}
	}

	public class Border {
		List<Layer> ends;
		
		public void connect(){
			for(Layer e : ends){
				e.connect();
			}
		}
		
		private class Layer {
			Node topL;
			Node topR;
			Node bottomL;
			Node bottomR;
			
			public Layer(Node topL, Node topR, Node bottomL, Node bottomR) {
				super();
				this.topL = topL;
				this.topR = topR;
				this.bottomL = bottomL;
				this.bottomR = bottomR;
			}
			
			public void connect(){
				topR.connect(topL);
				bottomR.connect(bottomL);
			}
		}
	}
}
