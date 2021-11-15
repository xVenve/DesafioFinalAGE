import java.util.List;


//Inputs: (x,y) relativa del checkpoint, (vx, vy) del choche

public class NeuronWeb {
    private final List<Neurone>[] layers;

    public NeuronWeb(int inputs, int[] sizeLayers, int methodHidden, int methodOutput){
        this.layers = new List[sizeLayers.length];
        
        for(int i=0; i<sizeLayers.length; i++){
            for (int j=0; j<sizeLayers[i]; j++){
                double[] weights;
                if(i==0)
                    weights = new double[inputs+1];
                else
                    weights = new double[sizeLayers[i-1]+1];

                for(int k=0; k<weights.length; k++)
                    weights[k] = Math.random()*2-1;

                Neurone n;
                if(i<sizeLayers.length-1)
                    n = new Neurone(weights, methodHidden);
                else
                    n = new Neurone(weights, methodOutput);
                this.layers[i].add(n);
            }            
        }
    }


    public NeuronWeb(NeuronWeb nw, GeneticAgent g){
        this.layers = new List[nw.layers.length];
        for (int i=0; i<layers.length; i++) {
            for(int j=0; j<nw.layers[i].size(); j++) {
                Neurone n = new Neurone(nw.layers[i].get(j), g.weightSigma);
                this.layers[i].add(n);
            }
        }
    }

    public double[] process(double[] input){
        for (List<Neurone> layer : layers) {
            double[] output = new double[layer.size()];
            for (int j = 0; j < layer.size(); j++)
                output[j] = layer.get(j).process(input);
            input = output;
        }
        return input;
    }

}
