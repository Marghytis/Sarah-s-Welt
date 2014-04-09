package world;

import java.util.Random;

public class Cloud{

	Point position, groesse;

	Random random = new Random();
	
	public Cloud(int sectorX){
		for(Sector s: World.sectors){
			for(int i = 0; i < Material.values().length; i++){
			}
		}
		position.set(sectorX*Sector.WIDTH + random.nextInt(Sector.WIDTH), 480 + random.nextInt(600));
	}
	
}
