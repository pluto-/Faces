import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
import java.util.*;

public class Faces {

    private static final double EYEBROW_LEARN_RATE = 0.1;
    private static final double MOUTH_LEARN_RATE = 0.1;
    private static final double EYEBROW_THRESHOLD = 0.5;
    private static final double MOUTH_THRESHOLD = 0.5;

    private static final double PERCENT_LIMIT = 0.99;

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

        Perceptron eyebrowPerceptron = new Perceptron(20, 20, EYEBROW_LEARN_RATE, EYEBROW_THRESHOLD);
        Perceptron mouthPerceptron = new Perceptron(20, 20, MOUTH_LEARN_RATE, MOUTH_THRESHOLD);
        Map<Integer, Integer> eyebrowAnswers = new HashMap<Integer, Integer>();
        Map<Integer, Integer> mouthAnswers = new HashMap<Integer, Integer>();

        Iterator<Integer> keys = trainingAnswers.keySet().iterator();
        int key;
        while(keys.hasNext()) {
            key = keys.next();
            eyebrowAnswers.put(key, (trainingAnswers.get(key) - 1) / 2);
            mouthAnswers.put(key, (trainingAnswers.get(key) - 1) % 2);
        }

        train(eyebrowPerceptron, trainingFaces, eyebrowAnswers);
        train(mouthPerceptron, trainingFaces, mouthAnswers);

        for(Face face : testFaces) {
            System.out.println("Image" + face.getImageNr() + " " + getFacialExpression(eyebrowPerceptron.answer(face.getImage()), mouthPerceptron.answer(face.getImage())));
        }
    }

    private static int getFacialExpression(int eyebrowExpression, int mouthExpression) {
        return 1 + eyebrowExpression * 2 + mouthExpression;
    }

    private static double train(Perceptron perceptron, ArrayList<Face> trainingFaces, Map<Integer, Integer> trainingAnswers) {
        double percent = 0;
        while(percent < PERCENT_LIMIT) {
            ArrayList<Face> trainingFacesTemp = (ArrayList<Face>)trainingFaces.clone();
            Collections.shuffle(trainingFacesTemp);
            ArrayList<Face> trainingTestFaces = new ArrayList<Face>();
            int size = (int)((double)trainingFacesTemp.size()/3);
            for(int i = 0; i < size; i++) {
                trainingTestFaces.add(trainingFacesTemp.get(0));
                trainingFacesTemp.remove(0);
            }
            for (Face face : trainingFacesTemp) {
                perceptron.learn(face.getImage(), trainingAnswers.get(face.getImageNr()));
            }
            int numberOfCorrectAnswers = 0;
            for(Face face : trainingTestFaces) {
                if (perceptron.answer(face.getImage()) == trainingAnswers.get(face.getImageNr())) {
                    numberOfCorrectAnswers++;
                }
            }
            percent = (double)numberOfCorrectAnswers / (double)trainingTestFaces.size();
        }
        return percent;
    }
}
