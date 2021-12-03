import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.*;

public class MuPlusLambda {

    List<Chromosome> chromosomes = new ArrayList<>(); // array of chromosomes, represents the population

    final int populationSize = 10; // size of the population
    final int lambda = 12;
    final double learningRate0 = 1 / Math.sqrt(2 * lambda);
    final double learningRate = 1 / Math.sqrt(2 * Math.sqrt(lambda));

    /**
     * Random inicialization of the population
     */
    public MuPlusLambda() {
        Chromosome c0 = new Chromosome("files/chromosome00.csv");
        c0.fitness = getFitness(c0);
        c0.writeChromosome("files/Mu_chromosome0.csv");
        this.chromosomes.add(c0);

        for (int i = 1; i < this.populationSize; i++) {
            Chromosome c = new Chromosome(this.chromosomes.get(i - 1));
            c.fitness = getFitness(c);
            c.writeChromosome("files/Mu_chromosome" + i + ".csv");
            this.chromosomes.add(c);
        }
    }

    /**
     * Ejecuta el entrenamiento un número de ciclos.
     * 
     * @param cicles: ciclos que ejecuta el entrenamiento
     */
    public void execute(int cicles) {
        Random random = new Random();
        for (int i = 0; i < cicles; i++) {
            // Desordena la lista para que los padres sean aleatorios pero se elijan todos
            Collections.shuffle(this.chromosomes);
            for (int j = 0; j < this.lambda; j++) {
                Chromosome newChromosome;

                // Crea un individuo con las parejas de padres adyacentes, si lambda es mayor,
                // elige random
                if (j < this.populationSize - 1)
                    newChromosome = new Chromosome(this.chromosomes.get(j), this.chromosomes.get(j + 1));
                else if (j == this.populationSize - 1)
                    newChromosome = new Chromosome(this.chromosomes.get(j), this.chromosomes.get(0));
                else {
                    int g1 = random.nextInt(this.populationSize);
                    int g2 = random.nextInt(this.populationSize);
                    newChromosome = new Chromosome(this.chromosomes.get(g1), this.chromosomes.get(g2));
                }

                // evaluation of the new individual
                newChromosome.fitness = getFitness(newChromosome);

                // adding the new individual into the population
                this.chromosomes.add(newChromosome);
            }

            // sort the new population based on fitness
            Collections.sort(this.chromosomes);

            // remove the last "lambda" indiviuduals from the population
            if (this.chromosomes.size() > this.populationSize) {
                this.chromosomes.subList(this.populationSize, this.chromosomes.size()).clear();
            }

            this.mutateVariance();
            this.chromosomes.get(0).writeChromosome("files/chromosome.csv");
            for (int j = 0; j < this.chromosomes.size(); j++)
                this.chromosomes.get(j).writeChromosome("files/Mu_chromosome" + j + ".csv");
        }
    }

    /**
     * Muta la varianza de los individuos de la lista
     */
    public void mutateVariance() {
        Random rand = new Random();

        // iterate over the whole population
        for (Chromosome chromosome : this.chromosomes) {
            // iterate over varianceDistance
            for (int j = 0; j < chromosome.varianceDistance.length; j++)
                chromosome.varianceDistance[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceDistance[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // iterate over varianceAngle
            for (int j = 0; j < chromosome.varianceAngle.length; j++)
                chromosome.varianceAngle[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceAngle[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // iterate over varianceThrust
            for (int j = 0; j < chromosome.varianceThrust.length; j++)
                for (int k = 0; k < chromosome.varianceThrust[j].length; k++)
                    chromosome.varianceThrust[j][k] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                            * chromosome.varianceThrust[j][k]
                            * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
        }
    }

    /**
     * Devuelve el fitness del individuo en función de la partida.
     * 
     * @param c: individuo a evaluar.
     * @return fitness: puntuación del individuo.
     */
    private float getFitness(Chromosome c) {
        // Inicia la ejecución del individuo y obtiene su fitness
        c.writeChromosome("files/chromosome.csv");

        int numMapas = 3;
        float totalFitness = 0;

        for (int i = 0; i < numMapas; i++) {
            SoloGameRunner gameRunner = new SoloGameRunner();
            gameRunner.setAgent(AgentEE.class);
            gameRunner.setTestCase("test" + i + ".json");
            GameResult gameRunnerResult = gameRunner.simulate();

            float fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
                    gameRunnerResult.metadata.split(":")[1].length() - 3));

            // Number of Checkpoints collected
            int numCheckpointCollected = gameRunnerResult.summaries.size()
                    - Collections.frequency(gameRunnerResult.summaries, "");

            totalFitness += fitness - 5 * numCheckpointCollected;
            System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);
        }

        System.err.println("Fitness: " + totalFitness / numMapas);
        return totalFitness / numMapas;
    }
}
