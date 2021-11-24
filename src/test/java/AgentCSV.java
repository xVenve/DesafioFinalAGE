import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AgentCSV {

	// This agent slows down from 200 to 50 when is at 4000 units before reaching
	// the checkpoint
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		int checkpoints = Integer.parseInt(scanner.nextLine());
		ArrayList<Point> targets = new ArrayList<>();
		for (int i = 0; i < checkpoints; i++) {
			String[] line = scanner.nextLine().split(" ");
			System.err.println(line[0] + " " + line[1]);
			targets.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
		}

		// Write CSV
		int[] exampleChromosome = { 25, 50, 75, 100 };
		FileWriter myWriter = new FileWriter("csvTest.csv");
		for (int i = 0; i < exampleChromosome.length; i++) myWriter.write(exampleChromosome[i] + "\n");
		myWriter.close();

		// Read CSV
		List<String[]> chromosome = new CSVReader(new FileReader("csvTest.csv"), ',', '"', 0).readAll();
		int firstGenInt = Integer.parseInt(chromosome.get(1)[0]);
		float firstGenFloat = Float.parseFloat(chromosome.get(1)[0]);

		scanner.close();
		while (true) {
			String s = scanner.nextLine();
			System.err.println(s);
			String[] input = s.split(" ");
			// id x y vx vy angle
			Point targ = targets.get(Integer.parseInt(input[0]));
			int x = Integer.parseInt(input[1]);
			int y = Integer.parseInt(input[2]);

			Point current = new Point(x, y);
			int thrust = Integer.parseInt(chromosome.get(1)[0]);
			if (targ.distance(current) < 2000) {
				thrust = Integer.parseInt(chromosome.get(2)[0]);
			} else if (targ.distance(current) < 4000) {
				thrust = Integer.parseInt(chromosome.get(3)[0]);
			}
			System.out.println(targ.x + " " + targ.y + " " + thrust + " Agent CSV"); // X Y THRUST MESSAGE
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
	}
}
