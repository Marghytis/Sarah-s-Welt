package util;


public class Animation {

	public int[] sequence;
	public int y;
	public int speed;
	
	public boolean repeat;
	
	public Animation(){
		this(0, 0);
	}
	
	public Animation(int x, int y){
		this(0, y, true, x);
	}
	
	public Animation(int speed, int y, boolean repeat, int... sequence){
		this.sequence = sequence;
		this.speed = speed;
		this.y = y;
		this.repeat = repeat;
	}
	
	public int next(int frame){
		if(speed == 0){
			return 0;
		} else {
			frame++;
			if(frame/speed >= sequence.length){
				if(repeat){
					frame = 0;
				} else {
					frame = -1;
				}
			}
			return frame;
		}
	}
	
	public int getPoint(int frame){
		if(speed == 0){
			return sequence[0];
		} else if(frame == -1){
			return sequence[sequence.length-1];
		} else {
			return sequence[frame/speed];
		}
	}
	
}
