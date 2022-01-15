import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class SkeletonMain {

    public static void main(String[] args) {
        // trainOnePlusOne();
        // trainMuPlusLambda();
        // test();
        run();
    }

    /**
     * Train the solutions with 1+1.
     */
    public static void trainOnePlusOne() {
        OnePlusOne evolutionary = new OnePlusOne();
        evolutionary.execute(200);
        System.err.println(evolutionary.chromosome.fitness);
    }

    /**
     * Train solutions with Mu+Lambda.
     */
    public static void trainMuPlusLambda() {
        // MuPlusLambda evolutionary = new MuPlusLambda();
        MuPlusLambda evolutionary = new MuPlusLambda(0);
        evolutionary.execute(20);
        System.err.println(evolutionary.chromosomes.get(0).fitness);
    }

    /**
     * Run the game with a trained individual on a map.
     */
    public static void run() {
        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(AgentEE.class);
        gameRunner.setTestCase("test10.json");
        gameRunner.start();
    }

    /**
     * Check the scores for each map and the average for
     * "files/chromosome.csv"
     */
    public static void test() {
        int numMapas = 15;
        float totalFitness = 0;
        float[] allFitness = new float[numMapas];

        for (int i = 0; i < numMapas; i++) {
            float fitness = 1000;

            try {
                SoloGameRunner gameRunner = new SoloGameRunner();
                gameRunner.setAgent(AgentEE.class);
                gameRunner.setTestCase("test" + i + ".json");
                GameResult gameRunnerResult = gameRunner.simulate();

                fitness = Float.parseFloat(gameRunnerResult.metadata.split(":")[1].substring(1,
                        gameRunnerResult.metadata.split(":")[1].length() - 3));
            } catch (Exception e) {
                System.err.println(e);
            }

            totalFitness += fitness;
            allFitness[i] = fitness;
        }

        for (int i = 0; i < numMapas; i++)
            System.err.println("Fitness " + i + ": " + allFitness[i]);
        System.err.println("Mean fitness: " + totalFitness / numMapas);
    }

}
