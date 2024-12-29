package indy;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

/**
 * This is a wrapper class for JavaFX Rectangle, modeling the square that
 * composes the board and tiles. It includes a reference to the JavaFX Rectangle
 * and Color as well as has a boolean attribute, squareoccupied, which represents
 * whether a square on the board has been covered by a Tile object or not.
 * This class mainly manages functions that concern an individual game square,
 * such as setting its color, adjusting its occupied status, and adjusting its
 * location.
 */
public class GameSquare {
    private Rectangle square;
    private Color color;
    private boolean squareOccupied;

    /**
     * This is the constructor for the GameSquare class, which
     * instantiates a square and sets its size and gives it a border
     * so that it will have an outline. It also initializes the
     * color to azure and sets the square occupied status to false.
     */
    public GameSquare(){
        this.square = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.setSquareColor(Color.AZURE);
        this.addSquareBorder(Color.MAROON);
        this.setSquareOccupied(false);
    }

    /**
     * Sets the color of the GameSquare to the Color passed in.
     */
    public void setSquareColor(Color color) {
        this.color = color;
        this.square.setFill(this.color);
    }

    /**
     * Adds a maroon colored outline to each GameSquare.
     */
    public void addSquareBorder(Color color){
        this.square.setStroke(color);
        this.square.setStrokeWidth(Constants.BOARDER_STROKE);
    }

    /**
     * Mutator for the occupied status of a gamesquare that
     * takes in a boolean and assigns it to the occupied status
     * of the gamesquare.
     */
    public void setSquareOccupied(boolean occupied){
        this.squareOccupied = occupied;
    }

    /**
     * Accessor for the occupied status of the gamesquare
     * returning true if the square is occupied, thus, has
     * a Tile on it, or false if the square is unoccupied and
     * contains no Tile.
     */
    public boolean getOccupiedStatus(){
        return this.squareOccupied;
    }

    /**
     * Takes in a Pane and adds the gamesquare to
     * that Pane, allowing it to appear graphically.
     */

     void addSquareToPane(Pane game){
        game.getChildren().add(this.square);
    }

    /**
     * Adds a gradient so that the color of the GameSquare fades from its main
     * Color to another color, Papayawhip.
     */
    public void addSquareGradient(){
        Stop[] stop = new Stop[]{new Stop(0, this.color), new Stop(1.0, Color.PAPAYAWHIP)};
        LinearGradient ombre = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stop);
        this.square.setFill(ombre);
    }

    /**
     * Takes in an HBox representing the tileLabel, which includes
     * the labels for the letter and value of the tile, then
     * combines that tileLabel with the Javafx rectangle on
     * one stackpane, which is then returned by the method to
     * model a Tile.
     */
    public StackPane combineTileWithLabel(HBox tileLabel){
        // create pane that will hold both tile and tileLabel
        StackPane tile = new StackPane();
        tile.getChildren().addAll(this.square, tileLabel);
        return tile;
    }

    /**
     * This accessor method returns the x coordinate of the GameSquare.
     */
    public double getSquareX(){
        return this.square.getX();
    }

    /**
     * This accessor method returns the y coordinate of the GameSquare.
     */
    public double getSquareY(){
        return this.square.getY();
    }

    /**
     * This mutator method sets the x and y location of the GameSquare to
     * the two double values that it takes in.
     */

    public void setSquareLoc(double x, double y){
        this.square.setX(x);
        this.square.setY(y);
    }

    /**
     * Takes in a double representing the x coordinate and converts
     * and casts that value to an int representing the corresponding
     * column number on the Scrabble board at that value. Returns column
     * number.
     */
    public int convertXtoCol(double x){
        return (int) (x/Constants.SQUARE_WIDTH - Constants.LOCATION_OFFSET);
    }

    /**
     * Takes in a double representing the y coordinate and converts
     * and casts that value to an int representing the corresponding
     * row number on the Scrabble board at that value. Returns row
     * number
     */
    public int convertYtoRow(double y){
        return (int) (y/Constants.SQUARE_WIDTH - 1);
    }

    /**
     * Returns an int representing the factor by which a tile's
     * value should be multiplied by when placed on a regular square: one.
     */
    public int getTileFactor(){
        return 1;
    }

    /**
     * Returns an int representing the factor by which a whole word's
     * value should be multiplied by when placed on a regular square: one.
     */
    public int getWordFactor(){
        return 1;
    }

    /**
     * Takes in a String, which is the path to an image. Then
     * displays that image on the gamesquare.
     */
    public void setImage(String imagePath){
        Image image = new Image(imagePath);
        this.square.setFill(new ImagePattern(image));
    }
}
