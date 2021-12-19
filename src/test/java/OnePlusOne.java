import java.util.Collections;

import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.Collections;

public class OnePlusOne {

	Chromosome chromosome;
	int contador = 0;
	int num_mejoras = 0;
	int ciclos1_5 = 10;

	/**
	 * Crea el primer individuo a partir de una codificación previa.
	 */
	public OnePlusOne() {
		this.chromosome = new Chromosome("files/chromosome00.csv");

		// Guardado del individuo inicial y ejecución del mismo
		this.chromosome.fitness = getFitness(this.chromosome);
		System.err.println(this.chromosome.fitness);
	}

	/**
	 * Ejecuta el algoritmo 1+1
	 * 
	 * @param cicles: número de ciclos que se ejecuta el algoritmo
	 */
	public void execute(int cicles) {
		for (int i = 0; i < cicles; i++) {
			// Crea el cromosoma y obtiene el fitness
			Chromosome hijo = new Chromosome(this.chromosome);
			hijo.fitness = getFitness(hijo);

			// Evaluación del fitness padre e hijo
			if (this.chromosome.fitness > hijo.fitness) {
				this.chromosome = hijo;
				this.num_mejoras++;
				this.chromosome.writeChromosome("files/One_chromosome" + i + ".csv");
			}
			this.chromosome.writeChromosome("files/chromosome.csv");

			// Mutación de varianza según la regla 1/5
			this.contador++;
			if (this.contador % this.ciclos1_5 == 0) {
				double v = (double) this.num_mejoras / this.ciclos1_5;
				mutateVariance(v);
				this.num_mejoras = 0;
			}
		}
	}

	/**
	 * Muta la varianza del cromosoma cuando cumple la regla del 1/5
	 * 
	 * @param v: relación de mejora por ciclo.
	 */
	private void mutateVariance(double v) {
		// Decide 'c' según las mejoras entre ciclos
		double c = 1;
		if (v < (double) 1 / 5) c = 0.82;
		else if (v > (double) 1 / 5) c = 1 / 0.82;

		// Muta todas las varianzas
		for (int i = 0; i < this.chromosome.varianceDistance.length; i++)
			this.chromosome.varianceDistance[i] *= c;
		for (int i = 0; i < this.chromosome.varianceAngle.length; i++)
			this.chromosome.varianceAngle[i] *= c;
		for (int i = 0; i < this.chromosome.varianceThrust.length; i++)
			for (int j = 0; j < this.chromosome.varianceThrust[i].length; j++)
				this.chromosome.varianceThrust[i][j] *= c;
	}

	/**
	 * Devuelve el fitness del individuo en función de los mapas sobre los que se
	 * entrene.
	 *
	 * @param c: individuo a evaluar.
	 * @return fitness: puntuación del individuo.
	 */
	private float getFitness(Chromosome c) {
		// Inicia la ejecución del individuo y obtiene su fitness
		c.writeChromosome("files/chromosome.csv");

		// Números de los mapas sobre los que se va a entrenar
		int[] numMapas = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
		float totalFitness = 0;

		for (int numMapa : numMapas) {
			float fitness = 1000;
			int numCheckpointCollected = 0;

			// Obtiene la puntuación del mapa
			// En ocasiones no da un valor correcto, en tal caso el fitness será 1000
			try {
				SoloGameRunner gameRunner = new SoloGameRunner();
				gameRunner.setAgent(AgentEE.class);
				gameRunner.setTestCase("train" + numMapa + ".json");
				GameResult gameRunnerResult = gameRunner.simulate();

				fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
						gameRunnerResult.metadata.split(":")[1].length() - 3));

				// Number of Checkpoints collected
				numCheckpointCollected = gameRunnerResult.summaries.size()
						- Collections.frequency(gameRunnerResult.summaries, "");
			} catch (Exception e) {
				System.err.println(e);
			}

			// Se mejora el fitness por cada checkpoint tocado y se suma al fitness total
			totalFitness += fitness - 5 * numCheckpointCollected;
			System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);
		}

		// Se devuelve la media del fitness total entre los mapas
		System.err.println("Fitness: " + totalFitness / numMapas.length);
		return totalFitness / numMapas.length;
	}
}
