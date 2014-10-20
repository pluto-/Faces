import com.sun.org.apache.bcel.internal.generic.NEW;

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

        Perceptron eyebrowPerceptron = new Perceptron(20, 20, 0.1, 0.5);
        Perceptron mouthPerceptron = new Perceptron(20, 20, 0.1, 0.5);



        double percent = 0;
        while(percent < 0.99) {
            ArrayList<Face> trainingFacesTemp = (ArrayList<Face>)trainingFaces.clone();
            Collections.shuffle(trainingFacesTemp);
            ArrayList<Face> trainingTestFaces = new ArrayList<Face>();
            int size = (int)((double)trainingFacesTemp.size()/3);
            for(int i = 0; i < size; i++) {
                trainingTestFaces.add(trainingFacesTemp.get(0));
                trainingFacesTemp.remove(0);
            }
            for (Face face : trainingFacesTemp) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                eyebrowPerceptron.learn(face.getImage(), (correctAnswer - 1) / 2);
            }
            int numberOfCorrectAnswers = 0;
            for(Face face : trainingTestFaces) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                if (eyebrowPerceptron.answer(face.getImage()) == ((correctAnswer - 1) / 2)) {
                    numberOfCorrectAnswers++;
                }
            }
            percent = (double)numberOfCorrectAnswers / (double)trainingTestFaces.size();
            //System.out.println("percentage : " + percent);
        }
        percent = 0;
        while(percent < 0.99) {
            ArrayList<Face> trainingFacesTemp = (ArrayList<Face>)trainingFaces.clone();
            Collections.shuffle(trainingFacesTemp);
            ArrayList<Face> trainingTestFaces = new ArrayList<Face>();
            int size = (int)((double)trainingFacesTemp.size()/3);
            for(int i = 0; i < size; i++) {
                trainingTestFaces.add(trainingFacesTemp.get(0));
                trainingFacesTemp.remove(0);
            }
            for (Face face : trainingFacesTemp) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                mouthPerceptron.learn(face.getImage(), (correctAnswer - 1) % 2);
            }
            int numberOfCorrectAnswers = 0;
            for(Face face : trainingTestFaces) {
                int correctAnswer = trainingAnswers.get(face.getImageNr());
                if (mouthPerceptron.answer(face.getImage()) == ((correctAnswer - 1) % 2)) {
                    numberOfCorrectAnswers++;
                }
            }
            percent = (double)numberOfCorrectAnswers / (double)trainingTestFaces.size();
            //System.out.println("percentage : " + percent);
        }

        for(Face face : testFaces) {
            System.out.println("Image" + face.getImageNr() + " " + getFacialExpression(eyebrowPerceptron.answer(face.getImage()), mouthPerceptron.answer(face.getImage())));
        }
    }

    private static int getFacialExpression(int eyebrowExpression, int mouthExpression) {
        return 1 + eyebrowExpression * 2 + mouthExpression;
    }
}
