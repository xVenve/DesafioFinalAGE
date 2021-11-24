import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Chromosome {
    public double[] distanceRanges; // 0 - 16000
    public double[] angleRanges; // 0 - 360
    public double[][] thrustInRange; // 0 - 200
    public double[] varianceDistance; 
    public double[] varianceAngle; 
    public double[][] varianceThrust;

    
    //Constructor de cromosoma para asociarle valores inicializados en 1+1 aleatoriamente 
    public Chromosome(double[] distanceRanges, double[] angleRanges, double[][] thrustInRange, int sizeDis, int sizeAng) {
        this.varianceDistance = new double [sizeDis];
        this.varianceAngle = new double [sizeAng];
        this.varianceThrust = new double [sizeDis+1][sizeAng+1];
        this.distanceRanges = distanceRanges.clone();
        this.angleRanges = angleRanges.clone();
        //this.thrustInRange = thrustInRange.clone(); 
        double[][] thrustInRange2 = new double[thrustInRange.length][];
        for (int i = 0; i < thrustInRange.length; i++) {
        	thrustInRange2[i] = thrustInRange[i].clone();
        }
        this.thrustInRange = thrustInRange2;
        this.varianceAngle = varianceAngle;
        this.varianceDistance = varianceDistance; 
        this.varianceThrust = varianceThrust;     
    }

    // Constructor de cromosoma para asociarle valores inicializados en 1+1 aleatoriamente 
    public Chromosome(Chromosome c) {
	    Random rand = new Random();

        this.distanceRanges = new double[c.distanceRanges.length];

        for (int i = 0; i < c.distanceRanges.length; i++){
            this.distanceRanges[i] = c.distanceRanges[i] + rand.nextGaussian() * c.varianceDistance[i];            
        }
        Arrays.sort(this.distanceRanges);
        
        this.angleRanges = new double[c.angleRanges.length];
        for (int i = 0; i < c.angleRanges.length; i++){
            this.angleRanges[i] = (c.angleRanges[i] + rand.nextGaussian() * c.varianceAngle[i])%360;
        }
        Arrays.sort(this.angleRanges);

        this.thrustInRange = new double[c.thrustInRange.length][c.thrustInRange[0].length];
        System.err.println(c.thrustInRange.length+ " " + c.thrustInRange[0].length);
        System.err.println(c.varianceThrust.length+ " " + c.varianceThrust[0].length);
        for (int i = 0; i < c.thrustInRange.length; i++){
            for (int j = 0; j < c.thrustInRange[0].length; j++){
                System.err.println(i +" " +j);
                this.thrustInRange[i][j] = c.thrustInRange[i][j] + rand.nextGaussian() * c.varianceThrust[i][j];
            }
        }
        
        this.varianceAngle = c.varianceAngle;
        this.varianceDistance = c.varianceDistance;
        this.varianceThrust = new double[c.varianceThrust.length][];
        for (int i = 0; i < thrustInRange.length; i++) {
        	this.varianceThrust[i] = c.varianceThrust[i].clone();
        }
        writeChromosome("chromosome.csv");

    }



    /**
     * Lee el fichero csv con la información del cormosoma e inicializa los arrays.
     * Fichero: chromosome.csv
     */
    public Chromosome(String file, double [] varianceDistance, double[] varianceAngle, double[][] varianceThrust) { //Meter varianza
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


