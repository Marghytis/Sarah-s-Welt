package util;

import world.Point;

public class Animation {

	public int[] sequence;
	public int y;
	public int speed;
	
	public boolean repeat;
	
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
	
	public Point getPoint(int frame){
		if(speed == 0){
			return new Point(sequence[0], y);
		} else if(frame == -1){
			return new Point(sequence[sequence.length-1], y);
		} else {
			Point out = new Point();
			out.x = sequence[frame/speed];
			out.y = y;
			return out;
		}
	}
	
}
