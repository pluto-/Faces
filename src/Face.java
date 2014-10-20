/**
 * Created by Jonas on 2014-10-16.
 */
public class Face {

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
}
