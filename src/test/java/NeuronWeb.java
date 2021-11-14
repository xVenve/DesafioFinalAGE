import java.util.List;

public class NeuronWeb {
    private List<Neurone>[] layers;

    public NeuronWeb(int[] sizeLayers, int methodHidden, int methodOutput){
        this.layers = new List[sizeLayers.length-1];

        for(int i=1; i<sizeLayers.length; i++){
            double[] weights = new double[sizeLayers[i-1]+1];
            for(int j=0; j<weights.length; j++)
                weights[j] = Math.random()*2-1;

            Neurone n;
            if(i<sizeLayers.length-1)
                n = new Neurone(weights, methodHidden);
            else
                n = new Neurone(weights, methodOutput);
            this.layers[i-1].add(n);
        }
    }
}
