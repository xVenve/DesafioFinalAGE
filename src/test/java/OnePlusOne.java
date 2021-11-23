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
import com.codingame.gameengine.runner.dto.GameResult;

public class OnePlusOne {

  Chromosome chromosome;
  int contador;
  int num_mejoras;
  float fitness;
  
  float eval_mutado;
  Chromosome prueba;

  /**
   * Lee el fichero csv con la información del cormosoma e inicializa los arrays.
   * Fichero: chromosome.csv
   */
  private Chromosome getChromosome(Chromosome chromosome) {
    try {
      FileReader fileReader = new FileReader("chromosome.csv");
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String distanceRangesLine = bufferedReader.readLine();
      int numDistanceRanges = distanceRangesLine.split(",").length;
      chromosome.distanceRanges =
        convertToDouble(distanceRangesLine.split(","));

      String angleRangesLine = bufferedReader.readLine();
      int numAngleRanges = angleRangesLine.split(",").length;
      chromosome.angleRanges = convertToDouble(angleRangesLine.split(","));

      chromosome.thrustInRange =
        new double[numDistanceRanges + 1][numAngleRanges + 1];
      for (int i = 0; i < numDistanceRanges + 1; i++) {
        chromosome.thrustInRange[i] =
          convertToDouble(bufferedReader.readLine().split(","));
      }
      bufferedReader.close();
    } catch (Exception e) {
      System.err.println(e);
    }
    return chromosome;
  }

  /**
   * Convierte la entrada String del csv a un array de double.
   * @param values: valores del csv en formato String.
   * @return convertedValues: array de valores en formato double.
   */
  private double[] convertToDouble(String[] values) {
    double[] convertedValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      convertedValues[i] = Double.parseDouble(values[i]);
    }
    return convertedValues;
  }

  /**
   * Guarda la información de un cromosoma en formato csv.
   * Fichero: chromosome.csv.
   * @param chromosome: cromosoma que se escribe en fichero.
   */
  private void writeChromosome(Chromosome chromosome) {
    FileWriter myWriter;
    try {
      myWriter = new FileWriter("chromosome.csv");
      for (int i = 0; i < chromosome.distanceRanges.length; i++) {
        if (i == chromosome.distanceRanges.length - 1) {
          myWriter.write(chromosome.distanceRanges[i] + "\n");
        } else {
          myWriter.write(chromosome.distanceRanges[i] + ",");
        }
      }
      for (int i = 0; i < chromosome.angleRanges.length; i++) {
        if (i == chromosome.angleRanges.length - 1) {
          myWriter.write(chromosome.angleRanges[i] + "\n");
        } else {
          myWriter.write(chromosome.angleRanges[i] + ",");
        }
      }
      for (int i = 0; i < chromosome.thrustInRange.length; i++) {
        for (int j = 0; j < chromosome.thrustInRange[i].length; j++) {
          if (j == chromosome.thrustInRange[i].length - 1) {
            myWriter.write(chromosome.thrustInRange[i][j] + "\n");
          } else {
            myWriter.write(chromosome.thrustInRange[i][j] + ",");
          }
        }
      }
      myWriter.flush();
      myWriter.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

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
    //Inicializacion de la varianza, solo se utilizará un valor de varianza 
    int mean = 5;
    int standard_desviation = 20;
    double variance = mean + rand.nextGaussian()* standard_desviation;
    this.contador = 0;
    this.num_mejoras = 0;

    //Inicialización del vector de rangos de distancias 
    double [] distanceRanges = new double [3];
    double distanceMin = 0.0;
    double distanceMax = 4000.0;

    for (int i = 0; i < distanceRanges.length; i++){
      distanceRanges[i] = distanceMin + (distanceMax-distanceMin) * rand.nextDouble();
    }

   //Inicialización del vector de rangos de angulos 
    double [] angleRanges = new double [6];
    double angleMin = 0.0;
    double angleMax = 360.0;   

    for (int i = 0; i < angleRanges.length; i++){
      angleRanges[i] = angleMin + (angleMax-angleMin) * rand.nextDouble();
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
    Chromosome c = new Chromosome(distanceRanges, angleRanges, thrustInRange, variance);
    
	c = new Chromosome("files/chromosome0.csv");  //TRAMPITAS
	c.variance = variance;//Trampitas 2.0

    writeChromosome(c);

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
	Chromosome mutado = new Chromosome (this.chromosome.distanceRanges, this.chromosome.angleRanges, this.chromosome.thrustInRange, this.chromosome.variance);

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
    writeChromosome(mutado);

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
      writeChromosome(this.chromosome);
    }

    this.contador++;

    if(this.contador % 10 == 0){
      mutacionVarianza();
    }
  }


  public void mutacionVarianza(){
    double c = 0.82;

    if (this.num_mejoras < 2){
      this.chromosome.variance *= c;
    }
    if (this.num_mejoras > 2){
      this.chromosome.variance /= c;
    }
     //this.num_mejoras = 0;
  }

}
