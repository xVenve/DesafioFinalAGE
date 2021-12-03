import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AgentEE {
	// This agent slows down from 200 to 50 when is at 4000 units before reaching
	// the checkpoint
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Chromosome solution = new Chromosome("files/chromosome.csv");
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
			// id x y vx vy angle
			int target = Integer.parseInt(input[0]);
			int x = Integer.parseInt(input[1]);
			int y = Integer.parseInt(input[2]);
			// int vx = Integer.parseInt(input[3]);
			// int vy = Integer.parseInt(input[4]);
			int angle = Integer.parseInt(input[5]);
			Point targ = targets.get(target);
			Point targ2 = targets.get((target + 1) % targets.size());
			Point current = new Point(x, y);

			// Calcula distancia al siguiente punto y ángulo relativo de la nave con el
			// siguiente punto
			double distance = current.distance(targ);
			double distance2 = current.distance(targ2);
			double relAngle = current.relativeAngle(angle, targ);
			double relAngle2 = current.relativeAngle(angle, targ2);
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

			// Se saca el ángulo del arcotantente de sen/cos, a menos que cos sea 0
			double pAngle;
			if (cos == 0) {
				if (sen > 0)
					pAngle = 90;
				else
					pAngle = 270;
			} else
				pAngle = Math.atan(sen / cos);

			// Se ajusta el ángulo, pues la arcotangente lo calcula mal.
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
		 * @param distance: distancia al siguiente punto.
		 * @param angle:    ángulo relativo de la nave con el siguiente punto.
		 * @param solution: cromosoma usado para la decisión.
		 * @return thrust: la velocidad que llevará la nave.
		 */
		public double getThrust(double distance, double distance2, double angle, double angle2, Chromosome solution) {
			for (int i = 0; i <= solution.distanceRanges[0].length; i++) {
				double dr = 160000;
				if (i < solution.distanceRanges[0].length)
					dr = solution.distanceRanges[0][i];
				if (distance < dr)
					for (int j = 0; j <= solution.angleRanges[0].length; j++) {
						double ag = 360;
						if (j < solution.angleRanges[0].length) ag = solution.angleRanges[0][j];
						if (angle < ag)
							for (int i2 = 0; i2 <= solution.distanceRanges[1].length; i2++) {
								double dr2 = 160000;
								if (i2 < solution.distanceRanges[1].length) dr2 = solution.distanceRanges[1][i2];
								if (distance2 < dr2) {
									for (int j2 = 0; j2 <= solution.angleRanges[0].length; j2++) {
										double ag2 = 360;
										if (j2 < solution.angleRanges[0].length) ag2 = solution.angleRanges[0][j2];
										if (angle2 < ag2) return solution.thrustInRange[i][j][i2][j2];
									}
								}
							}
					}
			}
			return 0;
		}
	}

}
