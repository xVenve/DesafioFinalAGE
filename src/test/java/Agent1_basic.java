import java.util.*;
import java.io.*;
import java.math.*;


class Agent1_basic {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int checkpoints = in.nextInt(); // Count of checkpoints to read
        for (int i = 0; i < checkpoints; i++) {
            int checkpointX = in.nextInt(); // Position X
            int checkpointY = in.nextInt(); // Position Y
        }

        // game loop
        while (true) {
            int checkpointIndex = in.nextInt(); // Index of the checkpoint to lookup in the checkpoints input, initially 0
            int x = in.nextInt(); // Position X
            int y = in.nextInt(); // Position Y
            int vx = in.nextInt(); // horizontal speed. Positive is right
            int vy = in.nextInt(); // vertical speed. Positive is downwards
            int angle = in.nextInt(); // facing angle of this car

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("5000 5000 200 message"); // X Y THRUST MESSAGE
        }
    }
}