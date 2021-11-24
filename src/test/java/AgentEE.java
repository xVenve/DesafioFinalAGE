import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AgentEE {
	// This agent slows down from 200 to 50 when is at 4000 units before reaching
	// the checkpoint
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Chromosome solution = new Chromosome("chromosome.csv");
		Arrays.sort(solution.distanceRanges);
		Arrays.sort(solution.angleRanges);

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
			Point current = new Point(x, y);

			// Calcula distancia al siguiente punto y ángulo relativo de la nave con el
			// siguiente punto
			double distance = current.distance(targ);
			double relAngle = current.relativeAngle(angle, targ);
			int thrust = (int) rules.getThrust(distance, relAngle, solution);
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
		public double getThrust(double distance, double angle, Chromosome solution) {
			for (int i = 0; i <= solution.distanceRanges.length; i++) {
				double dr = 160000;
				if (i < solution.distanceRanges.length) dr = solution.distanceRanges[i];

				if (distance < dr)
					for (int j = 0; j <= solution.angleRanges.length; j++) {
						double ag = 360;
						if (j < solution.angleRanges.length) ag = solution.angleRanges[j];
						if (angle < ag) return solution.thrustInRange[i][j];
					}
			}
			return 0;
		}
	}

}
