import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
import java.util.Collections;

public class SkeletonMain {

	public static void main(String[] args) {
		// Uncomment this section and comment the other one to create a Solo Game
		// trainMuPlusLambda();
		//trainOnePlusOne();
		//run();
		test();
	}

	/**
	 * Entrena las soluciones.
	 */
	public static void trainOnePlusOne() {
		OnePlusOne evolutivo = new OnePlusOne();
		evolutivo.execute(0);
		System.err.println(evolutivo.chromosome.fitness);
	}

	/**
	 * Entrena las soluciones.
	 */
	public static void trainMuPlusLambda() {
		MuPlusLambda evolutivo = new MuPlusLambda();
		// MuPlusLambda evolutivo = new MuPlusLambda(1);
		evolutivo.execute(10);
		System.err.println(evolutivo.chromosomes.get(0).fitness);
	}

	/**
	 * Ejecuta el juego con un individuo entrenado.
	 */
	public static void run() {
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test14.json");
		gameRunner.start();
		// Simulate
		// gameRunner.simulate();
	}


	public static void test() {
		int numMapas = 15;
		float totalFitness = 0;
		float[] allFitness = new float[numMapas];

		for (int i = 0; i < numMapas; i++) {
			SoloGameRunner gameRunner = new SoloGameRunner();
			gameRunner.setAgent(AgentEE.class);
			gameRunner.setTestCase("test" + i + ".json");
			GameResult gameRunnerResult = gameRunner.simulate();

			float fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
					gameRunnerResult.metadata.split(":")[1].length() - 3));

			totalFitness += fitness;
			allFitness[i] = fitness;
		}

		for (int i = 0; i < numMapas; i++) {
			System.err.println("Fitness " + i + ": " + allFitness[i]);
		}
		System.err.println("Mean fitness: " + totalFitness / numMapas);
	}

}
