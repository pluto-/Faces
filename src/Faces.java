import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        Perceptron[][] eyebrowPerceptronCandidates = new Perceptron[10][10];
        Perceptron[][] mouthPerceptronCandidates = new Perceptron[10][10];

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                eyebrowPerceptronCandidates[i][j] = new Perceptron(20, 20, (double)i / (double)10, (double)j / (double)10);
                mouthPerceptronCandidates[i][j] = new Perceptron(20, 20, (double)i / (double)10, (double)j / (double)10);
            }
        }

        //Perceptron eyebrowPerceptron = new Perceptron(20, 20, 0.1, 0.5);
        //Perceptron mouthPerceptron = new Perceptron(20, 20, 0.1, 0.5);

        List<Face> trainingTestFaces = new ArrayList<Face>();
        int size = (int)(trainingFaces.size() * 0.33);
        for(int i = 0; i < size; i++) {
            trainingTestFaces.add(trainingFaces.get(0));
            trainingFaces.remove(0);
        }


        double bestPercent = 0;
        int best_i = 0, best_j = 0;

        // Training perceptors.
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                for(Face face : trainingFaces) {
                    byte correctAnswer = -1;
                    if(trainingAnswers.get(face.getImageNr()) == ANSWER_HAPPY || trainingAnswers.get(face.getImageNr()) == ANSWER_SAD) {
                        correctAnswer = SHAPE_MOUNTAIN;
                    } else if(trainingAnswers.get(face.getImageNr()) == ANSWER_MISCHIEVOUS || trainingAnswers.get(face.getImageNr()) == ANSWER_MAD) {
                        correctAnswer = SHAPE_VALLEY;
                    }
                    eyebrowPerceptronCandidates[i][j].learn(face.getImage(), correctAnswer);

                    if(trainingAnswers.get(face.getImageNr()) == ANSWER_SAD || trainingAnswers.get(face.getImageNr()) == ANSWER_MAD) {
                        correctAnswer = SHAPE_MOUNTAIN;
                    } else if(trainingAnswers.get(face.getImageNr()) == ANSWER_MISCHIEVOUS || trainingAnswers.get(face.getImageNr()) == ANSWER_HAPPY) {
                        correctAnswer = SHAPE_VALLEY;
                    }
                    mouthPerceptronCandidates[i][j].learn(face.getImage(), correctAnswer);
                }

                int correctAnswers = 0;
                int wrongAnswers = 0;

                for(Face face : trainingTestFaces) {
                    int eyebrowAnswer = eyebrowPerceptronCandidates[i][j].answer(face.getImage());
                    int mouthAnswer = mouthPerceptronCandidates[i][j].answer(face.getImage());
                    int answer = -1;
                    if(eyebrowAnswer == SHAPE_MOUNTAIN && mouthAnswer == SHAPE_VALLEY) {
                        answer = ANSWER_HAPPY;
                    } else if(eyebrowAnswer == SHAPE_MOUNTAIN && mouthAnswer == SHAPE_MOUNTAIN) {
                        answer = ANSWER_SAD;
                    } else if(eyebrowAnswer == SHAPE_VALLEY && mouthAnswer == SHAPE_VALLEY) {
                        answer = ANSWER_MISCHIEVOUS;
                    } else if(eyebrowAnswer == SHAPE_VALLEY && mouthAnswer == SHAPE_MOUNTAIN) {
                        answer = ANSWER_MAD;
                    }

                    if(trainingAnswers.get(face.getImageNr()) == answer) {
                        correctAnswers++;
                    } else {
                        wrongAnswers++;
                    }
                }

                System.out.println("Learning Rate = " + (double)i / (double)10 + " Threshold = " + (double)j / (double)10 + " Percent correct answers = " +  ((double)correctAnswers / (double)(correctAnswers+wrongAnswers)));
                if(((double)correctAnswers / (double)(correctAnswers+wrongAnswers)) > bestPercent) {
                    bestPercent = ((double)correctAnswers / (double)(correctAnswers+wrongAnswers));
                    best_i = i;
                    best_j = j;
                }

            }
        }

        System.out.println("BEST I " + best_i);
        System.out.println("BEST J " + best_j);
        System.out.println("BEST % " + bestPercent);

        for(Face face : testFaces) {
            int eyebrowAnswer = eyebrowPerceptronCandidates[best_i][best_j].answer(face.getImage());
            int mouthAnswer = mouthPerceptronCandidates[best_i][best_j].answer(face.getImage());
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
