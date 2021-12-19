import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class SkeletonMain {

	public static void main(String[] args) {
		trainOnePlusOne();
		// trainMuPlusLambda();
		// test();
		// run();
	}

	/**
	 * Entrena las soluciones con 1+1.
	 */
	public static void trainOnePlusOne() {
		OnePlusOne evolutivo = new OnePlusOne();
		evolutivo.execute(200);
		System.err.println(evolutivo.chromosome.fitness);
	}

	/**
	 * Entrena las soluciones con Mu+Lambda.
	 */
	public static void trainMuPlusLambda() {
		// MuPlusLambda evolutivo = new MuPlusLambda();
		MuPlusLambda evolutivo = new MuPlusLambda(0);
		evolutivo.execute(20);
		System.err.println(evolutivo.chromosomes.get(0).fitness);
	}

	/**
	 * Ejecuta el juego con un individuo entrenado en un mapa.
	 */
	public static void run() {
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test10.json");
		gameRunner.start();
	}

	/**
	 * Comprueba las puntuaciones de cada mapa y la media para
	 * "files/chromosome.csv"
	 */
	public static void test() {
		int numMapas = 15;
		float totalFitness = 0;
		float[] allFitness = new float[numMapas];

		for (int i = 0; i < numMapas; i++) {
			float fitness = 1000;

			try {
				SoloGameRunner gameRunner = new SoloGameRunner();
				gameRunner.setAgent(AgentEE.class);
				gameRunner.setTestCase("test" + i + ".json");
				GameResult gameRunnerResult = gameRunner.simulate();

				fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
						gameRunnerResult.metadata.split(":")[1].length() - 3));
			} catch (Exception e) {
				System.err.println(e);
			}

			totalFitness += fitness;
			allFitness[i] = fitness;
		}

		for (int i = 0; i < numMapas; i++)
			System.err.println("Fitness " + i + ": " + allFitness[i]);
		System.err.println("Mean fitness: " + totalFitness / numMapas);
	}

}
