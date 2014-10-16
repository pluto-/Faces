package utils;

/**
 * Created by Jonas on 2014-10-16.
 */
public class Face {

    public static final int CORRECT_ANSWER_NO_ANSWER = -1;
    public static final int CORRECT_ANSWER_HAPPY = 1;
    public static final int CORRECT_ANSWER_SAD = 2;
    public static final int CORRECT_ANSWER_MISCHIEVOUS = 3;
    public static final int CORRECT_ANSWER_MAD = 4;

    private byte[][] image;
    private int correctAnswer;

    public Face(byte[][] image, int correctAnswer) {
        this.image = image;
        this.correctAnswer = correctAnswer;
    }

    public Face(byte[][] image) {
        this(image, CORRECT_ANSWER_NO_ANSWER);
    }

    public byte[][] getImage() {
        return image;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
