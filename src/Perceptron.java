/**
 * Created by Jonas on 2014-10-16.
 */
public class Perceptron {

    private final double[][] weights;
    private final double learningRate;
    private final double threshold;

    /**
     * Constructor.
     * @param rows number of rows in the input.
     * @param columns number of columns in the input.
     * @param learningRate learning rate of the perceptron, affects how quickly weights are adjusted.
     * @param threshold minimum activation value for the perceptron to fire.
     */
    public Perceptron(int rows, int columns, double learningRate, double threshold) {
        weights = new double[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                weights[i][j] = 0;
            }
        }
        this.learningRate = learningRate;
        this.threshold = threshold;
    }

    /**
     * Activation function for the perceptron.
     * @param in activation value.
     * @return output value of the perceptron.
     */
    private int activation(double in) {
        if(in >= threshold) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Trains the perceptron against a number of inputs and an expected output.
     * Weights are adjusted according to the error between actual and expected output.
     * @param inputs input data.
     * @param expected expected output value.
     */
    public void learn(double[][] inputs, int expected) {
        if(inputs.length != weights.length || inputs[0].length != weights[0].length) {
            System.err.println("In method learn: Weight array does not have same dimensions as input array.");
        }

        //set activation
        double sum = 0;
        for(int inputRow = 0; inputRow < inputs.length; inputRow++) {
            for(int inputColumn = 0; inputColumn < inputs[0].length; inputColumn++) {
                sum += inputs[inputRow][inputColumn]*weights[inputRow][inputColumn];
            }
        }
        int activation = activation(sum);

        //expected is either 0 or 1.

        //calc error
        int error = expected - activation;

        //calc delta weights and apply to weights.
        for(int inputRow = 0; inputRow < inputs.length; inputRow++) {
            for(int inputColumn = 0; inputColumn < inputs[0].length; inputColumn++) {
                double deltaWeight = learningRate*error*inputs[inputRow][inputColumn];
                weights[inputRow][inputColumn] += deltaWeight;
            }
        }

    }

    /**
     * Calculates and returns the output value of the perceptron for an input.
     * @param inputs input data.
     * @return output value.
     */
    public int answer(double[][] inputs) {
        if(inputs.length != weights.length || inputs[0].length != weights[0].length) {
            System.err.println("In method learn: Weight array does not have same dimensions as input array.");
        }

        //set activation
        double sum = 0;
        for(int inputRow = 0; inputRow < inputs.length; inputRow++) {
            for(int inputColumn = 0; inputColumn < inputs[0].length; inputColumn++) {
                sum += inputs[inputRow][inputColumn]*weights[inputRow][inputColumn];
            }
        }
        return activation(sum);
    }
}
