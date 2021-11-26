import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {

	public static void main(String[] args) {
		// Uncomment this section and comment the other one to create a Solo Game
		train();
		run();
	}

	/**
	 * Entrena las soluciones.
	 */
	public static void train() {
		OnePlusOne evolutivo = new OnePlusOne();
		evolutivo.execute(100);
		System.err.println(evolutivo.chromosome.fitness);
	}

	/**
	 * Ejecuta el juego con un individuo entrenado.
	 */
	public static void run() {
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test0.json");
		gameRunner.start();
		// Simulate
		// gameRunner.simulate();
	}
}
