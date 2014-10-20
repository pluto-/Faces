import java.io.IOException;
import java.util.*;

public class Faces {

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
            Collections.shuffle(trainingFaces, new Random(System.nanoTime()));
            int numberOfCorrectAnswers = 0;
            for (Face face : trainingFaces) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                double[][] image = face.getImage();
                eyebrowPerceptron.learn(image, (correctAnswer - 1) / 2);
                mouthPerceptron.learn(image, (correctAnswer - 1) % 2);
                if (getFacialExpression(eyebrowPerceptron.answer(image), mouthPerceptron.answer(image)) == trainingAnswers.get(face.getImageNr())) {
                    numberOfCorrectAnswers++;
                }
            }
            percent = (double)numberOfCorrectAnswers / (double)trainingFaces.size();
            System.out.println("percentage : " + percent);
        }

        for(Face face : testFaces) {
            int eyebrowAnswer = eyebrowPerceptron.answer(face.getImage());
            int mouthAnswer = mouthPerceptron.answer(face.getImage());
            System.out.println("Image" + face.getImageNr() + " " + getFacialExpression(eyebrowPerceptron.answer(face.getImage()), mouthPerceptron.answer(face.getImage())));
        }
    }

    private static int getFacialExpression(int eyebrowExpression, int mouthExpression) {
        return 1 + eyebrowExpression * 2 + mouthExpression;
    }
}
