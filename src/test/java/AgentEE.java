import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AgentEE {
	/**
	 * Este agente opera según el cromosoma de "files/chromosome.csv", la distancia
	 * al siguiente punto y al siguiente, y el ángulo al siguiente punto y al
	 * siguiente. La dirección será el siguiente punto y la velocidad depende de las
	 * distancias y ángulos.
	 * 
	 * @param args: -
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Chromosome solution = new Chromosome("files/chromosome.csv");
		// Ordena los arrays de menor a mayor.
		Arrays.sort(solution.distanceRanges[0]);
		Arrays.sort(solution.angleRanges[0]);
		Arrays.sort(solution.distanceRanges[1]);
		Arrays.sort(solution.angleRanges[1]);

		Rules rules = new Rules();
		int checkpoints = Integer.parseInt(scanner.nextLine());
		ArrayList<Point> targets = new ArrayList<>();
		for (int i = 0; i < checkpoints; i++) {
			String[] line = scanner.nextLine().split(" ");
			System.err.println(line[0] + " " + line[1]);
			targets.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
		}
		while (true) {
			String s = scanner.nextLine();
			String[] input = s.split(" ");
			// id x y angle
			int target = Integer.parseInt(input[0]);
			int x = Integer.parseInt(input[1]);
			int y = Integer.parseInt(input[2]);
			int angle = Integer.parseInt(input[5]);
			Point targ = targets.get(target);
			Point targ2 = targets.get((target + 1) % targets.size());
			Point current = new Point(x, y);

			// Calcula distancia y ángulo relativo al siguiente punto.
			// Hace la misma operación con el punto de después.
			double distance = current.distance(targ);
			double distance2 = current.distance(targ2);
			double relAngle = current.relativeAngle(angle, targ);
			double relAngle2 = current.relativeAngle(angle, targ2);

			// Calcula la velocidad según los ángulos y distancias.
			int thrust = (int) rules.getThrust(distance, distance2, relAngle, relAngle2, solution);
			System.err.println("Distance: " + (int) distance + "\nAngle: " + (int) relAngle + "\nThrust: " + thrust);

			System.out.println(targ.x + " " + targ.y + " " + thrust + " Agent EE"); // X Y THRUST MESSAGE
		}
	}

	private static class Point {
		public int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		double distance(Point p) {
			return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
		}

		/**
		 * Calcula el ángulo relativo de la nave con el siguiente punto.
		 * 
		 * @param angle: ángulo que tiene la nave respecto al eje.
		 * @param p:     punto con coordenadas X e Y al que se tiene que dirigir la
		 *               nave.
		 * @return relAngle: diferencia entre el ángulo de la nave y el punto.
		 */
		public double relativeAngle(int angle, Point p) {
			// Se calcula el seno y el coseno del punto con la nave.
			double cos = p.x - this.x, sen = p.y - this.y;

			// Se saca el ángulo del arco-tangente de sen/cos, a menos que cos sea 0
			double pAngle;
			if (cos == 0) {
				if (sen > 0)
					pAngle = 90;
				else
					pAngle = 270;
			} else
				pAngle = Math.atan(sen / cos);

			// Se ajusta el ángulo, pues la arco-tangente no tiene en cuenta los ejes.
			if (sen < 0 && cos < 0)
				pAngle = -pAngle - 90;
			else if (sen > 0 && cos < 0)
				pAngle = pAngle + 180;
			else if (sen > 0 && cos > 0)
				pAngle = 90 - pAngle;

			// Retorna la diferencia del ángulo de la nave y del punto.
			return (angle - pAngle + 360) % 360;
		}
	}

	public static class Rules {
		/**
		 * Decide la velocidad a tomar según la distancia y el ángulo.
		 *
		 * @param distance:  distancia al siguiente punto.
		 * @param distance2: distancia al segundo siguiente punto.
		 * @param angle:     ángulo relativo de la nave con el siguiente punto.
		 * @param angle2:    ángulo relativo de la nave con el segundo siguiente punto.
		 * @param solution:  cromosoma usado para la decisión.
		 * @return thrust: la velocidad que llevará la nave.
		 */
		public double getThrust(double distance, double distance2, double angle, double angle2, Chromosome solution) {
			// Para cada distancia al primer punto.
			for (int i = 0; i <= solution.distanceRanges[0].length; i++) {
				double dr = 160000;
				if (i < solution.distanceRanges[0].length) dr = solution.distanceRanges[0][i];
				if (distance < dr)
					// Para cada ángulo del primer punto.
					for (int j = 0; j <= solution.angleRanges[0].length; j++) {
						double ag = 360;
						if (j < solution.angleRanges[0].length) ag = solution.angleRanges[0][j];
						if (angle < ag)
							// Para cada distancia del segundo punto.
							for (int i2 = 0; i2 <= solution.distanceRanges[1].length; i2++) {
								double dr2 = 160000;
								if (i2 < solution.distanceRanges[1].length) dr2 = solution.distanceRanges[1][i2];
								if (distance2 < dr2)
									// Para cada ángulo del segundo punto.
									for (int j2 = 0; j2 <= solution.angleRanges[0].length; j2++) {
										double ag2 = 360;
										if (j2 < solution.angleRanges[0].length) ag2 = solution.angleRanges[0][j2];
										// Devuelve la velocidad asociada a las velocidades y ángulos.
										if (angle2 < ag2) return solution.thrustInRange[i][j][i2][j2];
									}
							}
					}
			}
			return 0;
		}
	}

}
