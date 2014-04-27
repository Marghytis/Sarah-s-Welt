package world;

public class WorldLoader {

	public static SectorLoader right = new SectorLoader(true);
	public static SectorLoader left = new SectorLoader(false);
	
	static WorldDatabase database;
	
	public static boolean ready(boolean right){
		SectorLoader thisTime = right ? WorldLoader.right : left;
		if(thisTime.ready){
			thisTime.ready = false;
			return true;
		}
		return false;
	}
	
	public static void load(int x, boolean right){
		SectorLoader thisTime = right ? WorldLoader.right : left;
		thisTime.ready = false;
		if(WorldWindow.generator.rim(x)){
			new Thread(thisTime.gen).start();
		} else {
			thisTime.sector = new Sector(x);
			System.out.println("Er lädt???? " + x);
			new Thread(thisTime.load).start();
		}
	}
	
	public static class SectorLoader {
		public Sector sector;
		public boolean ready;
		boolean right;
		
		public SectorLoader(boolean right){
			this.right = right;
		}
		
		Runnable load = () -> {
//			database.loadSector(sector);
			ready = true;
		};
		
		Runnable gen = () -> {
			if(right){
				sector = WorldWindow.generator.generateRight();
			} else {
				sector = WorldWindow.generator.generateLeft();
			}
			ready = true;
		};
	}
}
