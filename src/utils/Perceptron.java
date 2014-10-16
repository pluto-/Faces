package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jonas on 2014-10-16.
 */
public class Perceptron {

    private double[][] weights;
    private double learningRate;
    private double threshold;

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

    private byte activation(double in) {
        if(in >= threshold) {
            return 1;
        } else {
            return 0;
        }
    }

    public void learn(byte[][] inputs, byte expected) {

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
        byte activation = activation(sum);

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

    public int answer(byte[][] inputs) {
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

    public static void main(String args[]) {
        ArrayList<Face> trainingFaces = null;
        ArrayList<Face> testFaces = null;
        Map<Integer, Integer> trainingAnswers = null;
        Map<Integer, Integer> testAnswers = null;
        Perceptron eyebrowPerceptron = new Perceptron(20, 20, 0.1, 0.5);

        try {
            trainingFaces = FacesLoader.loadFaces("training.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            trainingAnswers = FacesLoader.loadAnswers("training-facit.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            testFaces = FacesLoader.loadFaces("test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            testAnswers = FacesLoader.loadAnswers("test-facit.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Face face : trainingFaces) {
            byte correctAnswer;
            if(trainingAnswers.get(face.getImageNr()) == 1 || trainingAnswers.get(face.getImageNr()) == 2) {
                correctAnswer = 0; // Mountain eyebrows.
            } else if(trainingAnswers.get(face.getImageNr()) == 3 || trainingAnswers.get(face.getImageNr()) == 4) {
                correctAnswer = 1; // Valley eyebrows.
            } else {
                correctAnswer = -1;
                System.err.println("bad correct answer.");
                System.exit(1);
            }

            eyebrowPerceptron.learn(face.getImage(), correctAnswer);
        }

        int correctAnswers = 0;
        int wrongAnswers = 0;

        for(Face face : testFaces) {
            int answer = eyebrowPerceptron.answer(face.getImage());
            System.out.println(answer);
            int correctAnswer = -1;
            if(testAnswers.get(face.getImageNr()) == 1 || testAnswers.get(face.getImageNr()) == 2) {
                correctAnswer = 0;
            } else if (testAnswers.get(face.getImageNr()) == 3 || testAnswers.get(face.getImageNr()) == 4) {
                correctAnswer = 1;
            }
            if(answer == correctAnswer) {
                correctAnswers++;
            } else {
                System.out.println("Wrong on Image"+face.getImageNr()+" Answer="+answer+" Correct="+correctAnswer);
                wrongAnswers++;
            }
        }

        System.out.println("Percent correct answers: " + 100*((double)correctAnswers/((double)correctAnswers+(double)wrongAnswers)));
    }

}
