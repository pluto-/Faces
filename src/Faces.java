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

        Map<Integer, Integer> eyebrowAnswers = new HashMap<Integer, Integer>();
        Map<Integer, Integer> mouthAnswers = new HashMap<Integer, Integer>();
        Iterator<Integer> keys = trainingAnswers.keySet().iterator();
        while(keys.hasNext()) {
            int key = keys.next();
            if(trainingAnswers.get(key) == ANSWER_HAPPY) {
                eyebrowAnswers.put(key, SHAPE_MOUNTAIN);
                mouthAnswers.put(key, SHAPE_VALLEY);
            } else if(trainingAnswers.get(key) == ANSWER_SAD) {
                eyebrowAnswers.put(key, SHAPE_MOUNTAIN);
                mouthAnswers.put(key, SHAPE_MOUNTAIN);
            } else if(trainingAnswers.get(key) == ANSWER_MISCHIEVOUS) {
                eyebrowAnswers.put(key, SHAPE_VALLEY);
                mouthAnswers.put(key, SHAPE_VALLEY);
            } else if(trainingAnswers.get(key) == ANSWER_MAD) {
                eyebrowAnswers.put(key, SHAPE_VALLEY);
                mouthAnswers.put(key, SHAPE_MOUNTAIN);
            }
        }

        ArrayList<Face> trainingTestFaces = new ArrayList<Face>();
        int size = (int)(trainingFaces.size() * 0.33);
        for(int i = 0; i < size; i++) {
            trainingTestFaces.add(trainingFaces.get(i));
            //trainingFaces.remove(0);
        }

        Perceptron eyebrowPerceptron = getBestPerceptron(trainingFaces, trainingTestFaces, eyebrowAnswers);
        Perceptron mouthPerceptron = getBestPerceptron(trainingFaces, trainingTestFaces, mouthAnswers);

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

    private static Perceptron getBestPerceptron(ArrayList<Face> trainingFaces, ArrayList<Face> trainingTestFaces, Map<Integer, Integer> trainingAnswers) {

        Perceptron candidate = null;

        double bestPercent = 0.0;
        int best_i = 0,best_j = 0;
        Perceptron bestPerceptron = null;

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 30; j++) {

                candidate = new Perceptron(20, 20, 0.1 + ((double)i / (double)10), ((double)j / (double)10));

                for(Face face : trainingFaces) {
                    candidate.learn(face.getImage(), trainingAnswers.get(face.getImageNr()).byteValue());
                }

                int correctAnswers = 0;
                int wrongAnswers = 0;

                for(Face face : trainingTestFaces) {
                    int answer = candidate.answer(face.getImage());

                    if(trainingAnswers.get(face.getImageNr()) == answer) {
                        correctAnswers++;
                    } else {
                        wrongAnswers++;
                    }
                }

                if(((double)correctAnswers / (double)(correctAnswers+wrongAnswers)) > bestPercent) {
                    bestPercent = ((double)correctAnswers / (double)(correctAnswers+wrongAnswers));
                    best_i = i;
                    best_j = j;
                    bestPerceptron = candidate;
                }

            }
        }

        /*System.out.println("BEST PERCENT = " + bestPercent);
        System.out.println("BEST LEARNING RATE = " + (0.1+(double)best_i/(double)10));
        System.out.println("BEST THRESHOLD = " + ((double)best_j/(double)10));*/
        return bestPerceptron;
    }
}
