//Al salir de 1+1, ordenar de menor a mayor los rangos de la distancia y los angulos

import com.codingame.gameengine.runner.dto.GameResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Arrays;
import java.util.Random;
import com.codingame.gameengine.runner.SoloGameRunner;

public class OnePlusOne {

  Chromosome chromosome;
  int contador;
  int num_mejoras;
  float fitness;
  float eval_mutado;

  int ciclos1_5 = 10;


  /**
   * Devuelve el fitness del individuo en función de la partida.
   * @param gameRunnerResult: resultado de la partida.
   * @return fitness: puntuación del individuo.
   */
  public float getFitness(GameResult gameRunnerResult) {
    // Basic Fitness function, game score
    float fitness = Float.parseFloat(
      gameRunnerResult.metadata.split(":")[1].substring(
          1, gameRunnerResult.metadata.split(":")[1].length() - 3));

    // Number of Checkpoints collected
    int numCheckpointCollected =
      gameRunnerResult.summaries.size() -
      Collections.frequency(gameRunnerResult.summaries, "");

    System.err.println(
      "Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected
    );

    return fitness - 5 * numCheckpointCollected;
  }

 
   public void initialize(){
    Random rand = new Random();
    int meanDis = 0;
    int standardDesviationDis = 1350;
    int meanAng = 5;
    int standardDesviationAng = 100;
    int meanThrust = 0;
    int standardDesviationThrust = 75;
    //Inicialización vector de varianzas de distancias 
    for (int i = 0; i< chromosome.varianceDistance.length; i++){
      chromosome.varianceDistance[i] = meanDis + rand.nextGaussian() * standardDesviationDis;
    }

    //Inicialización vector de varianzas de angulos 
    for (int i = 0; i< chromosome.varianceAngle.length; i++){
      chromosome.varianceAngle[i] = meanAng + rand.nextGaussian() * standardDesviationAng;
    }

    //Inicialización matriz de varianzas velocidades
    for (int i = 0; i< chromosome.varianceThrust.length; i++){
     for (int j = 0; j< chromosome.varianceThrust[0].length; j++){
          chromosome.varianceThrust[i][j] = Math.abs(meanThrust + rand.nextGaussian() * standardDesviationThrust);
     }
    }
    
    this.contador = 0;
    this.num_mejoras = 0;

    //Inicialización del vector de rangos de distancias 
    double distanceMin = 0.0;
    double distanceMax = 4000.0;

    for (int i = 0; i < chromosome.distanceRanges.length; i++){
      chromosome.distanceRanges[i] = distanceMin + (distanceMax-distanceMin) * rand.nextDouble();
    }

   //Inicialización del vector de rangos de angulos 
    double angleMin = 0.0;
    double angleMax = 360.0;   

    for (int i = 0; i < chromosome.angleRanges.length; i++){
      chromosome.angleRanges[i] = angleMin + (angleMax-angleMin) * rand.nextDouble();
    }

    //Inicialización matriz de aceleración 
    double [][] thrustInRange = new double [7][4];
    double thrustMin = 0.0;
    double thrustMax = 200.0;
    for (int i = 0; i < thrustInRange.length; i++){
      for (int j = 0; j < thrustInRange[i].length; j++){
        thrustInRange[i][j] = thrustMin + (thrustMax - thrustMin) * rand.nextDouble();
      }
    }

    //Asignacion de los valores aleatorizados a un objeto de tipo chromosome
    Chromosome c = new Chromosome(chromosome.distanceRanges, chromosome.angleRanges, chromosome.thrustInRange, chromosome.varianceDistance.length, chromosome.varianceAngle.length);
    
    // c = new Chromosome("files/chromosome0.csv");  // TRAMPITAS

    c.writeChromosome("chromosome.csv");

    SoloGameRunner gameRunner = new SoloGameRunner();
    gameRunner.setAgent(AgentEE.class);
    gameRunner.setTestCase("test0.json");
    GameResult gameRunnerResult = new GameResult();
    gameRunnerResult = gameRunner.simulate();
    this.fitness = getFitness(gameRunnerResult);
    this.chromosome = c;
    

  }
  
  public void mutacion(){
	Random rand = new Random();
    //Chromosome mutado = this.chromosome.clone;
	Chromosome mutado = new Chromosome (this.chromosome);
/*
    for (int i = 0; i < this.chromosome.distanceRanges.length; i++){
      mutado.distanceRanges[i] = this.chromosome.distanceRanges[i] + rand.nextGaussian() * this.chromosome.variance;
      Arrays.sort(mutado.distanceRanges);
    }
    
    for (int i = 0; i < this.chromosome.angleRanges.length; i++){
      mutado.angleRanges[i] = (this.chromosome.angleRanges[i] + rand.nextGaussian() * this.chromosome.variance)%360;
      Arrays.sort(mutado.angleRanges);
    }

    for (int i = 0; i < this.chromosome.thrustInRange.length; i++){
      for (int j = 0; j< this.chromosome.thrustInRange[i].length; j++){
        mutado.thrustInRange[i][j] = this.chromosome.thrustInRange[i][j] + rand.nextGaussian() * this.chromosome.variance;
      }
    }
    this.prueba = mutado;
    mutado.writeChromosome("chromosome.csv");
*/
    SoloGameRunner gameRunner = new SoloGameRunner();
    gameRunner.setAgent(AgentEE.class);
    gameRunner.setTestCase("test0.json");
    GameResult gameRunnerResult = new GameResult();
    gameRunnerResult = gameRunner.simulate();
    float fitness_mutado = getFitness(gameRunnerResult);
    this.eval_mutado = fitness_mutado;

    if (this.fitness > fitness_mutado){
      this.fitness = fitness_mutado;
      this.chromosome = mutado;
      this.num_mejoras++;
    }else{
      this.chromosome.writeChromosome("chromosome.csv");
    }

    this.contador++;

    if(this.contador % this.ciclos1_5 == 0){
      double v = this.num_mejoras / this.ciclos1_5;
      mutacionVarianza(v);   
      this.num_mejoras = 0;  
    }
  }

  public void mutacionVarianza(double v){
    double c = 1;

    if (v < 1/5){
       c=0.82;
    }
    if (v > 1/5){
      c=1/0.82;
    }

    for(int i=0; i<this.chromosome.varianceDistance.length; i++) 
      this.chromosome.varianceDistance[i] *= c;
    for(int i=0; i<this.chromosome.varianceAngle.length; i++) 
      this.chromosome.varianceAngle[i] *= c;
    for(int i=0; i<this.chromosome.varianceThrust.length; i++) 
      for(int j=0; j<this.chromosome.varianceThrust[i].length; j++) 
        this.chromosome.varianceThrust[i][j] *= c;
  }

}
