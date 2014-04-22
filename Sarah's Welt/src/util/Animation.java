package util;

import world.Point;

public class Animation {

	public int[] sequence;
	public int y;
	public int speed;
	
	Runnable toDoOnReady = null;
	
	public Animation(int speed, int y, Runnable toDo, int... sequence){
		this(speed,  y, sequence);
		toDoOnReady = toDo;
	}
	
	public Animation(int x, int y){
		this(0, y, x);
	}
	
	public Animation(int speed, int y, int... sequence){
		this.sequence = sequence;
		this.speed = speed;
		this.y = y;
	}
	
	public int next(int frame){
		if(speed == 0){
			return 0;
		} else {
			frame++;
			if(frame/speed >= sequence.length){
				if(toDoOnReady != null){
					toDoOnReady.run();
					frame = -1;
				} else {
					frame = 0;
				}
			}
			return frame;
		}
	}
	
	public Point getPoint(int frame){
		if(speed == 0){
			return new Point(sequence[0], y);
		} else {
			Point out = new Point();
			out.x = sequence[frame/speed];
			out.y = y;
			return out;
		}
	}
	
}
