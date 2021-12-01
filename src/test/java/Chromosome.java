import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>{
	public double[] distanceRanges; // 0 - 16000
	public double[] angleRanges; // 0 - 360
	public double[][] thrustInRange; // 0 - 200
	public double[] varianceDistance;
	public double[] varianceAngle;
	public double[][] varianceThrust;
	public double fitness = 100000.0;

	/**
	 * Constructor para inicializar cormosoma aleatoriamente
	 *
	 * @param sizeDis:   tamaño del rango de distancias
	 * @param sizeAngle: tamaño del rango de ángulos
	 */
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

	/**
	 * Crea un individuo a partir del padre y muta.
	 *
	 * @param c: cromosoma del padre
	 */
	public Chromosome(Chromosome c) {
		Random rand = new Random();

		// Mutación del vector de distancias
		this.distanceRanges = new double[c.distanceRanges.length];
		for (int i = 0; i < c.distanceRanges.length; i++) {
			this.distanceRanges[i] = Math.abs(c.distanceRanges[i] + rand.nextGaussian() * c.varianceDistance[i]);
		}

		// Mutacion vector de angulos
		this.angleRanges = new double[c.angleRanges.length];
		for (int i = 0; i < c.angleRanges.length; i++)
			this.angleRanges[i] = (c.angleRanges[i] + rand.nextGaussian() * c.varianceAngle[i]) % 360;

		// Mutacion vector de velocidades
		this.thrustInRange = new double[c.thrustInRange.length][c.thrustInRange[0].length];

		for (int i = 0; i < c.thrustInRange.length; i++)
			for (int j = 0; j < c.thrustInRange[0].length; j++)
				this.thrustInRange[i][j] = Math.min(Math.abs(c.thrustInRange[i][j] + rand.nextGaussian() * c.varianceThrust[i][j]), 200);

		// Copia de varianzas
		this.varianceDistance = new double[c.varianceDistance.length];
		System.arraycopy(c.varianceDistance, 0, this.varianceDistance, 0, c.varianceDistance.length);

		this.varianceAngle = new double[c.varianceAngle.length];
		System.arraycopy(c.varianceAngle, 0, this.varianceAngle, 0, c.varianceAngle.length);

		this.varianceThrust = new double[c.varianceThrust.length][c.varianceThrust[0].length];
		for (int i = 0; i < this.varianceThrust.length; i++)
			System.arraycopy(c.varianceThrust[i], 0, this.varianceThrust[i], 0, this.varianceThrust[i].length);

		writeChromosome("chromosome.csv");
	}

	/**
	 * Crea un cromosoma a partir de dos padres. Luego lo muta.
	 * @param c1: padre 1
	 * @param c2: padre 2
	 */
	public Chromosome(Chromosome c1, Chromosome c2){
		Random rand = new Random();

		this.varianceDistance = new double[c1.varianceDistance.length];
		for (int i = 0; i < this.varianceDistance.length; i++)
			if (Math.random()*2 < 1)	this.varianceDistance[i] = c1.varianceDistance[i];
			else	this.varianceDistance[i] = c2.varianceDistance[i];

		this.varianceAngle = new double[c1.varianceAngle.length];
		for (int i = 0; i < this.varianceAngle.length; i++)
			if (Math.random()*2 < 1)	this.varianceAngle[i] = c1.varianceAngle[i];
			else	this.varianceAngle[i] = c2.varianceAngle[i];

		this.varianceThrust = new double[c1.varianceThrust.length][c1.varianceThrust[0].length];
		for (int i = 0; i < this.varianceThrust.length; i++)
			for (int j = 0; j < this.varianceThrust[i].length; j++)
				if (Math.random() * 2 < 1)	this.varianceThrust[i][j] = c1.varianceThrust[i][j];
				else	this.varianceThrust[i][j] = c2.varianceThrust[i][j];

		// Mutación del vector de distancias
		this.distanceRanges = new double[c1.distanceRanges.length];
		for (int i = 0; i < this.distanceRanges.length; i++) {
			this.distanceRanges[i] = (c1.distanceRanges[i] + c2.distanceRanges[i]) / 2;
			this.distanceRanges[i] = Math.abs(this.distanceRanges[i] + rand.nextGaussian() * this.varianceDistance[i]);
		}

		this.angleRanges = new double[c1.angleRanges.length];
		for (int i = 0; i < this.angleRanges.length; i++) {
			this.angleRanges[i] = (c1.angleRanges[i] + c2.angleRanges[i]) / 2;
			this.angleRanges[i] = Math.abs(this.angleRanges[i] + rand.nextGaussian() * this.varianceAngle[i]);
			this.angleRanges[i] = (this.angleRanges[i]+360)%360;
		}

		this.thrustInRange = new double[c1.thrustInRange.length][c1.thrustInRange[0].length];
		for (int i = 0; i < this.thrustInRange.length; i++)
			for (int j = 0; j < this.thrustInRange[i].length; j++) {
				this.thrustInRange[i][j] = (c1.thrustInRange[i][j] + c2.thrustInRange[i][j]) / 2;
				this.thrustInRange[i][j] = Math.abs(this.thrustInRange[i][j] + rand.nextGaussian() * this.thrustInRange[i][j]);
				this.thrustInRange[i][j] = Math.min(this.thrustInRange[i][j], 200);
			}
	}

	/**
	 * Crea un cormosoma a partir de la información de un CSV
	 *
	 * @param path: ruta del fichero CSV
	 */
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

	/**
	 * Función que ayuda a ordenar la lista de cromosomas según el fitness.
	 * @param c: cromosoma a comparar con este
	 * @return -1, 0, 1: dependiendo si la comparación resulta menor, igual o mayor.
	 */
    @Override
    public int compareTo (Chromosome c){
		return Double.compare(this.fitness, c.fitness);
    }

	/**
	 * Inicializa los rangos de distancia, ángulo y velocidad aleatoriamente.
	 */
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

	/**
	 * Inicializa las varianzas aleatoriamente
	 */
	public void initializeVariances() {
		double[][] stndDer = { { 1000, 3000 }, { 90, 360 }, { 50, 150 } };

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
