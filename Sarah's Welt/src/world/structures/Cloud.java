package world.structures;

import org.lwjgl.opengl.GL11;

import util.Animation;
import world.Node;
import world.Point;
import world.particles.Particle.ParticleType;
import world.particles.ParticleSpawner;

public class Cloud extends Structure{

	private ParticleSpawner spawner;
	
	public Cloud(Point pos, Node worldLink, float xSize, float ySize){
		super(Structure.CLOUD, new Animation(1, 1), pos, worldLink);
		box.x*=xSize;
		box.y*=ySize;
		box.width*=xSize;
		box.width*=ySize;
		spawner = new ParticleSpawner(ParticleType.RAIN, 1000);
	}
	
	@Override
	public void tick(float dTime){
		pos.x += 0.1;
		int count = (int)box.width/150;
		for(int i = 0; i < count; i++){
			float x = pos.x + box.x + 50 + random.nextInt((int)(box.width-100));
			spawner.createParticle(x, pos.y + box.y + (box.height/2), random.nextFloat()*0.01f-0.003f, -0.4f);
		}
		spawner.update((int)dTime);
		System.out.println("test");
	}
	
	public void render(){
		spawner.render();
		GL11.glColor4f(1, 1, 1, 1);
		super.render();
	}
}
