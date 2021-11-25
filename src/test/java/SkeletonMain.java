import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {

  public static void main(String[] args) {
    // Uncomment this section and comment the other one to create a Solo Game
    /* Solo Game */

    /*
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

    */


    // Crear y entrena el evolutivo
	OnePlusOne evolutivo = new OnePlusOne();
	evolutivo.execute(500);
    System.err.println(evolutivo.chromosome.fitness);

    // Para que se vea comentar el entrenamiento
    SoloGameRunner gameRunner = new SoloGameRunner();
    gameRunner.setAgent(AgentEE.class);
    gameRunner.setTestCase("test0.json");
    gameRunner.start();
    //System.err.println(evolutivo.chromosome.fitness);
    // GameResult gameRunnerResult = new GameResult();
    // gameRunnerResult = gameRunner.simulate();

    
    /*
     * Toma el primer cromosoma y crea 5 idénticos.
     * En la carpeta files.
     */

    /*
	Chromosome chromosome = new Chromosome("files/chromosome0.csv");
    for(int i=0; i<6; i++) {
      String file = "files/chromosome" + i + ".csv";
      System.err.println(file);
      chromosome.writeChromosome(file);
    }
    */
  }
}
