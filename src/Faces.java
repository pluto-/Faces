import java.io.IOException;
import java.util.*;

public class Faces {

    public static final int SHAPE_VALLEY = 0;
    public static final int SHAPE_MOUNTAIN = 1;

    public static final int ANSWER_HAPPY = 1;
    public static final int ANSWER_SAD = 2;
    public static final int ANSWER_MISCHIEVOUS = 3;
    public static final int ANSWER_MAD = 4;

    public static void main(String[] args) {

        if(args.length < 3) {
            System.out.println("Usage: Faces [training faces path] [training answers path] [test faces path]");
            System.exit(1);
        }

        ArrayList<Face> trainingFaces = null;
        Map<Integer, Integer> trainingAnswers = null;
        ArrayList<Face> testFaces = null;
        try {
            trainingFaces = FacesLoader.loadFaces(args[0]);
            trainingAnswers = FacesLoader.loadAnswers(args[1]);
            testFaces = FacesLoader.loadFaces(args[2]);
        } catch (IOException e) {
            System.out.println("Could not load files. ERROR: " + e.getMessage());
        }

        Perceptron eyebrowPerceptron = new Perceptron(20, 20, 0.1, 0.9);
        Perceptron mouthPerceptron = new Perceptron(20, 20, 0.1, 0.7);

        double percent = 0;

        while(percent < 0.99) {
            long seed = System.nanoTime();
            Collections.shuffle(trainingFaces, new Random(seed));
            int numberOfCorrectAnswers = 0;
            for (Face face : trainingFaces) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                double[][] image = face.getImage();
                eyebrowPerceptron.learn(image, (correctAnswer - 1) / 2);
                mouthPerceptron.learn(image, (correctAnswer - 1) % 2);
                if (1 + eyebrowPerceptron.answer(image) * 2 + (mouthPerceptron.answer(image)) == trainingAnswers.get(face.getImageNr())) {
                    numberOfCorrectAnswers++;
                }
            }
            percent = (double)numberOfCorrectAnswers / (double)trainingFaces.size();
            System.out.println("percentage : " + percent);
        }

        for(Face face : testFaces) {
            int eyebrowAnswer = eyebrowPerceptron.answer(face.getImage());
            int mouthAnswer = mouthPerceptron.answer(face.getImage());
            System.out.println("Image" + face.getImageNr() + " " + (1 + eyebrowAnswer*2 + mouthAnswer ));
        }
    }
}
