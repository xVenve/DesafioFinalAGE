import java.io.BufferedReader;
import java.io.FileReader;

public class Rules {

  private double[] distanceRanges; // 0 - 16000
  private double[] angleRanges; // 0 - 360
  private double[][] thrustInRange; // 0 - 200

  public Rules() {
    this.getRanges();
  }

  // Read Ranges from file chromosome.csv
  private void getRanges() {
    try {
      FileReader fileReader = new FileReader("chromosome.csv");
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String distanceRangesLine = bufferedReader.readLine();
      int numDistanceRanges = distanceRangesLine.split(",").length;
      this.distanceRanges = convertToDouble(distanceRangesLine.split(","));

      String angleRangesLine = bufferedReader.readLine();
      int numAngleRanges = angleRangesLine.split(",").length;
      this.angleRanges = convertToDouble(angleRangesLine.split(","));

      this.thrustInRange = new double[numDistanceRanges + 1][numAngleRanges + 1];
      for (int i = 0; i < numDistanceRanges + 1; i++) {
        this.thrustInRange[i] = convertToDouble(bufferedReader.readLine().split(","));
      }

      bufferedReader.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  // Convert String[] to double[]
  private double[] convertToDouble(String[] values) {
    double[] convertedValues = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      convertedValues[i] = Double.parseDouble(values[i]);
    }
    return convertedValues;
  }

  /**
   * Decide la velocidad a tomar según la distancia y el ángulo.
   * @param distance: distancia al siguiente punto.
   * @param angle: ángulo relativo de la nave con el siguiente punto.
   * @return thrust: la velocidad que llevará la nave.
   */
  public double getThrust(double distance, double angle) {
    for (int i = 0; i < this.distanceRanges.length + 1; i++) {
      double dr = 160000;
      if (i < this.distanceRanges.length) dr = this.distanceRanges[i];

      if (distance < dr) for (int j = 0; j < this.angleRanges.length + 1; j++) {
        double ag = 360;
        if (j < this.angleRanges.length) ag = this.angleRanges[j];
        if (angle < ag) return this.thrustInRange[i][j];
      }
    }

    return 0;
  }
}
