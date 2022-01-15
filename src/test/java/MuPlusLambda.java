import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MuPlusLambda {

    final int populationSize = 10; // Population size
    final int lambda = 10; // New population size
    final double learningRate0 = 1 / Math.sqrt(2 * lambda);
    final double learningRate = 1 / Math.sqrt(2 * Math.sqrt(lambda));
    List<Chromosome> chromosomes = new ArrayList<>(); // Array of chromosomes representing the population

    /**
     * Random initialisation of a population with a single base chromosome.
     */
    public MuPlusLambda() {
        // Collect the first chromosome.
        Chromosome c0 = new Chromosome("files/chromosome00.csv");
        c0.fitness = getFitness(c0);
        c0.writeChromosome("files/Mu_chromosome0.csv");
        this.chromosomes.add(c0);

        // Create a new chromosome by mutating the previous chromosome in the population.
        for (int i = 1; i < this.populationSize; i++) {
            Chromosome c = new Chromosome(this.chromosomes.get(i - 1));
            c.fitness = getFitness(c);
            c.writeChromosome("files/Mu_chromosome" + i + ".csv");
            this.chromosomes.add(c);
        }
    }

    /**
     * Initialise with the population from the previous experiment to continue.
     * Reduces the variance to simulate a previous decline. If starting from 0
     * Take the originals
     *
     * @param prevCicles: number of cycles to simulate that the variance has decreased.
     */
    public MuPlusLambda(int prevCicles) {
        // If prevCicles is 0, initialise the population according to some original files.
        // original
        if (prevCicles == 0)
            for (int i = 0; i < this.populationSize; i++) {
                Chromosome c = new Chromosome("files/Mu_chromosome0" + i + ".csv");
                c.fitness = getFitness(c);
                this.chromosomes.add(c);
            }
        // If an experiment has already been done before, the last population is collected.
        else
            for (int i = 0; i < this.populationSize; i++) {
                Chromosome c = new Chromosome("files/Mu_chromosome" + i + ".csv");
                c.fitness = getFitness(c);
                this.chromosomes.add(c);
            }

        // The variance is mutated (decremented) according to the number of cycles that the last
        // experiment
        for (int i = 0; i < prevCicles; i++)
            mutateVariance();
    }

    /**
     * Run the training for a number of cycles.
     *
     * @param cicles: cycles that the training runs.
     */
    public void execute(int cicles) {
        Random random = new Random();
        for (int i = 0; i < cicles; i++) {
            // Disorder the list so that the parents are randomised but all parents are chosen
            Collections.shuffle(this.chromosomes);
            for (int j = 0; j < this.lambda; j++) {
                Chromosome newChromosome;

                // Create an individual with adjacent parent pairs, if lambda is greater,
                // choose random
                if (j < this.populationSize - 1)
                    newChromosome = new Chromosome(this.chromosomes.get(j), this.chromosomes.get(j + 1));
                else if (j == this.populationSize - 1)
                    newChromosome = new Chromosome(this.chromosomes.get(j), this.chromosomes.get(0));
                // If all parents have already been used, the rest will be randomised.
                else {
                    int g1 = random.nextInt(this.populationSize);
                    int g2 = random.nextInt(this.populationSize);
                    newChromosome = new Chromosome(this.chromosomes.get(g1), this.chromosomes.get(g2));
                }

                // Assessment and addition of the new individual
                newChromosome.fitness = getFitness(newChromosome);
                this.chromosomes.add(newChromosome);
            }

            // Sort the population according to fitness
            Collections.sort(this.chromosomes);

            // Removes the last 'lambda' individuals to keep the original size
            if (this.chromosomes.size() > this.populationSize)
                this.chromosomes.subList(this.populationSize, this.chromosomes.size()).clear();

            // Mutate variances
            this.mutateVariance();

            // Write the resulting population, the best one also writes it to
            // "files/chromosome.csv".
            this.chromosomes.get(0).writeChromosome("files/chromosome.csv");
            for (int j = 0; j < this.chromosomes.size(); j++)
                this.chromosomes.get(j).writeChromosome("files/Mu_chromosome" + j + ".csv");
        }
    }

    /**
     * Mutes the variance of the individuals in the list
     */
    public void mutateVariance() {
        Random rand = new Random();

        // Itera on the population
        for (Chromosome chromosome : this.chromosomes) {
            // Mutate the variance of distances
            for (int j = 0; j < chromosome.varianceDistance.length; j++)
                chromosome.varianceDistance[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceDistance[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // Mutate the variance of angles
            for (int j = 0; j < chromosome.varianceAngle.length; j++)
                chromosome.varianceAngle[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceAngle[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // Mutate the variance of velocities
            for (int j = 0; j < chromosome.varianceThrust.length; j++)
                for (int k = 0; k < chromosome.varianceThrust[j].length; k++)
                    chromosome.varianceThrust[j][k] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                            * chromosome.varianceThrust[j][k]
                            * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
        }
    }

    /**
     * Returns the individual's fitness as a function of the maps on which he/she is
     * trained on.
     *
     * @param c: individual to be evaluated.
     * @return fitness: individual's score.
     */
    private float getFitness(Chromosome c) {
        // Initiates the execution of the individual and obtains his fitness
        c.writeChromosome("files/chromosome.csv");

        // Numbers of the maps to be trained on
        int[] numMapas = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        float totalFitness = 0;

        for (int numMapa : numMapas) {
            float fitness = 1000;
            int numCheckpointCollected = 0;

            // Gets the map score
            // Sometimes it does not give a correct value, in this case the fitness will be 1000
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

            // Fitness is improved for each checkpoint touched and added to the total fitness.
            totalFitness += fitness - 5 * numCheckpointCollected;
            System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);
        }

        // The average total fitness across maps is returned.
        System.err.println("Fitness: " + totalFitness / numMapas.length);
        return totalFitness / numMapas.length;
    }
}
