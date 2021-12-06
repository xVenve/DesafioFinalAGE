import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
import java.util.Collections;

public class SkeletonMain {

	public static void main(String[] args) {
		// Uncomment this section and comment the other one to create a Solo Game
		//trainMuPlusLambda();
		//trainOnePlusOne();
		test();
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
		// MuPlusLambda evolutivo = new MuPlusLambda(1);
		evolutivo.execute(50);
		System.err.println(evolutivo.chromosomes.get(0).fitness);
	}

	/**
	 * Ejecuta el juego con un individuo entrenado.
	 */
	public static void run() {
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test3.json");
		gameRunner.start();
		// Simulate
		// gameRunner.simulate();
	}


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
			} catch (Exception e){
				System.err.println(e);
			}

			totalFitness += fitness;
			allFitness[i] = fitness;
		}

		for (int i = 0; i < numMapas; i++) {
			System.err.println("Fitness " + i + ": " + allFitness[i]);
		}
		System.err.println("Mean fitness: " + totalFitness / numMapas);
	}

}
