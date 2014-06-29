package world.creatures;

import item.Inventory;
import item.Item;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import world.WorldView;
import world.Zone.ZoneType;
import core.Settings;
import core.geom.Vec;


public class Sarah extends WalkingCreature {
	
	public float keyAcc = 0.00005f;//the acceleration the Sarah experiences on the pressure of a movement key
	public boolean ridingCow = false;
	
	public static Animation stand = new Animation(0, 0);
	public static Animation walk = new Animation(3, 1, true, 	4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6);
	public static Animation jump = new Animation(5, 3, false, 	1, 2, 3, 4, 5, 6);
	public static Animation fly = new Animation(6, 3);
	public static Animation land = new Animation(2, 3, false, 	5, 4, 3, 2, 1);
	public static Animation run = new Animation(2, 2, true, 	1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5);
	public static Animation sneak = new Animation(10, 5, true, 1, 2, 3, 2);
	public static Animation crouch = new Animation(0, 5);
	public static Animation punch = new Animation(1, 4, false, 1, 2, 3, 4, 5, 6, 7, 8, 0);
	public static Animation kick = new Animation(3, 6, false, 	1, 2, 3, 4, 5);
	public static Animation swingWeapon = new Animation(3, 8, false, 0, 1, 2, 3, 4);
	public static Animation castSpell = new Animation(8, 9, false, 0, 1, 0);

	public static Animation mountCow = new Animation(3, 0, false, 0, 1, 2, 3, 4, 5, 6);//don't forget to change the texture!!!
	public static Animation dismountCow = new Animation(3, 0, false, 6, 5, 4, 3, 2, 1, 0);//don't forget to change the texture!!!
	public static Animation walkOnCow = new Animation(3, 1, true, 0, 1, 2, 3, 4);//don't forget to change the texture!!!
	public static Animation standOnCow = new Animation(6, 0);//don't forget to change the texture!!!
	public static Animation flyOnCow = new Animation(2, 1);//don't forget to change the texture!!!
	
	public int mana = 20;
	
	public Sarah(Vec pos, Node worldLink){
		this(pos, worldLink, 20, false);
	}
	
	protected Sarah(Vec pos, Node worldLink, int mana, boolean ridingCow){
		super(new Animator(Res.SARAH, stand), pos, worldLink, false, null);
		hitradius = 60;
		punchStrength = 2;
		maxSpeed = 10;
		animator.doOnReady = () -> {
			if(g){
				if(animator.animation == castSpell){
					System.out.println("Test");
				}
				if(!ridingCow){
					animator.animation = stand;
				} else if(animator.animation == dismountCow){
					this.ridingCow = false;
					Cow newCow = riddenCow;
					newCow.worldLink = World.sarah.worldLink;
					WorldView.thingTasks.add(() -> ZoneType.spawnCreature(newCow, World.sarah.worldLink, 2, random));
					animator.setAnimation(stand);
					animator.tex = Res.SARAH;
					riddenCow = null;
				} else {
					animator.setAnimation(walkOnCow);
				}
			}
		};
	}
	
	@Override
	public void update(int dTime){
		Inventory.update(dTime);
		if(Settings.flying) g = false;
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
			if(!Settings.flying) acc.shift(0, -0.00005f);
			
			//apply keyboard force
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				acc.shift(keyAcc, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				acc.shift(-keyAcc, 0);
			}
			if(Settings.flying){
				if(Keyboard.isKeyDown(Keyboard.KEY_W)){
					acc.shift(0, keyAcc);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_S)){
					acc.shift(0, -keyAcc);
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(Keyboard.isKeyDown(Keyboard.KEY_TAB)){
					keyAcc = 0.0003f;
				} else {
					keyAcc = 0.00006f;
				}
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
		if(super.collision(true)){
			if(!ridingCow)animator.setAnimation(land);
			return true;
		}
		return false;
	}
	int keyDir;
	@Override
	public void walkingAI(float dTime){
		setKeyDirection();
		
		applyDirection(keyDir);
		
		doStepping(velocityUnit*vP*dTime);
	}
	
	public int walkMode;
	@Override
	protected void beforeRender(){
		setKeyDirection();
		
		walkMode = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))	walkMode++;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))		walkMode--;
		
		if(animator.animation.repeat){
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
				} else {
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
		animator.tex.file.bind();
		
		if(ridingCow && g)alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
		else if(Settings.flying){
			GL11.glRotatef(vel.angle()*(180/(float)Math.PI) - 90, 0, 0, 1);
		}
	}
	
	@Override
	public void afterRender(){
		TextureFile.bindNone();
		if(Settings.hitbox){
				animator.tex.box.outline();
		}
		
		if(Inventory.getSelectedItem() != null) Inventory.getSelectedItem().renderHand();
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
	
	public void useItem(Item item){
		if(!ridingCow){
			if(item == Item.fist){
				switch(walkMode){
				case 0: animator.setAnimation(kick); break;
				case 1:
				case 2: animator.setAnimation(punch); break;
				}
			} else {
				animator.setAnimation(item.animation);
			}
		}
	}
	
	public void die(){
		
	}
	
	Cow riddenCow = null;
	
	public void mountCow(Cow c){
		if(g){
			if(ridingCow){
				Cow newCow = riddenCow;
				newCow.worldLink = worldLink;
//				WorldView.thingTasks.add(() -> Biome.spawnCreature(Cow.typeId, newCow, World.sarah.worldLink, 2));//TODO
			}
			animator.setAnimation(mountCow); animator.tex = Res.SARAH_ON_COW;
			ridingCow = true;
			riddenCow = c;
			WorldView.thingTasks.add(() -> World.creatures[CreatureType.COW.ordinal()].remove(c));
		}
	}
	
	public int[] getHandPosition(){
		int x = animator.animation.getPoint(animator.frame);
		int y = animator.animation.y;
		if(ridingCow){
			return Res.SARAH_ON_COW_HAND_COORDS.get(y)[x];
		} else if(ridingCow || y >= Res.SARAH_HAND_COORDS.size() || x >= Res.SARAH_HAND_COORDS.get(y).length){
			return Res.SARAH_HAND_COORDS.get(0)[0];
		} else {
			return Res.SARAH_HAND_COORDS.get(y)[x];
		}
	}
	
	public int[] getHeadPosition(){
		int x = animator.animation.getPoint(animator.frame);
		int y = animator.animation.y;
		if(ridingCow){
			return Res.SARAH_ON_COW_HEAD_COORDS.get(y)[x];
		} else if(y >= Res.SARAH_HEAD_COORDS.size() || x >= Res.SARAH_HEAD_COORDS.get(y).length){
			return Res.SARAH_HEAD_COORDS.get(0)[0];
		} else {
			return Res.SARAH_HEAD_COORDS.get(y)[x];
		}
	}
	
	public void dismountCow(){
		if(g && ridingCow){
			animator.setAnimation(dismountCow);
		}
	}

	public static Sarah createNewSarah(float x, float y, float vX, float vY, int health, Node worldLink, String metaString){

		String[] args = metaString.split(";");
		int mana = Integer.parseInt(args[0]);
		boolean ridingCow = Boolean.parseBoolean(args[1]);
		
		Sarah s = new Sarah(new Vec(x, y), worldLink, mana, ridingCow);
		s.vel.set(vX, vY);
		s.health = health;
		return s;
	}

	@Override
	public String createMetaString() {
		return mana + ";" + ridingCow;
	}
}
