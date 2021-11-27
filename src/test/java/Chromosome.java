import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Chromosome {
	public double[][] distanceRanges; // 0 - 16000
	public double[][] angleRanges; // 0 - 360
	public double[][] thrustInRange; // 0 - 200

	public double[] varianceDistance;
	public double[] varianceAngle;
	public double[][] varianceThrust;

	public double fitness = 0.0;

	/**
	 * Constructor para inicializar cormosoma aleatoriamente
	 * 
	 * @param sizeDis:   tamaño del rango de distancias
	 * @param sizeAngle: tamaño del rango de ángulos
	 */
	public Chromosome(int sizeDis, int sizeAngle) {
		this.distanceRanges = new double[2][sizeDis];
		this.angleRanges = new double[2][sizeAngle];
		
		this.thrustInRange = new double[sizeDis + 1][sizeAngle + 1];

		this.varianceDistance = new double[sizeDis];
		this.varianceAngle = new double[sizeAngle];
		this.varianceThrust = new double[sizeDis + 1][sizeAngle + 1];

		this.initializeRanges();
		this.initializeVariances();
	}

	/**
	 * Crea un individuo a partir del padre y muta.
	 * 
	 * @param c: cromosoma del padre
	 */
	public Chromosome(Chromosome c) {
		Random rand = new Random();

		// Mutación del vector de distancias
		this.distanceRanges = new double[2][c.distanceRanges[0].length];
		for (int i = 0; i < c.distanceRanges.length; i++) {
			this.distanceRanges[0][i] = Math.abs(c.distanceRanges[0][i] + rand.nextGaussian() * c.varianceDistance[i]);
			this.distanceRanges[1][i] = Math.abs(c.distanceRanges[1][i] + rand.nextGaussian() * c.varianceDistance[i]);
		}

		// Mutacion vector de angulos
		this.angleRanges = new double[2][c.angleRanges[0].length];
		for (int i = 0; i < c.angleRanges.length; i++) {
			this.angleRanges[0][i] = (c.angleRanges[0][i] + rand.nextGaussian() * c.varianceAngle[i]) % 360;
			this.angleRanges[0][i] = (this.angleRanges[0][i]+360) % 360;
			this.angleRanges[1][i] = (c.angleRanges[1][i] + rand.nextGaussian() * c.varianceAngle[i]) % 360;
			this.angleRanges[1][i] = (this.angleRanges[1][i]+360) % 360;
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
	}

	/**
	 * Crea un cormosoma a partir de la información de un CSV
	 * 
	 * @param path: ruta del fichero CSV
	 */
	public Chromosome(String path) {
		this.distanceRanges = new double[2][];
		this.angleRanges = new double[2][];		
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String distanceRangesLine = bufferedReader.readLine();
			int numDistanceRanges = distanceRangesLine.split(",").length;
			this.distanceRanges[0] = convertToDouble(distanceRangesLine.split(","));
			distanceRangesLine = bufferedReader.readLine();
			this.distanceRanges[1] = convertToDouble(distanceRangesLine.split(","));

			String angleRangesLine = bufferedReader.readLine();
			int numAngleRanges = angleRangesLine.split(",").length;
			this.angleRanges[0] = convertToDouble(angleRangesLine.split(","));
			angleRangesLine = bufferedReader.readLine();
			this.angleRanges[1] = convertToDouble(angleRangesLine.split(","));
			
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

	/**
	 * Inicializa los rangos de distancia, ángulo y velocidad aleatoriamente.
	 */
	public void initializeRanges() {
		Random rand = new Random();

		// Inicialización del vector de rangos de distancias
		double distanceMax = 4000.0;
		for (int i = 0; i < this.distanceRanges.length; i++) {
			this.distanceRanges[0][i] = (distanceMax) * rand.nextDouble();
			this.distanceRanges[1][i] = (distanceMax) * rand.nextDouble();
		}

		// Inicialización del vector de rangos de angulos
		double angleMax = 360.0;
		for (int i = 0; i < this.angleRanges.length; i++) {
			this.angleRanges[0][i] = (angleMax) * rand.nextDouble();
			this.angleRanges[1][i] = (angleMax) * rand.nextDouble();		
		}

		// Inicialización matriz de aceleración
		double thrustMax = 200.0;
		for (int i = 0; i < thrustInRange.length; i++) {
			for (int j = 0; j < this.thrustInRange[i].length; j++) {
				this.thrustInRange[i][j] = (thrustMax) * rand.nextDouble();
			}
		}
	}

	/**
	 * Inicializa las varianzas aleatoriamente
	 */
	public void initializeVariances() {
		//En orden: distancia, ángulo, aceleración
		double[][] stndDer = { { 500, 3000 }, { 10, 180 }, { 50, 150 } };

		// Inicialización vector de varianzas de distancias
		for (int i = 0; i < this.varianceDistance.length; i++) {
			this.varianceDistance[i] = Math.random() * (stndDer[0][1] - stndDer[0][0]) + stndDer[0][0];
		}

		// Inicialización vector de varianzas de angulos
		for (int i = 0; i < this.varianceAngle.length; i++) {
			this.varianceAngle[i] = Math.random() * (stndDer[1][1] - stndDer[1][0]) + stndDer[1][0];
		}

		// Inicialización matriz de varianzas velocidades
		for (int i = 0; i < this.varianceThrust.length; i++) {
			for (int j = 0; j < this.varianceThrust[0].length; j++) {
				this.varianceThrust[i][j] = Math.random() * (stndDer[2][1] - stndDer[2][0]) + stndDer[2][0];
			}
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

			for (int k = 0; k < this.distanceRanges.length; k++) {
				for (int i = 0; i < this.distanceRanges[k].length; i++) {
					if (i == this.distanceRanges[k].length - 1) {
						myWriter.write(this.distanceRanges[k][i] + "\n");
					} else {
						myWriter.write(this.distanceRanges[k][i] + ",");
					}
				}
			}

			for (int k = 0; k < this.angleRanges.length; k++) {
				for (int i = 0; i < this.angleRanges[k].length; i++) {
					if (i == this.angleRanges[k].length - 1) {
						myWriter.write(this.angleRanges[k][i] + "\n");
					} else {
						myWriter.write(this.angleRanges[k][i] + ",");
					}
				}
			}

			for (double[] doubles : this.thrustInRange) {
				for (int j = 0; j < doubles.length; j++) {
					if (j == doubles.length - 1) {
						myWriter.write(doubles[j] + "\n");
					} else {
						myWriter.write(doubles[j] + ",");
					}
				}
			}
			myWriter.write(this.fitness + "\n");
			myWriter.flush();
			myWriter.close();

		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
