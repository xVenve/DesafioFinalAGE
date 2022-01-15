import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AgentEE {
    /**
     * This agent operates according to the chromosome of "files/chromosome.csv", the distance to the next point and to
     * the next point, and the angle to the next point and to the next point. The direction will be the next point and
     * the speed depends on the distances and angles distances and angles.
     *
     * @param args: -
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Chromosome solution = new Chromosome("files/chromosome.csv");
        // Sort arrays from smallest to largest.
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

            // Calculates distance and angle relative to the next point.
            // Do the same operation with the next point.
            double distance = current.distance(targ);
            double distance2 = current.distance(targ2);
            double relAngle = current.relativeAngle(angle, targ);
            double relAngle2 = current.relativeAngle(angle, targ2);

            // Calculate speed according to angles and distances.
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
         * Calculate the relative angle of the ship to the following point.
         *
         * @param angle: angle that the ship has with respect to the axis.
         * @param p:     point with X and Y coordinates to which the ship has to be directed.
         * @return relAngle: difference between the angle of the spacecraft and the * point.
         */
        public double relativeAngle(int angle, Point p) {
            // The sine and cosine of the point with the ship is calculated.
            double cos = p.x - this.x, sin = p.y - this.y;

            // The angle of the arc-tangent of sin/cos is taken, unless cos is 0.
            double pAngle;
            if (cos == 0) {
                if (sin > 0)
                    pAngle = 90;
                else
                    pAngle = 270;
            } else
                pAngle = Math.atan(sin / cos);

            // The angle is adjusted, because the arc-tangent does not take into account the axes.
            if (sin < 0 && cos < 0)
                pAngle = -pAngle - 90;
            else if (sin > 0 && cos < 0)
                pAngle = pAngle + 180;
            else if (sin > 0 && cos > 0)
                pAngle = 90 - pAngle;

            // Return the difference of the angle of the ship and the point.
            return (angle - pAngle + 360) % 360;
        }
    }

    public static class Rules {
        /**
         * Decides the speed to take according to distance and angle.
         *
         * @param distance:  distance to next point.
         * @param distance2: distance to second next point.
         * @param angle:     relative angle of the spacecraft to the next point.
         * @param angle2:    relative angle of the spacecraft to the second next point.
         * @param solution:  chromosome used for the decision.
         * @return thrust: the speed the spacecraft will take.
         */
        public double getThrust(double distance, double distance2, double angle, double angle2, Chromosome solution) {
            // For each distance to the first point.
            for (int i = 0; i <= solution.distanceRanges[0].length; i++) {
                double dr = 160000;
                if (i < solution.distanceRanges[0].length) dr = solution.distanceRanges[0][i];
                if (distance < dr)
                    // For each angle of the first point.
                    for (int j = 0; j <= solution.angleRanges[0].length; j++) {
                        double ag = 360;
                        if (j < solution.angleRanges[0].length) ag = solution.angleRanges[0][j];
                        if (angle < ag)
                            // For each distance from the second point.
                            for (int i2 = 0; i2 <= solution.distanceRanges[1].length; i2++) {
                                double dr2 = 160000;
                                if (i2 < solution.distanceRanges[1].length) dr2 = solution.distanceRanges[1][i2];
                                if (distance2 < dr2)
                                    // For each angle of the second point.
                                    for (int j2 = 0; j2 <= solution.angleRanges[0].length; j2++) {
                                        double ag2 = 360;
                                        if (j2 < solution.angleRanges[0].length) ag2 = solution.angleRanges[0][j2];
                                        // Returns the velocity associated with the velocities and angles.
                                        if (angle2 < ag2) return solution.thrustInRange[i][j][i2][j2];
                                    }
                            }
                    }
            }
            return 0;
        }
    }

}
