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

        // Saving the initial individual and executing it
        this.chromosome.fitness = getFitness(this.chromosome);
        System.err.println(this.chromosome.fitness);
    }

    /**
     * Execute the 1+1 algorithm
     *
     * @param cicles: number of cycles the algorithm executes
     */
    public void execute(int cicles) {
        for (int i = 0; i < cicles; i++) {
            // Create the chromosome and get the fitness
            Chromosome hijo = new Chromosome(this.chromosome);
            hijo.fitness = getFitness(hijo);

            // Parent and child fitness assessment
            if (this.chromosome.fitness > hijo.fitness) {
                this.chromosome = hijo;
                this.num_mejoras++;
                this.chromosome.writeChromosome("files/One_chromosome" + i + ".csv");
            }
            this.chromosome.writeChromosome("files/chromosome.csv");

            // Variance mutation according to the 1/5 rule
            this.contador++;
            if (this.contador % this.ciclos1_5 == 0) {
                double v = (double) this.num_mejoras / this.ciclos1_5;
                mutateVariance(v);
                this.num_mejoras = 0;
            }
        }
    }

    /**
     * Mutate the variance of the chromosome when the 1/5 rule is satisfied.
     *
     * @param v: enhancement ratio per cycle.
     */
    private void mutateVariance(double v) {
        // Decide 'c' according to improvements between cycles
        double c = 1;
        if (v < (double) 1 / 5) c = 0.82;
        else if (v > (double) 1 / 5) c = 1 / 0.82;

        // Mutate all variances
        for (int i = 0; i < this.chromosome.varianceDistance.length; i++)
            this.chromosome.varianceDistance[i] *= c;
        for (int i = 0; i < this.chromosome.varianceAngle.length; i++)
            this.chromosome.varianceAngle[i] *= c;
        for (int i = 0; i < this.chromosome.varianceThrust.length; i++)
            for (int j = 0; j < this.chromosome.varianceThrust[i].length; j++)
                this.chromosome.varianceThrust[i][j] *= c;
    }

    /**
     * Returns the individual's fitness as a function of the maps on which he/she is trained on.
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
