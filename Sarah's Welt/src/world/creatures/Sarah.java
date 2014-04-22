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
	
	public static StackedTexture STAND_WALK = new StackedTexture("Sarah", 11, 1, -0.5f, -0.1f);
	static Animation stand = new Animation(0, 0);
	static Animation walk = new Animation(3, 0, 	4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6);
	
	public static StackedTexture JUMP = new StackedTexture("sarah_jump_l", 7 , 1, -0.5f, -0.1f);
	static Animation jump = new Animation(5, 0, () -> WorldWindow.sarah.animator.animation = stand, 	1, 2, 3, 4, 5, 6);
	static Animation fly = new Animation(6, 0);
	static Animation land = new Animation(2, 0, () -> WorldWindow.sarah.animator.animation = stand, 	5, 4, 3, 2, 1);
	
	public static StackedTexture RUN = new StackedTexture("sarah_runs2_r", 9, 1, -0.5f, -0.1f);
	static Animation run = new Animation(3, 0, 	1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5);
	
	public static StackedTexture DOWN = new StackedTexture("sarah_down", 4 , 1, -0.5f, -0.1f);
	static Animation sneak = new Animation(10, 0, 	1, 2, 3, 4);
	static Animation crouch = new Animation(0, 0);
	
	public static StackedTexture PUNCH = new StackedTexture("sarah_beat_r", 9 , 1, -0.5f, -0.1f);
	static Animation punch = new Animation(1, 0, () -> WorldWindow.sarah.animator.animation = stand, 	1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
	
	public static StackedTexture KICK = new StackedTexture("sarah_kick", 6 , 1, -0.5f, -0.1f);
	static Animation kick = new Animation(3, 0, () -> WorldWindow.sarah.animator.animation = stand, 	1, 2, 3, 4, 5, 6);
	
	
	public Sarah(Point pos, Node worldLink){
		super(STAND_WALK, stand, pos, worldLink);
		hitradius = 80;
		punchStrength = 2;
		maxSpeed = 10;
	}
	
	public void tick(float dTime){
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
			tex = JUMP;
			return true;
		}
		return false;
	}
	
//	Quad quad = new Quad(-25, -7, 50, 75);
//	int counter = 0;
//	int[] framesWalking = {0, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6};
//	int[] framesJumping = {1, 2, 3, 4, 5, 6};
//	int[] framesRunning = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5};
//	int[] framesDown = {1, 2, 3, 4};
//	int[] framesBeat = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
//	int[] framesKick = { 1, 2, 3, 4, 5, 6 };
//	boolean blickR = true;
//	boolean down = false;
	
	/*
	public void render(){
		howToRender();
		
		GL11.glLoadIdentity();
		GL11.glTranslatef(Window.WIDTH/2, Window.HEIGHT/2, 0);

		if (!Keyboard.isKeyDown(Keyboard.KEY_S)){
			down = false;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			down = true;
		}

//		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
		if (Mouse.isButtonDown(0)) {
			if (down == true) {
				int time = 5;
				if (counter >= framesKick.length * time) counter = 0;

				if (blickR = false) {
					quad.drawMirrored(texkick, framesKick[counter / time], 0);

					counter++;
				} else if (blickR = true) {
					quad.draw(texkick, framesKick[counter / time], 0);

					counter++;
				}
			} else if (down == false) {
				int time = 5;

				if (counter >= framesBeat.length * time) counter = 20;

				if (blickR = false) {
					quad.drawMirrored(texbeat, framesBeat[counter / time], 0);

					counter++;
				} else if (blickR = true) {
					quad.draw(texbeat, framesBeat[counter / time], 0);

					counter++;
				}
			}
		} else if (down == true && g) {
			int time = 10;
			if (counter >= framesDown.length * time)
				counter = 0;

			if (vP < 0) {
				blickR = false;
				quad.drawMirrored(texdown, framesDown[counter / time], 0);

				counter++;
			} else if (vP > 0) {
				blickR = true;
				quad.draw(texdown, framesDown[counter / time], 0);

				counter++;
			} else if (vP == 0 && blickR == true) {
				quad.draw(texdown, framesDown[counter / time], 0);

			} else if (vP == 0 && blickR == false) {
				quad.drawMirrored(texdown, framesDown[counter / time], 0);

			}
		}

		else if (vP > 0 && g) {
			blickR = true;
			int time = 3;

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (counter >= framesRunning.length * time)
					counter = 4;
				quad.draw(texrun, framesRunning[counter / time], 0);
			}

			else {
				if (counter >= framesWalking.length * time)
					counter = 4;
				quad.draw(tex, framesWalking[counter / time], 0);
			}

			counter++;
		} else if (vP < 0 && g) {
			blickR = false;
			int time = 3;

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (counter >= framesRunning.length * time)
					counter = 4;
				quad.drawMirrored(texrun, framesRunning[counter / time], 0);
			} else {
				if (counter >= framesWalking.length * time)
					counter = 4;
				quad.drawMirrored(tex, framesWalking[counter / time], 0);
			}
			counter++;
		} else if (vP == 0 && g) {
			if (blickR == true) {
				quad.draw(tex, framesWalking[0], 0);
			} else {
				quad.drawMirrored(tex, framesWalking[0], 0);
			}
		}

		else if (!g) {
			int time = 4;
			if (counter >= framesJumping.length * time)
				counter = 23;

			if (vP < 0) {
				blickR = false;
				quad.draw(texjump, framesJumping[counter / time], 0);

				if (counter < 23)
					counter++;
			} else if (vP > 0) {
				blickR = true;
				quad.drawMirrored(texjump, framesJumping[counter / time], 0);

				if (counter < 23)
					counter++;
			} else if (vP == 0 && blickR == true) {
				quad.drawMirrored(texjump, framesJumping[counter / time], 0);

				if (counter < 23)
					counter++;
			} else if (vP == 0 && blickR == false) {
				quad.draw(texjump, framesJumping[counter / time], 0);

				if (counter < 23)
					counter++;
			}
		}
		if(hit > 0){
			hit--;
		}
		if(Settings.hitbox){
			GL11.glColor3f(1, 1, 0);
			box.outline();
			GL11.glColor3f(1, 1, 1);
		}
	}
	*/
	int keyDir;
	public void walkingAI(float dTime){
		setKeyDirection();
		
		applyDirection(keyDir);
		
		doStepping(velocityUnit*vP*dTime);
	}
	
	public int walkMode;
	protected void howToRender(){
		setKeyDirection();
		
		walkMode = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))	walkMode++;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))		walkMode--;
		
		if(animator.animation != kick && animator.animation != punch && animator.animation != jump && animator.animation != land){
			if(g){
				if(keyDir != 0){
					switch(walkMode){
					case 0: animator.setAnimation(sneak); tex = DOWN; break;
					case 1: animator.setAnimation(walk); tex = STAND_WALK; break;
					case 2: animator.setAnimation(run); tex = RUN; break;
					}
				} else {
					switch(walkMode){
					case 0: animator.setAnimation(crouch); tex = DOWN; break;
					case 1: 
					case 2: animator.setAnimation(stand); tex = STAND_WALK; break;
					}
				}
			} else {
				animator.setAnimation(fly); tex = JUMP;
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
			animator.setAnimation(jump); tex = JUMP;
		}
	}
	
	public void punch(){
		switch(walkMode){
		case 0: animator.setAnimation(kick); tex = KICK; break;
		case 1:
		case 2: animator.setAnimation(punch); tex = PUNCH; break;
		}
	}
}
