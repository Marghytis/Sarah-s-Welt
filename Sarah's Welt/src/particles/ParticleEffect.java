package particles;

public interface ParticleEffect {

	public void tick(int delta);
	
	public void render();
	
	public void finalize();
	
}
