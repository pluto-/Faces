package faces;

import utils.Face;
import utils.FacesLoader;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Faces {

    public static void main(String[] args) {

        if(args.length < 3) {

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
    }
}
