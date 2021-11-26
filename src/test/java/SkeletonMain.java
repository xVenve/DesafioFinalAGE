import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {

  public static void main(String[] args) {
    // Crear y entrena el evolutivo
    /* 
    OnePlusOne evolutivo = new OnePlusOne();
    evolutivo.execute(500);
    System.err.println(evolutivo.chromosome.fitness);
    */

    // Para que se vea comentar el entrenamiento
    SoloGameRunner gameRunner = new SoloGameRunner();
    gameRunner.setAgent(AgentEE.class);
    gameRunner.setTestCase("test0.json");
    gameRunner.start();

    // Toma el primer cromosoma y crea 5 idénticos. En la carpeta files.
    /*
    Chromosome chromosome = new Chromosome("files/chromosome0.csv");
    for (int i = 0; i < 6; i++) {
      String file = "files/chromosome" + i + ".csv";
      System.err.println(file);
      chromosome.writeChromosome(file);
    }
    */
  }
}
