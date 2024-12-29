package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class models a tile in scrabble and is a subclasss of GameSquare.
 * It contains a reference to a squareBeneath, and tilePane.
 * It mainly handles functions relevant to a single tile, such as setting
 * its color, creating its letter and number label, being dragged and
 * adjusting location, and storing the board square that lies directly
 * below it when placed onto the board.
 */
public class Tile extends GameSquare{
    private StackPane tilePane;
    private String letter;
    private int pointValue;
    private GameSquare squareBeneath;
    private double centerX;
    private double centerY;
    private double initialX;
    private boolean onBoard;
    private int idNumber;

    /**
     * The Tile constructor first invokes the superclass's
     * constructor to instantiate the Rectangle representing the tile.
     * Additionally, it takes in a String representing the letter
     * of the tile, which is stored as an instance variable, as well as
     * an int representing the point value of the tile, also stored as
     * an instance variable. Furthermore, the tile's color is set and
     * the tile gains a letter and number on its appearance. Finally,
     * initializes the onBoard status to false, since none of the
     * tiles have been placed on the game board when instantiated.
     */
    public Tile(String letter, int value) {
        super();
        this.letter = letter;
        this.pointValue = value;
        this.addLetterScoreLabel();
        this.setSquareColor(Color.BURLYWOOD);
        this.addSquareGradient();
        this.onBoard = false;
    }

    /**
     * Instantiates the tileLabel which will hold two labels
     * representing the tile's letter and the tile's point value.
     * Then combines that tileLabel with the Rectangle shape of
     * the tile to make the tile appear graphically with a letter
     * and a number. Also initializes the tilePane to the stack
     * pane returned by the superclass combinetilewith label method.
     */
    private void addLetterScoreLabel(){
        // create tileLabel that will hold letter and number
        HBox tileLabel = new HBox();
        Label letterLabel = new Label(this.letter);
        Font font = Font.font("Arial", FontWeight.BOLD, Constants.LETTER_FONT_SIZE);
        letterLabel.setFont(font);

        Label valueLabel = new Label(" " + this.pointValue);
        font = Font.font("Arial", FontWeight.BOLD, Constants.POINT_VAL_FONT_SIZE);
        valueLabel.setFont(font);

        tileLabel.setAlignment(Pos.CENTER);
        tileLabel.getChildren().addAll(letterLabel, valueLabel);

        // add tile and tileLabel to same stackPane
        this.tilePane = this.combineTileWithLabel(tileLabel);

    }

    /**
     * Mutator that takes in an int representing the ID number
     * of the tile, and sets its idNUmber to that int.
     */
    public void setIdNumber(int id){
        this.idNumber = id;
    }

    /**
     * Accessor that returns the idNumber of the tile.
     */
    public int getIdNumber(){
        return this.idNumber;
    }

    /**
     * Mutator that takes in a boolean and assigns it
     * to onBoard, which is a boolean representing whether
     * a tile has been added to the game board or not.
     */
    public void setOnBoardStatus(boolean dropped){
        this.onBoard = dropped;
    }

    /**
     * Accessor that returns boolean onBoard
     * which is a boolean representing whether
     * the tile has been added to the game board or not.
     */
    public boolean getOnBoardStatus(){
        return this.onBoard;
    }

    /**
     * Takes in a double that represents the initial x position
     * of the tile on the tile rack.
     */
    public void setInitialX(double x){
        this.initialX = x;
    }

    /**
     * Returns a double that represents the initial x position
     * of the tile on the tile rack.
     */
    public double getInitialX(){
        return this.initialX;
    }

    /**
     * Takes in two doubles representing the x and y coordinate of
     * the tile and sets the tile's location to those coordinates.
     */
    public void setTileLocation(double x, double y){
        this.tilePane.setLayoutX(x);
        this.tilePane.setLayoutY(y);
        this.setSquareLoc(x, y);
    }

    /**
     * Takes in two doubles that represent the desired x and y coordinates
     * of the tile's center, then sets the tile's center to those coordinates.
     */
    private void setTileCenter(double x, double y){
        this.centerX = x;
        this.centerY = y;
        this.tilePane.setLayoutX(this.centerX - Constants.SQUARE_MIDPOINT);
        this.tilePane.setLayoutY(this.centerY - Constants.SQUARE_MIDPOINT);
    }

    /**
     * Accessor for the x coordinate of the tile's center.
     * Returns double representing that coordinate.
     */
    public double getCenterX(){
        return this.centerX;
    }

    /**
     * Accessor for the y coordinate of the tile's center.
     * Returns double representing that coordinate.
     */
    public double getCenterY(){
        return this.centerY;
    }

    /**
     * Takes in a string representing the letter of the tile
     * and sets that string as the tile's letter both graphically
     * and logically. Useful for blank tile.
     */
    public void setLetter(String letter){
        letter = letter.toUpperCase();
        this.letter = letter;
        Label letterLabel = new Label(letter);
        Font font = Font.font("Arial", FontWeight.BOLD, Constants.LETTER_FONT_SIZE);
        letterLabel.setFont(font);
        this.tilePane.getChildren().add(letterLabel);
    }

    /**
     * Takes in a gamesquare representing a square on the baord
     * and stores it as the squarebeneath, representing the square
     * below the tile when it is dropped onto the board.
     */
    public void setSquareBeneath(GameSquare square){
        this.squareBeneath = square;
    }

    /**
     * Returns the squarebeneath, representing the square
     * below the tile when it is dropped onto the board.
     */
    public GameSquare getSquareBeneath(){
        return this.squareBeneath;
    }

    /**
     * Accessor that returns a String representing the letter
     * of the tile.
     */
    public String getLetter(){
        return this.letter;
    }

