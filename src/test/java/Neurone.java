import java.util.Random;

public class Neurone {
    //n+1 pesos, siendo n las nauronas anteriores y el umbral el último.
    private final double[] weights;
    private final double[] sigma;
    private final int method;

    /**
     *
     * Inicialización de una neurona sin mutación.
     * @param weights: pesos de la neurona, incluido umbral
     * @param sigma: variacion de cada peso
     * @param method: la función que operará la salida.
     */
    public Neurone (double[] weights, double[] sigma, int method){
        this.weights = weights;
        this.sigma = sigma;
        this.method = method;
    }

    /**
     * Inicialización de una neurona con mutación a partir de otra.
     * Genera pesos distintos aleatorios con un número aleatrorio gaussiano acotado por sigma.
     * Si weightSigma==0, no hay mutación.
     * @param n: neurona de la que desciende.
     */
    public Neurone (Neurone n){
        Random random = new Random();
        double[] weights = n.getWeights();

        for (int i=0; i<weights.length; i++)
            weights[i] += random.nextGaussian() * n.sigma[i];

        this.weights = weights;
        this.method = n.method;
        this.sigma = n.sigma;
    }

    /**
     * @return this.weights
     */
    public double[] getWeights(){
        return this.weights;
    }

    /**
     * @return this.method
     */
    public int getMethod(){ return this.method; }

    /**
     * Toma las salidas de las neuronas de la capa anterior, realiza la función y devuelve el resultado.
     * @param input: salidas de neuronas de la capa anterior.
     * @return f(exit)
     */
    public double process(double[] input){
        double exit = 0;
        for(int i=0; i<input.length; i++){
            exit += input[i] * this.weights[i];
        }
        exit += this.weights[weights.length-1];

        switch (this.method){
            case 1: return sigmoid_function(exit); //Sigmoid
            case 2: return relu_function(exit); //ReLu
            case 3: return hyperbolic_function(exit); //Hyperbolic
            default: return exit; //Linear
        }
    }

    /**
     * Función sigmoidal.
     * @param x: exit
     * @return sigmoid(x)
     */
    private double sigmoid_function (double x){
        double e = Math.E;
        return 1 / (1 + Math.pow(e, x));
    }

    /**
     * Función ReLu
     * @param x: exit
     * @return ReLu(x)
     */
    private double relu_function (double x){
        if (x<0) return 0;
        return x;
    }

    /**
     * Función hiperbóloca
     * @param x: exit
     * @return hyperbolic(x)
     */
    private double hyperbolic_function (double x){
        double e = Math.E;
        double ep = Math.pow(e, x), em = Math.pow(e, -x);
        return (ep - em) / (ep + em);
    }



}