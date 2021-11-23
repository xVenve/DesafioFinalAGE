import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Chromosome {
    public double[] distanceRanges; // 0 - 16000
    public double[] angleRanges; // 0 - 360
    public double[][] thrustInRange; // 0 - 200
    public double variance; //varianza asociada 

    public Chromosome() {
    	
    }
    
    //Constructor de cromosoma para asociarle valores inicializados en 1+1 aleatoriamente 
    public Chromosome(double[] distanceRanges, double[] angleRanges, double[][] thrustInRange, double variance) {
        this.distanceRanges = distanceRanges; 
        this.angleRanges = angleRanges; 
        this.thrustInRange = thrustInRange; 
        this.variance = variance;  
    
    }
    /**
     * Lee el fichero csv con la información del cormosoma e inicializa los arrays.
     * Fichero: chromosome.csv
     */
    public Chromosome(String file) {
        try {
            System.err.println(file);
            FileReader fileReader = new FileReader(file);
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

    /**
     * Convierte la entrada String del csv a un array de double.
     * @param values: valores del csv en formato String.
     * @return convertedValues: array de valores en formato double.
     */
    private double[] convertToDouble(String[] values) {
        double[] convertedValues = new double[values.length];
        for (int i = 0; i < values.length; i++)
            convertedValues[i] = Double.parseDouble(values[i]);
        return convertedValues;
    }

    /**
     * Guarda la información de un cromosoma en formato csv.
     * @param file: fichero en el que se copia el individuo.
     */
    public void writeChromosome(String file) {
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(file);
            for (int i = 0; i < this.distanceRanges.length; i++) {
                if (i == this.distanceRanges.length - 1) {
                    myWriter.write(this.distanceRanges[i] + "\n");
                } else {
                    myWriter.write(this.distanceRanges[i] + ",");
                }
            }
            for (int i = 0; i < this.angleRanges.length; i++) {
                if (i == this.angleRanges.length - 1) {
                    myWriter.write(this.angleRanges[i] + "\n");
                } else {
                    myWriter.write(this.angleRanges[i] + ",");
                }
            }
            for (int i = 0; i < this.thrustInRange.length; i++) {
                for (int j = 0; j < this.thrustInRange[i].length; j++) {
                    if (j == this.thrustInRange[i].length - 1) {
                        myWriter.write(this.thrustInRange[i][j] + "\n");
                    } else {
                        myWriter.write(this.thrustInRange[i][j] + ",");
                    }
                }
            }
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}


