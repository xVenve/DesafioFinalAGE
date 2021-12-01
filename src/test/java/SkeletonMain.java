import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {

	public static void main(String[] args) {
		// Uncomment this section and comment the other one to create a Solo Game
		trainMuPlusLambda();
		//run();
	}

	/**
	 * Entrena las soluciones.
	 */
	public static void trainOnePlusOne() {
		OnePlusOne evolutivo = new OnePlusOne();
		evolutivo.execute(100);
		System.err.println(evolutivo.chromosome.fitness);
	}

	/**
	 * Entrena las soluciones.
	 */
	public static void trainMuPlusLambda() {
		MuPlusLambda evolutivo = new MuPlusLambda();
		evolutivo.execute(2);
		System.err.println(evolutivo.chromosomes.get(0).fitness);
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
