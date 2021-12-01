import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.*;


public class MuPlusLambda {

    List <Chromosome> chromosomes = new ArrayList<>(); // array of chromosomes, represents the population

    final int populationSize = 10; // size of the population
    final int lambda = 12; 
    final double learningRate0 = 1 / Math.sqrt(2 * lambda); 
    final double learningRate = 1 / Math.sqrt(2 * Math.sqrt(lambda));

    /**
	 * Random inicialization of the population
	 */
    public MuPlusLambda() {
        Chromosome c0 = new Chromosome("files/chromosome2.csv");
        c0.fitness = getFitness(c0);
        c0.writeChromosome("files/Mu_chromosome0.csv");
        this.chromosomes.add(c0);

        for (int i = 1; i < this.populationSize; i++) {
            Chromosome c = new Chromosome(this.chromosomes.get(i - 1));
            c.writeChromosome("files/Mu_chromosome" + i + ".csv");
		    c.fitness = getFitness(c);
            this.chromosomes.add(c);
        }
    }

    public void execute(int cicles) {
        for (int i = 0; i < cicles; i++) {
            this.evolution();
            this.mutateVariance();
        }
    }

    /**
     * this method generates "lambda" new individuals, sort the population and select the best ones
     */
    public void evolution(){
		Random rand = new Random();
        //C-- Este método no es como execute, sólo sería para un ciclo
        //C-- La mayor parte del código sirve para inicializar un cromosoma, me dan ganas de meterlo en esa clase
        // generate as much individuals as lambda 
        for (int i = 0; i < this.lambda; i++) {

            // chromosome to overwrite
            //C-- Aquí renta crear un nuevo constructor a aprtir de dos padres
            //C-- Hay cruce pero no veo mutación.
            Chromosome newChromosome = new Chromosome("files/chromosome2.csv");

            // values of varianceDistance for the new individual
            for (int j = 0; j < this.chromosomes.get(i).varianceDistance.length; j++){
                if ( Math.floor(Math.random()*2) == 0){
                    newChromosome.varianceDistance[j] = this.chromosomes.get(i).varianceDistance[j];
                } else {
                    newChromosome.varianceDistance[j] = this.chromosomes.get(i+1).varianceDistance[j];
                }
            }

            // values of VarianceAngle for the new individual
            for (int j = 0; j < this.chromosomes.get(i).varianceAngle.length; j++){
                if ( Math.floor(Math.random()*2) == 0){
                    newChromosome.varianceAngle[j] = this.chromosomes.get(i).varianceAngle[j];
                } else {
                    newChromosome.varianceAngle[j] = this.chromosomes.get(i+1).varianceAngle[j];
                }
            }

            // values of varianceThrust for the new individual
            for (int j = 0; j < this.chromosomes.get(i).varianceThrust.length; j++){
                for (int k = 0; k < this.chromosomes.get(i).varianceThrust[j].length; k++){
                    if ( Math.floor(Math.random()*2) == 0){
                        newChromosome.varianceThrust[j][k] = this.chromosomes.get(i).varianceThrust[j][k];
                    } else {
                        newChromosome.varianceThrust[j][k] = this.chromosomes.get(i+1).varianceThrust[j][k];
                    }
                }    
            }
            
            // values for distanceRanges for the new individual
            for (int j = 0; j < this.chromosomes.get(i).distanceRanges.length; j++){
                double newGen = (this.chromosomes.get(i).distanceRanges[j] + this.chromosomes.get(i+1).distanceRanges[j]) / 2;
                newGen = Math.abs(newGen + rand.nextGaussian() * newChromosome.varianceDistance[j]);
                newChromosome.distanceRanges[j] = newGen;
            }
            
            // values for angleRanges for the new individual
            for (int j = 0; j < this.chromosomes.get(i).angleRanges.length; j++){
                double newGen = (this.chromosomes.get(i).angleRanges[j] + this.chromosomes.get(i+1).angleRanges[j]) / 2;
                newGen = Math.abs(newGen + rand.nextGaussian() * newChromosome.varianceAngle[j]);
                newGen = (newGen+360)%360;
                newChromosome.angleRanges[j] = newGen;
            }
            
            // values for thrustInRange for the new individual
            for (int j = 0; j < this.chromosomes.get(i).thrustInRange.length; j++){
                for (int k = 0; k < this.chromosomes.get(i).thrustInRange[j].length; k++){
                    double newGen = (this.chromosomes.get(i).thrustInRange[j][k] + this.chromosomes.get(i+1).thrustInRange[j][k]) / 2;
                    newGen = Math.abs(newGen + rand.nextGaussian() * newChromosome.varianceThrust[j][k]);
                    newGen = Math.min(newGen, 200);
                    newChromosome.thrustInRange[j][k] = newGen;
                }    
            }

            // evaluation of the new individual 
           newChromosome.fitness = getFitness(newChromosome);

           // adding the new individual into the population
           this.chromosomes.add(newChromosome);
        }
        
        // sort the new population based on fitness
        //C-- No sé si esto ordenará por fitness, en plan, cómo sabe que es por fitness? Por el compareTo en Chromosome?
        Collections.sort(this.chromosomes);

        /*
        for(int i=0; i<this.chromosomes.size(); i++){
            System.err.println(this.chromosomes.get(i).fitness);
        }
        int[] a = {};
        int b = a[0];
        */

        // remove the last "lambda" indiviuduals from the population
        for (int j = this.chromosomes.size() - 1; j >= this.populationSize; j--) {
            this.chromosomes.remove(j);
        }

        this.chromosomes.get(0).writeChromosome("files/chromosome1.csv");
    }
    

    public void mutateVariance(){
        //C-- Ok pero como cabezón lo pasaba a cromosoma, que no hace más que coger get(i)
        Random rand = new Random();

        // iterate over the whole population
        for (int i = 0; i < this.populationSize; i++) {

            // iterate over varianceDistance
            for (int j = 0; j < this.chromosomes.get(i).varianceDistance.length; j++){
			    this.chromosomes.get(i).varianceDistance[j]  = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0)) 
                                                            * this.chromosomes.get(i).varianceDistance[j]
                                                            * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
            }

            // iterate over varianceAngle
            for (int j = 0; j < this.chromosomes.get(i).varianceAngle.length; j++) {
                this.chromosomes.get(i).varianceAngle[j] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0)) 
                                                        * this.chromosomes.get(i).varianceAngle[j]
                                                        * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
            }
                
            // iterate over varianceThrust
            for (int j = 0; j < this.chromosomes.get(i).varianceThrust.length; j++){
                for (int k = 0; k < this.chromosomes.get(i).varianceThrust[j].length; k++){
                    this.chromosomes.get(i).varianceThrust[j][k] = (Math.pow(Math.E, rand.nextGaussian() * this.learningRate0)) 
                                                                * this.chromosomes.get(i).varianceThrust[j][k]
                                                                * (Math.pow(Math.E, rand.nextGaussian() * this.learningRate));
                }
                   
            }
                
        }
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
