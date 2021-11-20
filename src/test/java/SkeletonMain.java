import com.codingame.gameengine.runner.SoloGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
import java.util.Collections;

public class SkeletonMain {

  public static void main(String[] args) {
    // Uncomment this section and comment the other one to create a Solo Game
    /* Solo Game */
    SoloGameRunner gameRunner = new SoloGameRunner();
    // Sets the player
    gameRunner.setAgent(AgentCSV.class);
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

    // Basic Fitness function, game score
    float fitness = Float.parseFloat(
      gameRunnerResult.metadata.split(":")[1].substring(1, gameRunnerResult.metadata.split(":")[1].length() - 3)
    );
    
    // Number of Checkpoints collected
    int numCheckpointCollected =
      gameRunnerResult.summaries.size() -
      Collections.frequency(gameRunnerResult.summaries, "");
  }
}
