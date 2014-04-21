package world;

import org.lwjgl.input.Keyboard;

import resources.StackedTexture;
import util.Quad;

public class Character extends WalkingThing {

	public float keyAcc = 0.00005f;// the acceleration the Character experiences
									// on the pressure of a movement key
	public boolean flying = false;
	StackedTexture tex = new StackedTexture("Sarah", 11, 1);
	StackedTexture texjump = new StackedTexture("sarah_jump_l", 7, 1);
	StackedTexture texrun = new StackedTexture("sarah_runs2_r", 9, 1);
	StackedTexture texdown = new StackedTexture("sarah_down", 4, 1);
	StackedTexture texbeat = new StackedTexture("sarah_beat_r", 9, 1);
	StackedTexture texkick = new StackedTexture("sarah_kick", 6, 1);

	public Character(float x, float y) {
		super(1f, 0.5f);
		pos.set(x, y);
		nextPos.set(x, y);
	}

	public void tick(float dTime) {
		if (flying)
			g = false;
		if (g) {
			// System.out.println(pos.x + "  " + vP);
			// apply keyboard force
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				pos.y++;
				nextPos.y++;
				accelerateFromGround(new Point(0, 0.001f));
			}

			int walkingAcceleration = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				walkingAcceleration++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				walkingAcceleration--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				maxSpeed = 24;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				maxSpeed = 6;
			} else {
				maxSpeed = 15;
			}

			// do movement on ground
			calculateSpeed(walkingAcceleration);
			doMotion(dTime);
		} else {
			// apply gravity
			if (!flying)
				accelerate(0, -0.00005f);

			// apply keyboard force
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				accelerate(keyAcc, 0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				accelerate(-keyAcc, 0);
			}
			if (flying && Keyboard.isKeyDown(Keyboard.KEY_W)) {
				accelerate(0, keyAcc);
			}
			if (flying && Keyboard.isKeyDown(Keyboard.KEY_S)) {
				accelerate(0, -keyAcc);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				keyAcc = 0.00009f;
			} else {
				keyAcc = 0.00005f;
			}

			// do movement in air
			updateVel(dTime);
			if (!flying)
				collision();
		}
		// set the character to the new location
		updatePos();
	}

	Quad quad = new Quad(-25, -7, 50, 75);
	int counter = 0;
	int[] framesWalking = { 0, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6 };
	int[] framesJumping = { 1, 2, 3, 4, 5, 6 };
	int[] framesRunning = { 1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5 };
	int[] framesDown = { 1, 2, 3, 4 };
	int[] framesBeat = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
	int[] framesKick = { 1, 2, 3, 4, 5, 6 };
	boolean blickR = true;
	boolean down = false;

	public void render() {
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_S)){
			down = false;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			down = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
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

	}
}
