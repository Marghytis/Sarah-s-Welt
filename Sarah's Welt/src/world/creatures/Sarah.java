package world.creatures;

import org.lwjgl.input.Keyboard;

import resources.StackedTexture;
import util.Animation;
import world.Material;
import world.Node;
import world.Point;
import world.WorldWindow;


public class Sarah extends WalkingCreature {
	
	public float keyAcc = 0.00005f;//the acceleration the Sarah experiences on the pressure of a movement key
	public boolean flying = false;
	
	public static Sarah sarah;
	
	public static void updateSarah(int dTime){
		sarah.tick(dTime);
	}
	
	public static void renderSarah(){
		sarah.render();
	}
	
	public static StackedTexture SARAH = new StackedTexture("Sarah", 11, 7, -0.5f, -0.1f);
	
	static Animation stand = new Animation(0, 0);
	static Animation walk = new Animation(3, 1, true, 	4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6);
	static Animation jump = new Animation(5, 3, false, 	1, 2, 3, 4, 5, 6);
	static Animation fly = new Animation(6, 3);
	static Animation land = new Animation(2, 3, false, 	5, 4, 3, 2, 1);
	static Animation run = new Animation(3, 2, true, 	1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5);
	static Animation sneak = new Animation(10, 5, true, 1, 2, 3, 4);
	static Animation crouch = new Animation(0, 5);
	static Animation punch = new Animation(1, 4, false, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
	static Animation kick = new Animation(3, 6, false, 	1, 2, 3, 4, 5, 6);
	
	
	public Sarah(Point pos, Node worldLink){
		super(SARAH, stand, pos, worldLink);
		hitradius = 80;
		punchStrength = 2;
		maxSpeed = 10;
		animator.doOnReady = () -> WorldWindow.sarah.animator.animation = stand;
		sarah = this;
	}
	
	public void tick(int dTime){
		if(flying) g = false;
		if(g){
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				maxSpeed = 20;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				maxSpeed = 6;
			} else {
				maxSpeed = 10;
			}
			
			walkingAI(dTime);
			
		} else {
			//apply gravity
			if(!flying) acc.add(0, -0.00005f);
			
			//apply keyboard force
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				acc.add(keyAcc, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				acc.add(-keyAcc, 0);
			}
			if(flying){
				if(Keyboard.isKeyDown(Keyboard.KEY_W)){
					acc.add(0, keyAcc);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_S)){
					acc.add(0, -keyAcc);
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				keyAcc = 0.00006f;
			} else {
				keyAcc = 0.00003f;
			}
			applyFriction(Material.AIR);
			
			//do movement in air
			if(!flying) collision();
		}
		super.tick(dTime);
	}
	
	public boolean collision(){
		if(super.collision()){
			animator.setAnimation(land);
			return true;
		}
		return false;
	}
	int keyDir;
	public void walkingAI(float dTime){
		setKeyDirection();
		
		applyDirection(keyDir);
		
		doStepping(velocityUnit*vP*dTime);
	}
	
	public int walkMode;
	protected void beforeRender(){
		setKeyDirection();
		
		walkMode = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))	walkMode++;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))		walkMode--;
		
		if(animator.animation != kick && animator.animation != punch && animator.animation != jump && animator.animation != land){
			if(g){
				if(keyDir != 0){
					switch(walkMode){
					case 0: animator.setAnimation(sneak); break;
					case 1: animator.setAnimation(walk); break;
					case 2: animator.setAnimation(run); break;
					}
				} else {
					switch(walkMode){
					case 0: animator.setAnimation(crouch); break;
					case 1: 
					case 2: animator.setAnimation(stand); break;
					}
				}
			} else {
				animator.setAnimation(fly);
			}
		}
	}
	
	public void setKeyDirection(){
		keyDir = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) keyDir++;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) keyDir--;
	}
	
	public void jump(){
		if(g){
			pos.y++;
			accelerateFromGround(new Point(0, 0.001f));
			animator.setAnimation(jump);
		}
	}
	
	public void punch(){
		switch(walkMode){
		case 0: animator.setAnimation(kick); break;
		case 1:
		case 2: animator.setAnimation(punch); break;
		}
	}
}
