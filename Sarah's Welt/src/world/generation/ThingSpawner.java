package world.generation;

import java.util.Random;

import world.Node;

public class ThingSpawner {
	Spawner spawner;
	int maxCreatsPerLine;
	int probInPercent;
	
	public ThingSpawner(Spawner spawner, int maxAmount, int prob){
		this.spawner = spawner;
		this.maxCreatsPerLine = maxAmount;
		this.probInPercent = prob;
	}
	
	public void doYourTask(Node n, Random random){
		int perc = probInPercent;
		int luck = random.nextInt(1000);
		for(int i = 0; i < maxCreatsPerLine; i++){
			if(luck < perc){
				spawner.spawn(n, random);
				perc = (int)(perc * (perc/1000.0f));
			} else {
				break;
			}
		}
	}
	public interface Spawner {public abstract void spawn(Node node, Random random);}
}