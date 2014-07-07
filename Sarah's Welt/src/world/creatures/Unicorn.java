package world.creatures;

import item.Inventory;
import item.Item;
import item.WorldItem;

import org.lwjgl.opengl.GL11;

import particles.RainbowSpit;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import world.WorldView;
import core.Settings;
import core.geom.Vec;

public class Unicorn extends WalkingCreature {
	
	static Animation stand = new Animation(0, 0);
	static Animation hitt = new Animation(10, 1, true, 0, 1, 0);
	static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 5, 4, 3, 2);
	static Animation punch = new Animation(5, 2, false, 0, 1, 2, 2, 2, 1, 0);
	
	boolean spitting = false;
	
	public Unicorn(Vec p, Node worldLink){
		super(new Animator(Res.UNICORN, stand), p, worldLink, false, CreatureType.UNICORN);
		hitradius = 200;
		maxSpeed = 5;
		animator.doOnReady = () -> donePunch();
		health = 25;
		coinDrop = 20;
		punchStrength = 4;
	}
	
	public int spitCount = 0;
	
	@Override
	public void update(float delta){
		if(g){
//			pos.y++;
//			accelerateFromGround(new Point(0, 0.001f));
			
			walkingAI(delta);
		} else {
			acc.shift(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision(true);
		}
		spitCount -= delta;
		super.update(delta);
	}
	
	public int dir = 0;
	@Override
	public void walkingAI(float dTime){
		if(!spitting){
			if((!Settings.agro || !findSarah()))wanderAbout();
			applyDirection(dir);
		} else {
			applyDirection(0);
		}
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
		if(mirrored){
			headX = pos.x + 50;
		} else {
			headX = pos.x - 50;
		}
		
		float distSquare = pos.minus(World.sarah.pos).lengthSqare();
		
		if(distSquare < 160000){
			if(distSquare < 22500){
				if(spitCount <= 0){
					animator.setAnimation(punch);
					WorldView.particleEffects.add(new RainbowSpit(headX, headY, this));
					spitting = true;
					spitCount = 1000;
				}
			}
			dir = 0;
			if(World.sarah.pos.x > pos.x + 20){
				dir = 1;
			} else if(World.sarah.pos.x < pos.x - 20){
				dir = -1;
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	@Override
	protected void beforeRender(){
		super.beforeRender();

		if(hit > 0){
			animator.setAnimation(hitt);
			spitting = false;
		} else if(!spitting){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
		
		if(g) alignWithGround();
	}
	
	public void renderHair(){
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);

		beforeRender();
		
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
		GL11.glColor3f(color[0], color[1], color[2]);
		animator.animate(mirrored);
		GL11.glColor3f(1, 1, 1);
//		if(Settings.shader){
//			Shader20.UNICORN.bind();
//				GL20.glUniform3f(GL20.glGetUniformLocation(Shader20.UNICORN.handle, "color"), color[0], color[1], color[2]);
//		}
		GL11.glPopMatrix();
	}
	
	int color = 0;
	boolean turnUp = true;
	int colorCounter;
	int[] sky = {0, 0, 100};
	
	@Override
	public void afterRender(){
//		if(Settings.shader) Shader20.bindNone();
	}
	
	@Override
	protected void onDeath(){
		Inventory.addItem(Item.horn);
		World.sarah.mana = 30;
		World.sarah.health = Math.max(World.sarah.health, 20);
		for(int i = 0; i < coinDrop; i++){
			WorldItem item = new WorldItem(Item.coin, new Vec(pos.x, pos.y+2), null);
			item.vel.set((0.5f - random.nextFloat())*10f, (random.nextFloat())*30f);
			World.items[Item.coin.id].add(item);
		}
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Unicorn u = new Unicorn(new Vec(x, y), worldLink);
		u.vel.set(vX, vY);
		u.health = health;
		return u;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
