package indy;

/**
 * This class models a triple word square and is a subclass of gamesquare.
 * It displays the corresponding image of the triple word square and also
 * returns its word factor.
 */
public class TripleWordSquare extends GameSquare {

    /**
     * The TripleWordSquare constructor invokes the superclass's
     * constructor to create the rectangle representing the
     * triplewordsquare and also sets the corresponding
     * image onto the rectangle.
     */
    public TripleWordSquare(){
        super();
        this.setImage(Constants.TRIPLE_WORD_IMAGE);
    }

    /**
     * Returns an int representing the factor by which a whole word's
     * value should be multiplied by when placed on this special square: three.
     */
    @Override
    public int getWordFactor(){
        return Constants.TRIPLE_FACTOR;
    }
}
