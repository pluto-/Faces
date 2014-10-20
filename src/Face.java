/**
 * Created by Jonas on 2014-10-16.
 */
public class Face implements Cloneable {

    private double[][] image;
    private int imageNr;

    public Face(double[][] image, int imageNr) {
        this.image = image;
        this.imageNr = imageNr;
    }

    public double[][] getImage() {
        return image;
    }

    public int getImageNr() {
        return imageNr;
    }

    @Override
    public Face clone() {
        return new Face(image.clone(), imageNr);
    }

    public String toString() {
        String str = "";
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                str = str.concat(image[i][j] + " ");
            }
            str = str.concat("\n");
        }
        return str;
    }
}
