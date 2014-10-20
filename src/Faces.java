import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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

        Perceptron eyebrowPerceptron = new Perceptron(20, 20, 0.1, 0.5);
        Perceptron mouthPerceptron = new Perceptron(20, 20, 0.1, 0.5);

        // Training eyebrow perceptor.
        for(Face face : trainingFaces) {
            byte correctAnswer = -1;
            if(trainingAnswers.get(face.getImageNr()) == ANSWER_HAPPY || trainingAnswers.get(face.getImageNr()) == ANSWER_SAD) {
                correctAnswer = SHAPE_MOUNTAIN;
            } else if(trainingAnswers.get(face.getImageNr()) == ANSWER_MISCHIEVOUS || trainingAnswers.get(face.getImageNr()) == ANSWER_MAD) {
                correctAnswer = SHAPE_VALLEY;
            }
            eyebrowPerceptron.learn(face.getImage(), correctAnswer);
        }

        // Training mouth perceptor.
        for(Face face : trainingFaces) {
            byte correctAnswer = -1;
            if(trainingAnswers.get(face.getImageNr()) == ANSWER_SAD || trainingAnswers.get(face.getImageNr()) == ANSWER_MAD) {
                correctAnswer = SHAPE_MOUNTAIN;
            } else if(trainingAnswers.get(face.getImageNr()) == ANSWER_MISCHIEVOUS || trainingAnswers.get(face.getImageNr()) == ANSWER_HAPPY) {
                correctAnswer = SHAPE_VALLEY;
            }
            mouthPerceptron.learn(face.getImage(), correctAnswer);
        }

        for(Face face : testFaces) {
            int eyebrowAnswer = eyebrowPerceptron.answer(face.getImage());
            int mouthAnswer = mouthPerceptron.answer(face.getImage());
            if(eyebrowAnswer == SHAPE_MOUNTAIN && mouthAnswer == SHAPE_VALLEY) {
                System.out.println("Image" + face.getImageNr() + " " + ANSWER_HAPPY);
            } else if(eyebrowAnswer == SHAPE_MOUNTAIN && mouthAnswer == SHAPE_MOUNTAIN) {
                System.out.println("Image" + face.getImageNr() + " " + ANSWER_SAD);
            } else if(eyebrowAnswer == SHAPE_VALLEY && mouthAnswer == SHAPE_VALLEY) {
                System.out.println("Image" + face.getImageNr() + " " + ANSWER_MISCHIEVOUS);
            } else if(eyebrowAnswer == SHAPE_VALLEY && mouthAnswer == SHAPE_MOUNTAIN) {
                System.out.println("Image" + face.getImageNr() + " " + ANSWER_MAD);
            }
        }

    }
}
