import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.codingame.gameengine.runner.dto.GameResult;

public class OnePlusOne {
	
    public float getFitness(GameResult gameRunnerResult) { // Basic Fitness function, game score
    	
    	float fitness = Float.parseFloat(
    		      gameRunnerResult.metadata.split(":")[1].substring(1, gameRunnerResult.metadata.split(":")[1].length() - 3)
    	);
    		    
        // Number of Checkpoints collected
    	int numCheckpointCollected =
    	gameRunnerResult.summaries.size() -
    	Collections.frequency(gameRunnerResult.summaries, "");
    		    
    	System.err.println("Fitness: " + fitness + "\tCheckpoints: " + numCheckpointCollected);
    	
    	return fitness;	
    }
    
    
}