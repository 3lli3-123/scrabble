package indy;

/**
 * This class models a double letter square and is a subclass of gamesquare.
 * It displays the corresponding image of the double letter square and also
 * returns its tile factor.
 */
public class DoubleLetterSquare extends GameSquare{

    /**
     * The DoubleLetterSquare constructor invokes the superclass's
     * constructor to create the rectangle representing the
     * doublelettersquare and also sets the corresponding
     * image onto the rectangle.
     */
    public DoubleLetterSquare(){
        super();
        this.setImage(Constants.DOUBLE_LETTER_IMAGE);
    }

    /**
     * Returns an int representing the factor by which a tile's
     * value should be multiplied by when placed on this special square: two.
     */
    @Override
    public int getTileFactor(){
        return Constants.DOUBLE_FACTOR;
    }
}
