package world.creatures;

import org.lwjgl.opengl.GL20;

import particles.RainbowSpit;
import resources.Res;
import resources.Shader20;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Unicorn extends WalkingCreature {

	public static int typeId;
	
	static Animation stand = new Animation(0, 0);
	static Animation hitt = new Animation(10, 4, true, 0, 1, 0);
	static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 5, 4, 3, 2);
	static Animation punch = new Animation(5, 2, false, 0, 1, 2, 2, 2, 1, 0);
	
	boolean spitting = false;
	
	public Unicorn(Vec p, Node worldLink){
		super(new Animator(Res.UNICORN, stand), p, worldLink);
//		hitradius = 50;
		maxSpeed = 5;
		animator.doOnReady = () -> donePunch();
		health = 10;
		punchStrength = 4;
	}
	
	public void update(int dTime){
		if(g){
//			pos.y++;
//			accelerateFromGround(new Point(0, 0.001f));
			
			walkingAI(dTime);
		} else {
			acc.add(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision();
		}
		super.update(dTime);
	}
	
	public int dir = 0;
	public void walkingAI(float dTime){
		if(spitting || (!Settings.agro || !findSarah()))wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
//		World.sarah.hitBy(this);
		animator.setAnimation(stand);
		spitting = false;
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	public boolean findSarah(){
		float headX;
		float headY = pos.y + 50;
		if(dir == 1){
			headX = pos.x + 50;
		} else if(dir == -1){
			headX = pos.x - 50;
		} else {
			headX = pos.x;
		}
		
		float distSquare = pos.minus(World.sarah.pos).lengthSqare();
		
		if(distSquare < 160000){
			if(World.sarah.pos.x + World.sarah.animator.tex.box.x - 60 > pos.x){
				dir = 1;
			} else if(World.sarah.pos.x + World.sarah.animator.tex.box.x + World.sarah.animator.tex.box.size.x + 60 < pos.x){
				dir = -1;
			} else {
				animator.setAnimation(punch);
				World.particleEffects.add(new RainbowSpit(headX, headY, this));
				spitting = true;
				dir = 0;
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	protected void beforeRender(){
		super.beforeRender();

		if(hit > 0){
			animator.setAnimation(hitt);
		} else if(!spitting){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
		
		if(turnUp){
			sky[color] += 10;
		} else {
			sky[(color + 2) % 3] -= 10;
		}
		colorCounter += 1;
		if(colorCounter >= 10){
			if(!turnUp){
				color = (color + 1) % 3;
			}
			turnUp = !turnUp;
			colorCounter = 0;
		}
		float[] color = new float[]{sky[0]/100.0f + 0.4f, sky[1]/100.0f + 0.4f, sky[2]/100.0f + 0.4f};
		
		Shader20.UNICORN.bind();
			GL20.glUniform3f(GL20.glGetUniformLocation(Shader20.UNICORN.handle, "color"), color[0], color[1], color[2]);
	}
	
	int color = 0;
	boolean turnUp = true;
	int colorCounter;
	int[] sky = {0, 0, 100};
	
	public void afterRender(){
		Shader20.bindNone();
	}
}
