//Al salir de 1+1, ordenar de menor a mayor los rangos de la distancia y los angulos

import com.codingame.gameengine.runner.dto.GameResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class OnePlusOne {

  // Gets the chromosome from the CSV to use it locally
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

  // Convert String[] to double[]
  private double[] convertToDouble(String[] values) {
    double[] convertedValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      convertedValues[i] = Double.parseDouble(values[i]);
    }
    return convertedValues;
  }

  // Write the chromosome to the CSV
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

  public float getFitness(GameResult gameRunnerResult) {
    // Basic Fitness function, game score
    float fitness = Float.parseFloat(
      gameRunnerResult.metadata
        .split(":")[1].substring(
          1,
          gameRunnerResult.metadata.split(":")[1].length() - 3
        )
    );

    // Number of Checkpoints collected
    int numCheckpointCollected =
      gameRunnerResult.summaries.size() -
      Collections.frequency(gameRunnerResult.summaries, "");

    System.err.println(
      "Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected
    );

    return fitness;
  }
}
