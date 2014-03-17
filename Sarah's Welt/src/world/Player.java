package world;

import org.lwjgl.input.Keyboard;


public class Player extends WalkingThing{
	
	public float keyAcc = 0.00005f;//the acceleration the Player experiences on the pressure of a movement key
	
	public Player(){
		super(1f, 0.5f);
	}
	
	public void tick(float dTime){
		if(g){
//			System.out.println(pos.x + "  " + vP);
			//apply keyboard force
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				pos.y++;
				nextPos.y++;
				accelerateFromGround(new Point(0, 0.001f));
			}
			
			int walkingAcceleration = 0;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				walkingAcceleration++;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				walkingAcceleration--;
			}
			
			//do movement on ground
			calculateSpeed(walkingAcceleration);
			doMotion(dTime);
		} else {
			//apply gravity
			accelerate(0, -0.00005f);
			
			//apply keyboard force
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				accelerate(keyAcc, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				accelerate(-keyAcc, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				accelerate(0, keyAcc);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				accelerate(0, -keyAcc);
			}
			
			//do movement in air
			updateVel(dTime);
			collision();
		}
		//set the player to the new location
		updatePos();
	}
}
