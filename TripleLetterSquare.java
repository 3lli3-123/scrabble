package indy;

/**
 * This class models a triple letter square and is a subclass of gamesquare.
 * It displays the corresponding image of the triple letter square and also
 * returns its tile factor.
 */
public class TripleLetterSquare extends GameSquare {

    /**
     * The TripleLetterSquare constructor invokes the superclass's
     * constructor to create the rectangle representing the
     * triplelettersquare and also sets the corresponding
     * image onto the rectangle.
     */
    public TripleLetterSquare(){
        super();
        this.setImage(Constants.TRIPLE_LETTER_IMAGE);
    }

    /**
     * Returns an int representing the factor by which a tile's
     * value should be multiplied by when placed on this special square: three.
     */
    @Override
    public int getTileFactor(){
        return Constants.TRIPLE_FACTOR;
    }
}
