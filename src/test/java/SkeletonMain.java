import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class SkeletonMain {

  public static void main(String[] args) {
    // Uncomment this section and comment the other one to create a Solo Game
    /* Solo Game */
    SoloGameRunner gameRunner = new SoloGameRunner();
    // Sets the player
    gameRunner.setAgent(AgentEE.class);
    // gameRunner.setAgent(Agent2.class);

    // Sets a test case
    gameRunner.setTestCase("test0.json");

    // Another way to add a player for python
    // gameRunner.addAgent("python3 /home/user/player.py");

    // Start the game server
    // gameRunner.start();
    // Simulate
    GameResult gameRunnerResult = new GameResult();
    gameRunnerResult = gameRunner.simulate();

    OnePlusOne evolutivo = new OnePlusOne();
    evolutivo.getFitness(gameRunnerResult);
    /*
    * Toma el primer cromosoma y crea 5 idénticos.
    * En la carpeta files.
     */
    Chromosome chromosome = new Chromosome("files/chromosome0.csv");
    for(int i=0; i<6; i++) {
      String file = "files/chromosome" + i + ".csv";
      System.err.println(file);
      chromosome.writeChromosome(file);
    }
  }
}
