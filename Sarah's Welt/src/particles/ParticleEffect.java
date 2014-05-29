package particles;

public interface ParticleEffect {

	public void tick(float delta);
	
	public void render();
	
	public void finalize();
	
	public boolean living();
}
