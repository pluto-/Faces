import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 2014-10-16.
 */
public class FacesLoader {

    public static ArrayList<Face> loadFaces(String facesPath) throws IOException {
        ArrayList<Face> faces = new ArrayList<Face>();

        BufferedReader facesBufferedReader = new BufferedReader(new FileReader(facesPath));
        try {
            String line = facesBufferedReader.readLine();

            while (line != null) {
                if(line.length() > "Image".length() && line.substring(0, "Image".length()).equals("Image")) {
                    int imageNr = Integer.valueOf(line.substring("Image".length()));
                    byte[][] image = new byte[20][20];
                    for(int i = 0; i < 20; i++) {
                        line = facesBufferedReader.readLine();

                        for(int j = 0; j < 20; j++) {
                            if(line.indexOf(' ') == -1) {
                                image[i][j] = Byte.valueOf(line);
                            } else {
                                image[i][j] = Byte.valueOf(line.substring(0, line.indexOf(' ')));

                            }
                            if(line.indexOf(' ') != -1) {
                                line = line.substring(line.indexOf(' ') + 1);
                            }
                        }
                    }
                    faces.add(new Face(image, imageNr));
                }
                line = facesBufferedReader.readLine();
            }
        } finally {
            facesBufferedReader.close();
        }

        return faces;
    }

    public static Map<Integer, Integer> loadAnswers(String answersPath) throws IOException {
        Map<Integer, Integer> answers = new HashMap<Integer, Integer>();

        BufferedReader answersBufferedReader = new BufferedReader(new FileReader(answersPath));
        try {
            String line = answersBufferedReader.readLine();

            while (line != null) {
                if(line.length() > "Image".length() && line.substring(0, "Image".length()).equals("Image")) {
                    int imageNr = Integer.valueOf(line.substring("Image".length(), line.indexOf(' ')));
                    line = line.substring(line.indexOf(' ') + 1);
                    int answer = Integer.valueOf(line);
                    answers.put(imageNr, answer);
                }
                line = answersBufferedReader.readLine();
            }
        } finally {
            answersBufferedReader.close();
        }
        return answers;
    }
}
