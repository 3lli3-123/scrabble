package indy;

/**
 * This class models a double word square and is a subclass of gamesquare.
 * It displays the corresponding image of the double word square and also
 * returns its word factor.
 */
public class DoubleWordSquare extends GameSquare{

    /**
     * The DoubleWordSquare constructor invokes the superclass's
     * constructor to create the rectangle representing the
     * doublewordsquare and also sets the corresponding
     * image onto the rectangle.
     */
    public DoubleWordSquare(){
        super();
        this.setImage(Constants.DOUBLE_WORD_IMAGE);
    }

    /**
     * Returns an int representing the factor by which a whole word's
     * value should be multiplied by when placed on this special square: two.
     */
    @Override
    public int getWordFactor(){
        return Constants.DOUBLE_FACTOR;
    }
}
