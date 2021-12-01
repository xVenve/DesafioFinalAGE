//Al salir de 1+1, ordenar de menor a mayor los rangos de la distancia y los angulos

import java.util.Collections;

import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class OnePlusOne {

	Chromosome chromosome;
	int contador = 0;
	int num_mejoras = 0;
	int ciclos1_5 = 10;

	/**
	 * Crea el primer individuo
	 */
	public OnePlusOne() {
		// this.chromosome = new Chromosome(3, 6);
		this.chromosome = new Chromosome("files/chromosome0.csv");

		// Guardado del individuo inicial y ejecución del mismo
		this.chromosome.writeChromosome("files/chromosome1.csv");
		this.chromosome.fitness = getFitness(this.chromosome);
		System.err.println(this.chromosome.fitness);
	}

	/**
	 * Ejecuta el algoritmo 1+1
	 * 
	 * @param cicles: número de ciclos que se ejecuta el algoritmo
	 */
	public void execute(int cicles) {
		int cont_mejor = 0;
		for (int i = 0; i < cicles; i++) {
			// Creamos el cromosoma y los escribimos para obtener el fitness
			Chromosome hijo = new Chromosome(this.chromosome);
			hijo.fitness = getFitness(hijo);

			// Evaluación del fitness padre e hijo
			if (this.chromosome.fitness > hijo.fitness) {
				this.chromosome = hijo;
				this.num_mejoras++;
				this.chromosome.writeChromosome("files/chromosomeX" + cont_mejor + ".csv");
				cont_mejor++;
			} else {
				this.chromosome.writeChromosome("chromosome.csv");
			}

			// Mutacion de varianza
			this.contador++;
			if (this.contador % this.ciclos1_5 == 0) {
				double v = (double) this.num_mejoras / this.ciclos1_5;
				mutacionVarianza(v);
				this.num_mejoras = 0;
			}
		}
		this.chromosome.writeChromosome("files/chromosome1.csv");
	}

	/**
	 * Muta la varianza del cromosoma cuando cumple la regla del 1/5
	 * 
	 * @param v: relación de mejora por ciclo.
	 */
	private void mutacionVarianza(double v) {
		double c = 1;
		if (v < (double) 1 / 5) c = 0.82;
		else if (v > (double) 1 / 5) c = 1 / 0.82;

		for (int i = 0; i < this.chromosome.varianceDistance.length; i++)
			this.chromosome.varianceDistance[i] *= c;
		for (int i = 0; i < this.chromosome.varianceAngle.length; i++)
			this.chromosome.varianceAngle[i] *= c;
		for (int i = 0; i < this.chromosome.varianceThrust.length; i++)
			for (int j = 0; j < this.chromosome.varianceThrust[i].length; j++)
				this.chromosome.varianceThrust[i][j] *= c;
	}

	/**
	 * Devuelve el fitness del individuo en función de la partida.
	 * 
	 * @return fitness: puntuación del individuo.
	 */
	private float getFitness(Chromosome c) {
		// Inicia la ejecución del individuo y obtiene su fitness
		c.writeChromosome("files/chromosome.csv");
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test0.json");
		GameResult gameRunnerResult = gameRunner.simulate();

		// Basic Fitness function, game score
		float fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
				gameRunnerResult.metadata.split(":")[1].length() - 3));

		// Number of Checkpoints collected
		int numCheckpointCollected = gameRunnerResult.summaries.size()
				- Collections.frequency(gameRunnerResult.summaries, "");

		System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);
		return fitness - 5 * numCheckpointCollected;
	}
}
