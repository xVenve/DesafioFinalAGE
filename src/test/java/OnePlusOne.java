//Al salir de 1+1, ordenar de menor a mayor los rangos de la distancia y los angulos

import java.util.Collections;
import java.util.Random;

import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class OnePlusOne {

	Chromosome chromosome;
	int contador = 0;
	int num_mejoras = 0;
	float eval_mutado;
	int ciclos1_5 = 10;

	// Si no se pasan parametros iniciliza todo aleatoriamente
	public OnePlusOne() {
		this.initialize();
	}

	/**
	 * Devuelve el fitness del individuo en función de la partida.
	 * 
	 * @param gameRunnerResult: resultado de la partida.
	 * @return fitness: puntuación del individuo.
	 */
	public float getFitness(GameResult gameRunnerResult) {
		// Basic Fitness function, game score
		float fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
				gameRunnerResult.metadata.split(":")[1].length() - 3));

		// Number of Checkpoints collected
		int numCheckpointCollected = gameRunnerResult.summaries.size()
				- Collections.frequency(gameRunnerResult.summaries, "");

		System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);

		return fitness - 5 * numCheckpointCollected;
	}

	/**
	 * Inicializa el individuo con valores aleatorios.
	 */
	public void initialize() {

		// this.chromosome = new Chromosome(3, 6);
		this.chromosome = new Chromosome("files/chromosome0.csv"); // TRAMPITAS

		// Guardado del individuo inicial y ejecución del mismo
		this.chromosome.writeChromosome("chromosome.csv");
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test0.json");
		GameResult gameRunnerResult = new GameResult();
		gameRunnerResult = gameRunner.simulate();
		this.chromosome.fitness = getFitness(gameRunnerResult);
	}

	public void mutacion() {
		// Creamos el cromosoma y los escribimos para obtener el fitness
		Chromosome hijo = new Chromosome(this.chromosome);
		hijo.writeChromosome("chromosome.csv");

		// Calcular fitness hijo
		SoloGameRunner gameRunner = new SoloGameRunner();
		gameRunner.setAgent(AgentEE.class);
		gameRunner.setTestCase("test0.json");
		GameResult gameRunnerResult = new GameResult();
		gameRunnerResult = gameRunner.simulate();
		hijo.fitness = getFitness(gameRunnerResult);

		// Evaluación del fitness padre e hijo
		if (chromosome.fitness > hijo.fitness) {
			chromosome.copyChromosome(hijo);
			this.num_mejoras++;
		} else {
			this.chromosome.writeChromosome("chromosome.csv");
		}

		// Mutacion de varianza
		this.contador++;
		if (this.contador % this.ciclos1_5 == 0) {
			mutacionVarianza(this.num_mejoras / this.ciclos1_5);
			this.num_mejoras = 0;
		}
	}

	public void mutacionVarianza(double v) {
		double c = 1;

		if (v < 1 / 5) {
			c = 0.82;
		} else if (v > 1 / 5) {
			c = 1 / 0.82;
		}

		for (int i = 0; i < this.chromosome.varianceDistance.length; i++)
			this.chromosome.varianceDistance[i] *= c;
		for (int i = 0; i < this.chromosome.varianceAngle.length; i++)
			this.chromosome.varianceAngle[i] *= c;
		for (int i = 0; i < this.chromosome.varianceThrust.length; i++)
			for (int j = 0; j < this.chromosome.varianceThrust[i].length; j++)
				this.chromosome.varianceThrust[i][j] *= c;
	}

	public void execute(int cicles) {
		for (int i = 0; i < cicles; i++) {
			this.mutacion();
		}

	}

}
