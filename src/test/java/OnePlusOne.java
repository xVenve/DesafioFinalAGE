//Inicializacion vectores
/*
for (i = 0;i<100;i++){

setAgent(Agent_EE.java, x,y,z);


}






import java.util.ArrayList;
import java.util.Scanner;

public class OnePlusOne {
    
    private int thrust;
    
    
    public OnePlusOne (int thrust){
        this.thrust = thrust;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int checkpoints = Integer.parseInt(scanner.nextLine());
        ArrayList<Point> targets = new ArrayList<>();
        for(int i = 0; i < checkpoints; i++){
            String[] line = scanner.nextLine().split(" ");
            System.err.println(line[0] + " " + line[1]);
            targets.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
        }
        double dist = 100000.0;
        int z = 0;
        while (true) {
            String s = scanner.nextLine();
            System.err.println(s);
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
            int thrust = 100;
            if(targ.distance(current) < 4000){
                thrust = 50;
            }
            System.out.println(targ.x + " " + targ.y + " " + this.thrust + " Agent 1"); // X Y THRUST MESSAGE
        }
    }
}*/