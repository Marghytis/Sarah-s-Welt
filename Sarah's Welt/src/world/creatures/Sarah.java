package world.creatures;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;


public class Sarah extends WalkingCreature {
	
	public float keyAcc = 0.00005f;//the acceleration the Sarah experiences on the pressure of a movement key
	public boolean ridingCow = false;
	
	static Animation stand = new Animation(0, 0);
	static Animation walk = new Animation(3, 1, true, 	4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6);
	static Animation jump = new Animation(5, 3, false, 	1, 2, 3, 4, 5, 6);
	static Animation fly = new Animation(6, 3);
	static Animation land = new Animation(2, 3, false, 	5, 4, 3, 2, 1);
	static Animation run = new Animation(3, 2, true, 	1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5);
	static Animation sneak = new Animation(10, 5, true, 1, 2, 3, 2);
	static Animation crouch = new Animation(0, 5);
	static Animation punch = new Animation(1, 4, false, 1, 2, 3, 4, 5, 6, 7, 8, 0);
	static Animation kick = new Animation(3, 6, false, 	1, 2, 3, 4, 5);

	static Animation mountCow = new Animation(3, 0, false, 0, 1, 2, 3, 4, 5, 6);//don't forget to change the texture!!!
	static Animation dismountCow = new Animation(3, 0, false, 6, 5, 4, 3, 2, 1, 0);//don't forget to change the texture!!!
	static Animation walkOnCow = new Animation(3, 1, true, 0, 1, 2, 3, 4);//don't forget to change the texture!!!
	static Animation standOnCow = new Animation(6, 0);//don't forget to change the texture!!!
	static Animation flyOnCow = new Animation(2, 1);//don't forget to change the texture!!!
	
	public Sarah(Vec pos, Node worldLink){
		super(new Animator(Res.SARAH, stand), pos, worldLink);
		hitradius = 80;
		punchStrength = 2;
		maxSpeed = 10;
		animator.doOnReady = () -> {
			if(g){
				if(!ridingCow){
					World.sarah.animator.animation = stand;
				} else if(animator.animation == dismountCow){
					ridingCow = false;
//					Biome.spawnCreature(Cow.typeId, new Cow(new Vec(), null), worldLink, 5);
					World.sarah.animator.animation = stand;
					World.sarah.animator.tex = Res.SARAH;
				} else {
					World.sarah.animator.animation = walkOnCow;
				}
			}
		};
	}
	
	public void update(int dTime){
		if(Settings.flying) g = false;
		if(oldCow != null){
			World.creatures.get(Cow.typeId).remove(oldCow);
			oldCow = null;
		}
		if(g){
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				maxSpeed = ridingCow ? 30 : 20;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				maxSpeed = ridingCow ? 20 : 4;
			} else {
				maxSpeed = ridingCow ? 20 : 10;
			}
			
			walkingAI(dTime);
			
		} else {
			//apply gravity
			if(!Settings.flying) acc.add(0, -0.00005f);
			
			//apply keyboard force
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				acc.add(keyAcc, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				acc.add(-keyAcc, 0);
			}
			if(Settings.flying){
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
			if(!Settings.flying) collision();
		}
		super.update(dTime);
	}
	
	public boolean collision(){
		if(super.collision()){
			if(!ridingCow)animator.setAnimation(land);
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
				if(!ridingCow){
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
				} else if(animator.animation != mountCow && animator.animation != dismountCow){
					if(keyDir != 0){
						animator.setAnimation(walkOnCow);
					} else {
						animator.setAnimation(standOnCow);
					}
				}
			} else {
				if(ridingCow){
					animator.setAnimation(flyOnCow);
				} else {
					animator.setAnimation(fly);
				}
			}
		}
		animator.tex.bind();
		
		if(ridingCow && g)GL11.glRotatef(worldLink.getPoint().minus(worldLink.getNext().getPoint()).angle()*(180/(float)Math.PI), 0, 0, 1);//worldLink.p.minus(worldLink.getNext().p).angle()
		else if(Settings.flying){
			GL11.glRotatef(vel.angle()*(180/(float)Math.PI) - 90, 0, 0, 1);
		}
	}
	
	public void afterRender(){
		Texture.bindNone();
	}
	
	public void setKeyDirection(){
		keyDir = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) keyDir++;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) keyDir--;
	}
	
	public void jump(){
		if(g){
			pos.y++;
			accelerateFromGround(new Vec(0, 0.001f));
			if(!ridingCow)animator.setAnimation(jump);
		}
	}
	
	public void punch(){
		if(!ridingCow){
			switch(walkMode){
			case 0: animator.setAnimation(kick); break;
			case 1:
			case 2: animator.setAnimation(punch); break;
			}
		}
	}
	
	Cow oldCow = null;
	
	public void mountCow(Cow c){
		if(g){
			animator.setAnimation(mountCow); animator.tex = Res.SARAH_ON_COW;
			ridingCow = true;
			oldCow = c;
		}
	}
	
	public void dismountCow(){
		if(ridingCow){
			animator.setAnimation(dismountCow);
		}
	}
}
