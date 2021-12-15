import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.*;

public class MuPlusLambda {

    List<Chromosome> chromosomes = new ArrayList<>(); // Array de cromosomas que representa la población

    final int populationSize = 10; // Tamaño de la población
    final int lambda = 10; // Tamaño de la nueva población
    final double learningRate0 = 1 / Math.sqrt(2 * lambda);
    final double learningRate = 1 / Math.sqrt(2 * Math.sqrt(lambda));

    /**
     * Inicialización aleatoria de una población con un solo cromosoma base.
     */
    public MuPlusLambda() {
        // Recoge el primer cromosoma.
        Chromosome c0 = new Chromosome("files/chromosome00.csv");
        c0.fitness = getFitness(c0);
        c0.writeChromosome("files/Mu_chromosome0.csv");
        this.chromosomes.add(c0);

        // Crea un nuevo cromosoma mutando el anterior de la población.
        for (int i = 1; i < this.populationSize; i++) {
            Chromosome c = new Chromosome(this.chromosomes.get(i - 1));
            c.fitness = getFitness(c);
            c.writeChromosome("files/Mu_chromosome" + i + ".csv");
            this.chromosomes.add(c);
        }
    }

    /**
     * Inicializa con la población del experimento anterior para continuar.
     * Reduce la varianza para que simule un descenso previo. Si se empieza de 0 coge los originales
     *
     * @param prevCicles: número de ciclos que simule que ha descendido la varianza.
     */
    public MuPlusLambda(int prevCicles) {
        // Si los ciclos previos son 0, inicializa la población según unos ficheros originales
        if(prevCicles==0)
            for (int i=0; i<this.populationSize; i++){
                Chromosome c = new Chromosome("files/Mu_chromosome0"+i+".csv");
                c.fitness = getFitness(c);
                this.chromosomes.add(c);
            }
        // Si ya se ha hecho antes un experimento, se recoge la última población
        else
            for (int i=0; i<this.populationSize; i++){
                Chromosome c = new Chromosome("files/Mu_chromosome"+i+".csv");
                c.fitness = getFitness(c);
                this.chromosomes.add(c);
            }

        // Se muta (decrementa) la varianza según los ciclos que tuvo el último experimento
        for (int i=0; i<prevCicles; i++)
            mutateVariance();
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
                // Si ya se han usado todos los padres, el resto serán aleatorios.
                else {
                    int g1 = random.nextInt(this.populationSize);
                    int g2 = random.nextInt(this.populationSize);
                    newChromosome = new Chromosome(this.chromosomes.get(g1), this.chromosomes.get(g2));
                }

                // Evaluación y adición del nuevo individuo
                newChromosome.fitness = getFitness(newChromosome);
                this.chromosomes.add(newChromosome);
            }

            // Ordena la población según el fitness
            Collections.sort(this.chromosomes);

            // Elimina los últimos 'lambda' individuos para quedarse con el tamaño original
            if (this.chromosomes.size() > this.populationSize)
                this.chromosomes.subList(this.populationSize, this.chromosomes.size()).clear();

            // Muta las varianzas
            this.mutateVariance();

            // Escribe la población resultante, el mejor también lo escribe en "files/chromosome.csv"
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

        // Itera sobre la población
        for (Chromosome chromosome : this.chromosomes) {
            // Muta la varianza de distancias
            for (int j = 0; j < chromosome.varianceDistance.length; j++)
                chromosome.varianceDistance[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceDistance[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // Muta la varianza de ángulos
            for (int j = 0; j < chromosome.varianceAngle.length; j++)
                chromosome.varianceAngle[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                        * chromosome.varianceAngle[j] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));

            // Muta la varianza de velocidades
            for (int j = 0; j < chromosome.varianceThrust.length; j++)
                for (int k = 0; k < chromosome.varianceThrust[j].length; k++)
                    chromosome.varianceThrust[j][k] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0))
                            * chromosome.varianceThrust[j][k] * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
        }
    }

    /**
     * Devuelve el fitness del individuo en función de los mapas sobre los que se entrene.
     *
     * @param c:	individuo a evaluar.
     * @return fitness: puntuación del individuo.
     */
    private float getFitness(Chromosome c) {
        // Inicia la ejecución del individuo y obtiene su fitness
        c.writeChromosome("files/chromosome.csv");

        // Números de los mapas sobre los que se va a entrenar
        int [] numMapas = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
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
