import java.util.ArrayList;
import java.util.Scanner;

public class AgentEE {
    // This agent slows down from 200 to 50 when is at 4000 units before reaching the checkpoint
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Rules rules = new Rules();
        int checkpoints = Integer.parseInt(scanner.nextLine());
        ArrayList<Point> targets = new ArrayList<>();
        for(int i = 0; i < checkpoints; i++){
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
            int vx = Integer.parseInt(input[3]);
            int vy = Integer.parseInt(input[4]);
            int angle = Integer.parseInt(input[5]);
            Point targ = targets.get(target);
            Point current = new Point(x, y);

            double relAngle = current.relativeAngle(angle, targ);
            double distance = current.distance(targ);
            int thrust = rules.getThrust(distance, relAngle);
            System.err.println("Distance: " + (int) distance + "\nAngle: " + (int) relAngle + "\nThrust: " + thrust);
            System.out.println(targ.x + " " + targ.y + " " + thrust + " Agent EE"); // X Y THRUST MESSAGE
        }
    }

    private static class Point{
        public int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        double distance(Point p) {
            return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
        }

        double[] distanceVector(Point p){
            return new double[]{(this.x - p.x), (this.y - p.y)};
        }

        public double relativeAngle(int angle, Point p){
            // Puede dar problemas con la disposicion x e y
            double cos = p.x-this.x, sen = p.y-this.y;

            double pAngle;
            if(cos==0){
                if(sen>0)   pAngle=90;
                else    pAngle=270;
            } else
                pAngle = Math.atan(sen/cos);

            if(sen < 0 && cos < 0)   pAngle = -pAngle-90;
            else if (sen > 0 && cos < 0)    pAngle = pAngle+180;
            else if (sen > 0 && cos > 0)    pAngle = 90-pAngle;

            return (angle-pAngle+360)%360;
        }
    }



}

