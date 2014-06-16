package world;

public class WorldRunnerThread extends Thread {
	
	public WorldRunnerThread() {
		super("WorldRunnerThread");
	}

	public void run() {
		WorldRunner worldRunner = new WorldRunner();
		try {
			worldRunner.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