    /**
     * Accessor that returns an int representing the point value
     * of the tile.
     */
    public int getTileValue(){
        return this.pointValue;
    }

    /**
     * Clears the letter from a tile both graphically
     * and logically.
     */
    public void clearTileLetter(){
        this.letter = " ";
        if (this.tilePane.getChildren().size() == 2) {
            this.tilePane.getChildren().remove(this.tilePane.getChildren().get(1));
        }
    }

    /**
     * Takes in a Pane which represents the game pane and adds
     * the tilePane to it, enabling it to appear graphically.
     */
    public void displayTileInGame(Pane game){
        // if the tile is not already on the board
        if (!game.getChildren().contains(this.tilePane)) {
            game.getChildren().add(this.tilePane);
        }
        this.sendFrontOfPane();
    }

    /**
     * Makes the tilePane representing the tile unresponsive
     * to mouse events, including clicking, dragging, and releasing.
     */
    public void unresponsiveToMouse(){
        this.tilePane.setMouseTransparent(true);
    }

    /**
     * Takes in an arraylist of Tile objects representing the newly placed
     * tiles as well as an HBox representing the messageBox displaying
     * messages to the user, and a board representing the game board, then
     * enables a Tile object to respond to mouse events by calling corresponding
     * helper methods on each mouse event scenario.
     */
    public void beDragged(ArrayList<Tile> placedTiles, HBox messageBox, Board board) {
            // first highlight the tile that is selected
            this.tilePane.setOnMousePressed(event -> this.reactToMousePressed(messageBox));

            // enabling drag movement
            this.tilePane.setOnMouseDragged(event -> this.setTileCenter(event.getSceneX(), event.getSceneY()));

            // get rid of highlight once mouse released as well as locking it into the board
            // by deleting the board square there and replacing it with the tile both graphically
            // and logically
            this.tilePane.setOnMouseReleased(event -> this.reactToRelease(event, placedTiles, board));
        }

    /**
     * Takes in an HBox representing the messageBox to the user
     * and reacts to the press of a mouse by highlighting the
     * tile yellow and moving the messageBox to the back of the
     * gamePane, thus hiding any messages that were previously
     * displaying.
     */
    private void reactToMousePressed(HBox message){
        this.tilePane.toFront();
        this.addSquareBorder(Color.YELLOW);
        if (this.getSquareBeneath() != null){
            this.getSquareBeneath().setSquareOccupied(false);
        }
        message.toBack();
    }

    /**
     * Takes in a MouseEvent representing the current action of the mouse,
     * an arraylist of Tile objects, placedTiles, representing newly placed tiles
     * and a Board object representing the game board. Reacts to the release of
     * a mouse by first checking if the tile is allowed to be dropped at the location
     * where the mouse is, and only dropping it if that location is within board bounds
     * and not on another tile. The tile is dropped onto a specific board square
     * depending on which board square the center of the tile is within. If dropped
     * in an invalid location, the tile is sent back to the rack. Highlight around tile
     * disappears.
     */
    public void reactToRelease(MouseEvent e, ArrayList<Tile> placedTiles, Board board) {
        // BEFORE DOING ANYTHING ONLY let tile be dropped if it is within board bounds
        if ((e.getSceneX() >= Constants.SQUARE_MIDPOINT && e.getSceneX() < Constants.BOARD_COL *
                Constants.SQUARE_WIDTH + Constants.SQUARE_MIDPOINT) && (e.getSceneY() >= Constants.SQUARE_WIDTH
                && e.getSceneY() < Constants.BOARD_ROW * Constants.SQUARE_WIDTH + Constants.SQUARE_WIDTH)) {
            // tell board to drop the tile onto the board if it does not overlap with another tile
            if (board.dropTileToBoard(this)) {
                // revert outline color
                this.addSquareBorder(Color.MAROON);
                // conditionally add the tile to the placedTiles arraylist only if
                // it is not already in the arraylist
                if (!placedTiles.contains(this)) {
                    placedTiles.add(this);
                }
            }
            // if tile is dropped on top of another tile, return to rack
            else {
                this.returnTileToRack(placedTiles);
            }
        }

        // if tile is attempted to be dropped outside of board bounds, return tile to initial location
        else {
            this.returnTileToRack(placedTiles);
        }
        e.consume();
    }

    /**
     * Returns the tile to the tile rack and removes that tile from
     * the arraylist of recently placed tiles if it was first placed
     * on the game board then moved to an invalid location.
     */
    private void returnTileToRack(ArrayList<Tile> placedTiles){
        this.addSquareBorder(Color.MAROON);
        this.setTileLocation(this.getInitialX(), 0);
        // if that tile was in the placedTiles arrayList, remove it
        if (placedTiles.contains(this)){
            placedTiles.remove(this);
        }
    }

    /**
     * Hides the tile by sending it to the back of the gamePane.
     */
    public void sendBackOfPane(){
        this.tilePane.toBack();
    }

    /**
     * Makes the tile display by sending the tile to the front of the gamePane.
     */
    public void sendFrontOfPane(){
        this.tilePane.toFront();
    }

    /**
     * Determines how to differentiate every tile in the tile bag.
     * Takes in an object and indicates that the object is only equivalent
     * to a Tile object if it has the same tile ID number, which is unique
     * to ever tile in the bag. This method is useful for distinguishing
     * tiles with the same letter and point value. Returns true if the object
     * is considered equivalent to, thus the same as, the tile, and false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }

        if (o == null || this.getClass() != o.getClass()){
            return false;
        }

        Tile tile = (Tile) o;
        return this.getIdNumber() == tile.getIdNumber();
    }

    /**
     * Provides a way of computing a mathematical representation of a Tile object
     * and returns an int representing that mathematical representation.
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.letter, this.pointValue);
    }

}
