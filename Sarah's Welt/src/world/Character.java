package world;

import org.lwjgl.input.Keyboard;

import resources.StackedTexture;
import util.Quad;


public class Character extends WalkingThing{
	
	public float keyAcc = 0.00005f;//the acceleration the Character experiences on the pressure of a movement key
	public boolean flying = false;
	StackedTexture tex = new StackedTexture("Sarah", 11, 1);
	
	public Character(float x, float y){
		super(1f, 0.5f);
		pos.set(x, y);
		nextPos.set(x, y);
	}
	
	public void tick(float dTime){
		if(flying) g = false;
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
			if(!flying) accelerate(0, -0.00005f);
			
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
			if(!flying) collision();
		}
		//set the character to the new location
		updatePos();
	}
	
	Quad quad = new Quad(-25, -37, 50, 75);
	int counter = 0;
	int[] framesWalking = {4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6};
	public void render(){
		if(counter >= framesWalking.length*20) counter = 0;

		quad.draw(tex, framesWalking[counter/20], 0);
		
		counter++;
	}
}
