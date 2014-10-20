/**
 * Created by Jonas on 2014-10-16.
 */
public class Face {

    private byte[][] image;
    private int imageNr;

    public Face(byte[][] image, int imageNr) {
        this.image = image;
        this.imageNr = imageNr;
    }

    public byte[][] getImage() {
        return image;
    }

    public int getImageNr() {
        return imageNr;
    }
}
