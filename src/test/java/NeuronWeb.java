import java.util.List;
import java.util.Random;


//Inputs: (x,y) relativa del checkpoint, (vx, vy) del choche

public class NeuronWeb {
    private final List<Neurone>[] layers;

    /**
     * Crea la red de neuronas
     * @param inputs: variables que se pasan de entrada.
     * @param sizeLayers: numero de neuronas por capa oculta (tantas capas ocultas como el tamaño del array).
     * @param outputs: numero de salidas.
     * @param methodHidden: metodo usado en las capas ocultas.
     * @param methodOutput: metodo usado en la ultima capa.
     */
    public NeuronWeb(int inputs, int[] sizeLayers, int outputs, int methodHidden, int methodOutput){
        Random random = new Random();
        this.layers = new List[sizeLayers.length+1];
        
        for(int i=0; i<sizeLayers.length; i++){
            for (int j=0; j<sizeLayers[i]; j++){
                double[] weights, sigma;

                if(i==0)
                    weights = new double[inputs+1];
                else
                    weights = new double[sizeLayers[i-1]+1];
                sigma = new double[weights.length];

                for(int k=0; k<weights.length; k++) {
                    weights[k] = Math.random() * 2 - 1;
                    sigma[k] = random.nextDouble();
                }

                Neurone n = new Neurone(weights, weights, methodHidden);
                this.layers[i].add(n);
            }
        }

        for (int j=0; j<outputs; j++) {
            double[] weights, sigma;
            weights = new double[sizeLayers[sizeLayers.length-1] + 1];
            sigma = new double[weights.length];

            for (int k = 0; k < weights.length; k++) {
                weights[k] = Math.random() * 2 - 1;
                sigma[k] = random.nextDouble();
            }

            Neurone n = new Neurone(weights, weights, methodOutput);
            this.layers[layers.length - 1].add(n);
        }
    }

    /**
     * Crea una nueva neurona mutada a partir de otra anterior.
     * @param nw: neurona anterior.
     */
    public NeuronWeb(NeuronWeb nw){
        this.layers = new List[nw.layers.length];
        for (int i=0; i<layers.length; i++) {
            for(int j=0; j<nw.layers[i].size(); j++) {
                Neurone n = new Neurone(nw.layers[i].get(j));
                this.layers[i].add(n);
            }
        }
    }


    /**
     * Processa los inputs y devuelve los outputs.
     * @param input: inputs.
     */
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
