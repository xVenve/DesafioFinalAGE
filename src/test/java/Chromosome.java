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
	public double fitness = 0.0;

	public Chromosome(int sizeDis, int sizeAngle) {
		this.distanceRanges = new double[sizeDis];
		this.angleRanges = new double[sizeAngle];
		this.thrustInRange = new double[sizeDis + 1][sizeAngle + 1];

		this.varianceDistance = new double[sizeDis];
		this.varianceAngle = new double[sizeAngle];
		this.varianceThrust = new double[sizeDis + 1][sizeAngle + 1];

		this.initializeRanges();
		this.initializeVariances();
	}

	// Constructor de cromosoma para asociarle valores inicializados en 1+1
	// aleatoriamente
	public Chromosome(Chromosome c) {
		Random rand = new Random();

		// Mutación del vector de distancias
		this.distanceRanges = new double[c.distanceRanges.length];
		for (int i = 0; i < c.distanceRanges.length; i++) {
			this.distanceRanges[i] = Math.abs(c.distanceRanges[i] + rand.nextGaussian() * c.varianceDistance[i]);
		}

		// Mutacion vector de angulos
		this.angleRanges = new double[c.angleRanges.length];
		for (int i = 0; i < c.angleRanges.length; i++) {
			this.angleRanges[i] = (c.angleRanges[i] + rand.nextGaussian() * c.varianceAngle[i]) % 360;
		}

		// Mutacion vector de velocidades
		this.thrustInRange = new double[c.thrustInRange.length][c.thrustInRange[0].length];

		for (int i = 0; i < c.thrustInRange.length; i++) {
			for (int j = 0; j < c.thrustInRange[0].length; j++) {
				this.thrustInRange[i][j] = Math.min(Math.abs(c.thrustInRange[i][j] + rand.nextGaussian() * c.varianceThrust[i][j]), 200);

			}
		}

		// Copia de varianzas
		this.varianceDistance = new double[c.varianceDistance.length];
		System.arraycopy(c.varianceDistance, 0, this.varianceDistance, 0, c.varianceDistance.length);

		this.varianceAngle = new double[c.varianceAngle.length];
		System.arraycopy(c.varianceAngle, 0, this.varianceAngle, 0, c.varianceAngle.length);

		this.varianceThrust = new double[c.varianceThrust.length][c.varianceThrust[0].length];
		for (int i = 0; i < this.varianceThrust.length; i++) {
			System.arraycopy(c.varianceThrust[i], 0, this.varianceThrust[i], 0, this.varianceThrust[i].length);
		}

		writeChromosome("chromosome.csv");
	}

	// Read Ranges from file chromosome.csv
	public Chromosome(String path) {
		try {
			FileReader fileReader = new FileReader(path);
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
			this.varianceDistance = new double[numDistanceRanges];
			this.varianceAngle = new double[numAngleRanges];
			this.varianceThrust = new double[numDistanceRanges + 1][numAngleRanges + 1];
			bufferedReader.close();

		} catch (Exception e) {
			System.err.println(e);
		}
		this.initializeVariances();
	}

	public void initializeRanges() {
		Random rand = new Random();

		// Inicialización del vector de rangos de distancias
		double distanceMax = 4000.0;
		for (int i = 0; i < this.distanceRanges.length; i++) {
			this.distanceRanges[i] = (distanceMax) * rand.nextDouble();
		}

		// Inicialización del vector de rangos de angulos
		double angleMax = 360.0;
		for (int i = 0; i < this.angleRanges.length; i++) {
			this.angleRanges[i] = (angleMax) * rand.nextDouble();
		}

		// Inicialización matriz de aceleración
		double thrustMax = 200.0;
		for (int i = 0; i < thrustInRange.length; i++) {
			for (int j = 0; j < this.thrustInRange[i].length; j++) {
				this.thrustInRange[i][j] = (thrustMax) * rand.nextDouble();
			}
		}
	}

	public void initializeVariances() {
		Random rand = new Random();
		int standardDesviationDis = 1350;
		int standardDesviationAng = 100;
		int standardDesviationThrust = 75;

		// Inicialización vector de varianzas de distancias
		for (int i = 0; i < this.varianceDistance.length; i++) {
			this.varianceDistance[i] = Math.abs(rand.nextGaussian() * standardDesviationDis);
		}

		// Inicialización vector de varianzas de angulos
		for (int i = 0; i < this.varianceAngle.length; i++) {
			this.varianceAngle[i] = Math.abs(rand.nextGaussian() * standardDesviationAng);
		}

		// Inicialización matriz de varianzas velocidades
		for (int i = 0; i < this.varianceThrust.length; i++) {
			for (int j = 0; j < this.varianceThrust[0].length; j++) {
				this.varianceThrust[i][j] = Math.abs(rand.nextGaussian() * standardDesviationThrust);
			}
		}
	}

	public void copyChromosome(Chromosome c) {
		System.arraycopy(c.distanceRanges, 0, this.distanceRanges, 0, c.distanceRanges.length);
		System.arraycopy(c.angleRanges, 0, this.varianceDistance, 0, c.varianceDistance.length);
		for (int i = 0; i < this.varianceThrust.length; i++) {
			System.arraycopy(c.thrustInRange[i], 0, this.thrustInRange[i], 0, this.thrustInRange[i].length);
		}
	}

	/**
	 * Convierte la entrada String del csv a un array de double.
	 * 
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
	 * 
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
