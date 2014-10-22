/** Data holding class. Contains data about a face.
 *
 * Created by Jonas on 2014-10-16.
 */
public class Face implements Cloneable {

    private final double[][] image;
    private final int imageNr;

    /**
     * Constructor
     * @param image pixel data containing greyscale values between 0 and 31.
     * @param imageNr the number of the image.
     */
    public Face(double[][] image, int imageNr) {
        this.image = image;
        this.imageNr = imageNr;
    }

    /**
     * Returns the pixel data.
     * @return pixel data.
     */
    public double[][] getImage() {
        return image;
    }

    /**
     * Returns the image number.
     * @return image number.
     */
    public int getImageNr() {
        return imageNr;
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
