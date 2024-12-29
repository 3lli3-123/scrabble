package indy;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * As the top-level logic class, the Game class contains references to parts of the
 * game such as the gamePane, gameBoard, referee, and playerTiles. This class mainly
 * manages the broader elements of Scrabble, such as creating the tileBag, switching
 * player turns, and ending the game.
 */
public class Game {
    private Pane gamePane;
    private HBox buttonPane;
    private Board gameBoard;
    private Referee referee;
    private ArrayList<Tile> tileBag;
    private ArrayList<Tile> placedTiles;
    private Tile[] playerOneTiles;
    private Tile[] playerTwoTiles;
    private HBox messageBox;
    private Text userMessage;
    private Text playerOneScoreBox;
    private Text playerTwoScoreBox;
    private int playerOneScore;
    private int playerTwoScore;
    private boolean playerOneTurn;
    private boolean gameOver;

    /**
     * The Game constructor initializes the gamePane and buttonPane to the Pane and
     * HBox parameter passed in, as well as instantiates a referee, a gameBoard, a
     * tileBag, the playerTiles, and the messageBox, among other components of the game.
     * It makes the first player's tile appear on the board, and it generates all six buttons.
     */
    public Game(Pane game, HBox button) {
        this.gamePane = game;
        this.buttonPane = button;
        this.playerOneTurn = true;
        this.setUpScorePane();
        this.playerOneTiles = new Tile[Constants.STARTING_PLAYER_TILES];
        this.playerTwoTiles = new Tile[Constants.STARTING_PLAYER_TILES];
        this.tileBag = new ArrayList<>();
        this.placedTiles = new ArrayList<>();
        this.gameBoard = new Board(this.gamePane);
        this.setUpUserMessage();
        this.referee = new Referee(this.placedTiles, this.gamePane, this.gameBoard);
        this.setUpTileBag();
        this.setUpTileRack();
        this.generatePlayerTiles();
        this.gameOver = false;
        this.setUpButtons();
        this.gamePane.setOnKeyPressed((KeyEvent e) -> this.handleKeyPress(e));
    }

    /**
     * Sets up the score boxes for both players by instantiating a scorePane and adding
     * both Text objects representing player score boxes onto it set with initial
     * texts alerting the users that both players' scores are zero.
     */
    private void setUpScorePane(){
        this.playerOneScore = 0;
        this.playerTwoScore = 0;

        HBox scorePane = new HBox();
        scorePane.setPrefHeight(Constants.SQUARE_WIDTH);

        this.playerOneScoreBox = this.makeScoreTemplate(Constants.PLAYER_ONE_SCORE +
                "\n" + this.playerOneScore);
        this.playerTwoScoreBox = this.makeScoreTemplate(Constants.PLAYER_TWO_SCORE +
                "\n" + this.playerTwoScore);

        scorePane.getChildren().addAll(this.playerOneScoreBox, this.playerTwoScoreBox);
        scorePane.setSpacing(Constants.SCORE_SPACING);
        this.gamePane.getChildren().add(scorePane);
    }

    /**
     * Helper method that takes in a String representing the initial text
     * that the scorebox should display. Instantiates a scoreBox and sets
     * the font style and its fill color.
     */
    private Text makeScoreTemplate(String text){
        Text scoreBox = new Text(text);
        scoreBox.setTextAlignment(TextAlignment.CENTER);
        scoreBox.setFont(Font.font("Verdana", FontWeight.BOLD, Constants.SCORE_FONT));
        scoreBox.setFill(Color.MAROON);

        InnerShadow shadow = new InnerShadow();
        shadow.setOffsetX(Constants.INNER_SHADOW);
        shadow.setOffsetY(Constants.INNER_SHADOW);
        scoreBox.setEffect(shadow);

        scoreBox.setFocusTraversable(false);
        return scoreBox;
    }

    /**
     * Instantiates a messageBox and a userMessage that will display various messages
     * to the user over the course of the game. Sets the font style of the text in
     * the messageBox. The first text that the messageBox is set to is alerting the user
     * that it is the first player's turn.
     */
    private void setUpUserMessage(){
        this.messageBox = new HBox();
        this.messageBox.setLayoutX(0);
        this.messageBox.setLayoutY(Constants.MESSAGE_Y);
        this.messageBox.setPrefSize(Constants.SCENE_WIDTH, Constants.BUTTON_PANE_HEIGHT);
        this.gamePane.getChildren().add(this.messageBox);

        this.userMessage = new Text(Constants.PLAYER_ONE_MESSAGE);
        this.userMessage.setY(Constants.MESSAGE_Y);
        this.userMessage.setFont(Font.font("Verdana", FontWeight.BOLD, Constants.FONT_SIZE));
        this.userMessage.setFill(Color.MAROON);
        this.userMessage.setTextAlignment(TextAlignment.CENTER);

        InnerShadow shadow = new InnerShadow();
        shadow.setOffsetX(Constants.INNER_SHADOW);
        shadow.setOffsetY(Constants.INNER_SHADOW);
        this.userMessage.setEffect(shadow);

        this.messageBox.getChildren().add(this.userMessage);
        this.messageBox.setAlignment(Pos.CENTER);
        this.messageBox.setFocusTraversable(false);
    }

    /**
     * Logically creates the tileBag by populating a hashmap
     * with Tile objects of every tile type as the key of the
     * hashmap and setting the frequency of each type of tile in the
     * tile bag as the value of the hashmap. For every entry in the
     * hashmap, each key (Tile) is instantiated and added to the tile
     * bag arraylist the same number of times as its value (frequency
     * in the tile bag). Every tile added to the tileBag has a unique ID
     * number which is an int from 1 to 100 since there are 100 total
     * tiles in the tilebag. This ID is used to distinguish tiles. Also,
     * every tile in the tile bag is given the ability to be dragged
     * by the mouse.
     */
    private void setUpTileBag() {
        HashMap<Tile, Integer> tiles = new HashMap<>();

        tiles.put((new Tile("A", Constants.AEIOULNSTR_POINT_VAL)), Constants.IA_FREQ);
        tiles.put((new Tile("B", Constants.BCMP_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("C", Constants.BCMP_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("D", Constants.DG_POINT_VAL)), Constants.DUSL_FREQ);
        tiles.put((new Tile("E", Constants.AEIOULNSTR_POINT_VAL)), Constants.E_FREQ);
        tiles.put((new Tile("F", Constants.FHVWY_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("G", Constants.DG_POINT_VAL)), Constants.G_FREQ);
        tiles.put((new Tile("H", Constants.FHVWY_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("I", Constants.AEIOULNSTR_POINT_VAL)), Constants.IA_FREQ);
        tiles.put((new Tile("J", Constants.JX_POINT_VAL)), Constants.ZXQJK_FREQ);
        tiles.put((new Tile("K", Constants.K_POINT_VAL)), Constants.ZXQJK_FREQ);
        tiles.put((new Tile("L", Constants.AEIOULNSTR_POINT_VAL)), Constants.DUSL_FREQ);
        tiles.put((new Tile("M", Constants.BCMP_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("N", Constants.AEIOULNSTR_POINT_VAL)), Constants.TRN_FREQ);
        tiles.put((new Tile("O", Constants.AEIOULNSTR_POINT_VAL)), Constants.O_FREQ);
        tiles.put((new Tile("P", Constants.BCMP_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("Q", Constants.QZ_POINT_VAL)), Constants.ZXQJK_FREQ);
        tiles.put((new Tile("R", Constants.AEIOULNSTR_POINT_VAL)), Constants.TRN_FREQ);
        tiles.put((new Tile("S", Constants.AEIOULNSTR_POINT_VAL)), Constants.DUSL_FREQ);
        tiles.put((new Tile("T", Constants.AEIOULNSTR_POINT_VAL)), Constants.TRN_FREQ);
        tiles.put((new Tile("U", Constants.AEIOULNSTR_POINT_VAL)), Constants.DUSL_FREQ);
        tiles.put((new Tile("V", Constants.FHVWY_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("W", Constants.FHVWY_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("X", Constants.JX_POINT_VAL)), Constants.ZXQJK_FREQ);
        tiles.put((new Tile("Y", Constants.FHVWY_POINT_VAL)), Constants.YWVHFPMCB_FREQ);
        tiles.put((new Tile("Z", Constants.QZ_POINT_VAL)), Constants.ZXQJK_FREQ);
        tiles.put((new BlankTile(" ", Constants.BLANK_POINT_VAL)), Constants.YWVHFPMCB_FREQ);

        // declare local var numId to represent the id of each tile
        int numId = 0;

        // iterate through hashmap in outer loop and iterate through the freq value in inner loop
        // add letter however many times necessary
        for (Map.Entry<Tile, Integer> entry : tiles.entrySet()) {

            int count = entry.getValue();

            for (int i = 0; i < count; i++) {
                Tile myTile;
                if (entry.getKey().getLetter() != " ") {
                    myTile = new Tile(entry.getKey().getLetter(), entry.getKey().getTileValue());
                }

                else {
                    myTile = new BlankTile(entry.getKey().getLetter(), entry.getKey().getTileValue());
                }

                //enable drag ability for all starting tiles
                myTile.beDragged(this.placedTiles, this.messageBox, this.gameBoard);

                // set id number
                numId++;
                myTile.setIdNumber(numId);

                this.tileBag.add(myTile);
            }
        }
    }

    /**
     * Instantiates a tileRack, which is a Rectangle that all seven of the player's
     * tiles are placed on. The rectangle is added to the gamePane so that it may
     * graphically appear.
     */
    private void setUpTileRack(){
        Rectangle tileRack = new Rectangle(Constants.SQUARE_WIDTH*Constants.STARTING_PLAYER_TILES,
                Constants.SQUARE_WIDTH, Color.MAROON);
        this.gamePane.getChildren().add(tileRack);
        tileRack.setX(Constants.TILE_RACK_OFFSET * Constants.SQUARE_WIDTH);
        tileRack.setY(0);
    }

    /**
     * Generates both players' starting collection of seven tiles
     * logically by randomly attaining a tile from the tile bag
     * and adding it to player one's tile array, then attaining
     * another random tile from the tile bag and adding it to player
     * two's tile array. This is done for seven iterations, leading
     * to each player having seven starting tiles. Additionally, the
     * initial x position of each tile is calculated based on its index
     * and it is stored, to determine where on the tile rack the tile will
     * be displayed.
     */
    private void generatePlayerTiles() {
        for (int i = 0; i < Constants.STARTING_PLAYER_TILES; i++) {
            // generate a random new index representing a tile the tile bag
            Tile tileOne = this.tileBag.remove((int) (Math.random() * (this.tileBag.size())));
            this.playerOneTiles[i] = tileOne;

            Tile tileTwo = this.tileBag.remove((int) (Math.random() * (this.tileBag.size())));
            this.playerTwoTiles[i] = tileTwo;

            // determine initial x coord of tile and use it to graphically add tile
            // to the correct location. Then store initial x of Tile
            double xVal = Constants.SQUARE_WIDTH * (i + Constants.TILE_RACK_OFFSET);
            tileOne.setInitialX(xVal);
            tileTwo.setInitialX(xVal);
        }

        this.displayPlayerTiles(this.playerOneTiles, this.playerTwoTiles);
    }

    /**
     * Takes in two arrays, one representing the tiles to display, displayTiles
     * and one representing the tiles to hide, hideTiles. Graphically displays
     * the displayTiles and hides the hideTiles.
     */
    private void displayPlayerTiles(Tile[] displayTiles, Tile[] hideTiles) {
        for (int i = 0; i < Constants.STARTING_PLAYER_TILES; i++) {

            // send all the other player's tiles to the back of gamePane
            if (hideTiles[i] != null) {
                hideTiles[i].sendBackOfPane();
            }

            // display the current player's tiles graphically
            if (displayTiles[i] != null) {
                Tile tileOne = displayTiles[i];
                tileOne.displayTileInGame(this.gamePane);
                tileOne.setTileLocation(tileOne.getInitialX(), 0);
            }
        }
    }

    /**
     * Helper method in charge of generating all six buttons graphically
     * and handling what should happen when each one is clicked. Every
     * button except the quit button only reacts to being clicked if the
     * game is not over.
     */
    private void setUpButtons(){
        Button quitButton = this.makeButtonTemplate("Quit");
        quitButton.setOnMouseClicked((MouseEvent e) -> {
            System.exit(0);
            e.consume();
        });

        Button revertButton = this.makeButtonTemplate("Revert Tiles to Rack");
        revertButton.setOnMouseClicked((MouseEvent e) -> {
            this.reactToRevert();
            e.consume();
        });

        Button playButton = this.makeButtonTemplate("Play Move!");
        playButton.setOnMouseClicked((MouseEvent e) -> {
            this.respondToPlay();
            e.consume();
        });

        Button shuffleButton = this.makeButtonTemplate("Shuffle");
        shuffleButton.setOnMouseClicked((MouseEvent e) -> {
            if (this.playerOneTurn) {
                this.reactToShuffle(this.playerOneTiles);
            }

            else {
                this.reactToShuffle(this.playerTwoTiles);
            }

            e.consume();
        });

        Button passButton = this.makeButtonTemplate("Pass");
        passButton.setOnMouseClicked((MouseEvent e) -> {
            this.reactToPass();
            e.consume();
        });

        Button resignButton = this.makeButtonTemplate("Resign");
        resignButton.setOnMouseClicked((MouseEvent e) -> {
            this.reactToResign();
            e.consume();
        });
    }

    /**
     * Helper method that takes in a String representing the title and
     * function of a button, for example, "Quit," which instantiates
     * the button, sets its color and font, and adds that button to
     * the buttonPane so it will appear graphically.
     */
    private Button makeButtonTemplate(String text){
        Button button = new Button(text);
        button.setStyle(Constants.BUTTON_PROPERTIES);
        this.buttonPane.getChildren().add(button);
        button.setFocusTraversable(false);
        return button;
    }

    /**
     * Reverts all newly placed tiles back to the tile rack in their initial
     * positions and clears the newly placed tiles from being stored in
     * the placedTiles memory.
     */
    private void reactToRevert(){
        if (!this.gameOver) {
            // send message backwards in case there is message displaying
            this.messageBox.toBack();

            // visually revert tiles
            // do it conditionally depending on whose turn it is
            if (this.playerOneTurn) {
                this.displayPlayerTiles(this.playerOneTiles, this.playerTwoTiles);
            }

            else {
                this.displayPlayerTiles(this.playerTwoTiles, this.playerOneTiles);
            }

            for (Tile tile : this.placedTiles) {
                if (tile.getSquareBeneath() != null) {
                    tile.getSquareBeneath().setSquareOccupied(false);
                }
            }

            // clear placedTiles
            this.placedTiles.clear();
        }
    }

    /**
     * Takes in a KeyEvent and only proceeds if the key pressed
     * is 'enter,' in which case a move is played and checked
     * for validity, just as if the play button were clicked.
     */
    private void handleKeyPress(KeyEvent e) {
        KeyCode keyPressed = e.getCode();
        if (keyPressed == KeyCode.ENTER){
            this.respondToPlay();
        }
        e.consume();
    }

    /**
     * If one or more tiles are played, then they are checked for validity in terms
     * of placement on the board, as well as the dictionary validity of the word
     * created by those tiles. If both are valid, then conditions are checked
     * to see whether the game is over or not, in essence, whether there
     * are no tiles left in the tile bag and whether a player has played all
     * of their tiles. If both conditions are true, then the game is over and
     * the user is alerted which player has won. If the game is not over, then
     * turns are switched, meaning the other player's tiles will appear on the
     * tile rack.
     */
    private void respondToPlay() {
        // while the game is not over
        if (!this.gameOver) {
            // first check if any tiles were actually placed
            if (this.placedTiles.size() > 0) {
                // get the ref to validate word. will return true if the move was a valid move, false otherwise
                if (this.referee.reactToPlay()) {
                    if (this.playerOneTurn) {
                        //obtain score of the move and add it to corresponding player's score
                        this.playerOneScore += this.referee.getScore();
                        this.playerOneScoreBox.setText(Constants.PLAYER_ONE_SCORE + "\n" + this.playerOneScore);
                    }

                    else {
                        this.playerTwoScore += this.referee.getScore();
                        this.playerTwoScoreBox.setText(Constants.PLAYER_TWO_SCORE + "\n" + this.playerTwoScore);
                    }

                    if (this.checkGameOver()) {
                        this.endGame();
                    }

                    // if the game is not over
                    else {
                        //replenish current player tiles
                        this.replenishTiles();

                        // clear the current player's newly placed tiles
                        this.placedTiles.clear();

                        this.switchTurn();
                    }
                }

                else {
                    // display invalid move
                    this.userMessage.setText(Constants.INVALID_MOVE_MESSAGE);
                    this.messageBox.toFront();
                }
            }

            // if play button pressed without there being any tiles then alert user and don't proceed
            else {
                this.userMessage.setText(Constants.NO_PLACED_TILES_MESSAGE);
                this.messageBox.toFront();
            }
        }
    }

    /**
     * Takes in an array of Tile objects and shuffles them by changing their
     * position randomly on the tileBoard both graphically on logically using the
     * Math.random function.
     */
    private void reactToShuffle(Tile[] playerTiles) {
        if (!this.gameOver) {
            // create local var initialXCoords to represent all initial xCoords of tiles on rack
            ArrayList<Double> initialXCoords = new ArrayList<>();

            // only shuffle the player tiles that are on the tile rack, i.e. have a y pos of zero
            for (Tile tile : playerTiles) {

                if (tile != null && tile.getSquareY() == 0) {
                    initialXCoords.add(tile.getInitialX());
                }
            }

            for (Tile tile : playerTiles) {
                if (tile != null && tile.getSquareY() == 0) {
                    // generate random number between 0 and size of initialxCoords
                    int index = (int) (Math.random() * (initialXCoords.size()));

                    // use it to obtain a new initialxCoord
                    double newInitialX = initialXCoords.get(index);

                    // assign newInitialX to each tile graphically
                    tile.setTileLocation(newInitialX, 0);

                    // and logically
                    tile.setInitialX(newInitialX);

                    // delete that index from initialXCoords
                    initialXCoords.remove(index);
                }
            }
        }
    }

    /**
     * Passes a player's turn by immediately displaying the other player's tiles
     * and alerting the user that it is the other player's turn. Reverts the board
     * squares below any newly placed tile prior to passing the turn to unoccupied.
     */
    private void reactToPass() {
        if (!this.gameOver) {
            // set all the positions of the gamesquares below placedtiles back to unoccupied
            for (Tile tile : this.placedTiles) {
                if (tile.getSquareBeneath() != null) {
                    tile.getSquareBeneath().setSquareOccupied(false);
                }
            }
            // clear any placed tiles from memory
            this.placedTiles.clear();

            // switch turns
            this.playerOneTurn = !this.playerOneTurn;

            if (this.playerOneTurn) {
                this.displayPlayerTiles(this.playerOneTiles, this.playerTwoTiles);
            }

            else {
                this.displayPlayerTiles(this.playerTwoTiles, this.playerOneTiles);
            }

            // display message indicating whose turn it is
            this.displayTurnMessage("");
        }
    }

    /**
     * Ends the game instantly by declaring that the corresponding player has resigned,
     * the game is over, and that the other player has won. At this point, the tiles
     * are no longer draggable via the mouse, and all buttons besides the quit button
     * are nonfunctional. Enter is also nonfunctional.
     */
    private void reactToResign() {
        if (!this.gameOver) {
            if (this.playerOneTurn) {
                this.userMessage.setText(Constants.RESIGN_MESSAGE_PLAYER_ONE +
                        "\n" + Constants.WINNING_PLAYER_DECLAR_TWO);
            }

            else {
                this.userMessage.setText(Constants.RESIGN_MESSAGE_PLAYER_TWO +
                        "\n" + Constants.WINNING_PLAYER_DECLAR_ONE);
            }

            this.messageBox.toFront();
            this.makeTilesUnclickable();
            // make enter not work
            this.gamePane.setOnKeyPressed(null);
            this.gameOver = true;
        }
    }

    /**
     * Ends the game by alerting the user of the game being over and
     * revealing which player has won based off points accrued. All
     * buttons except the quit button become nonfunctional and all
     * tiles are made non-draggable. Enter is also nonfunctional.
     */
    private void endGame(){
        this.gameOver = true;
        // display game over
        if (this.playerOneScore > this.playerTwoScore) {
            this.userMessage.setText(Constants.GAME_OVER_MESSAGE + "\n" + Constants.WINNING_PLAYER_DECLAR_ONE);
        }

        else if (this.playerTwoScore > this.playerOneScore) {
            this.userMessage.setText(Constants.GAME_OVER_MESSAGE + "\n" + Constants.WINNING_PLAYER_DECLAR_TWO);
        }

        else {
            this.userMessage.setText(Constants.GAME_OVER_MESSAGE + "\n" + Constants.TIE_MESSAGE);
        }

        this.messageBox.toFront();
        // MAKE ALL TILES AND BUTTONS excluding quit button UNRESPONSIVE
        this.makeTilesUnclickable();
        // make enter not work
        this.gamePane.setOnKeyPressed(null);
    }

    /**
     * Switches turns by alerting the user that it is the other player's turn
     * and displaying the other player's tiles
     */
    private void switchTurn(){
        // switch turns
        this.playerOneTurn = !this.playerOneTurn;

        if (this.playerOneTurn) {
            this.displayPlayerTiles(this.playerOneTiles, this.playerTwoTiles);
        }

        else {
            this.displayPlayerTiles(this.playerTwoTiles, this.playerOneTiles);
        }

        // display message indicating valid move and whose turn it is
        this.displayTurnMessage(Constants.VALID_MOVE_MESSAGE + "\n");
    }

    /**
     * Replenishes tiles of a player by the amount that they successfully
     * played, as long as there are tiles left in the tileBag. If the tile
     * bag becomes empty, no more tiles are replenished. The newly drawn
     * tiles are assigned the same initial position on the tile rack as
     * the tile that they replace, so they can appear there when displayed
     * graphically.
     */
    private void replenishTiles() {
        for (Tile placedTile : this.placedTiles) {
            for (int i = 0; i < Constants.STARTING_PLAYER_TILES; i++) {
                if (this.playerOneTurn) {
                    if (this.playerOneTiles[i] != null && this.playerOneTiles[i].getInitialX() ==
                            placedTile.getInitialX()) {
                        if (this.tileBag.size() > 0) {
                            Tile removed = this.tileBag.remove((int) (Math.random() * (this.tileBag.size())));
                            removed.setInitialX(this.playerOneTiles[i].getInitialX());
                            this.playerOneTiles[i] = removed;
                        }
                        // tile bag is empty
                        else {
                            this.playerOneTiles[i] = null;
                        }
                        break;
                    }
                }
                else {
                    if (this.playerTwoTiles[i] != null && this.playerTwoTiles[i].getInitialX()
                            == placedTile.getInitialX()) {
                        if (this.tileBag.size() > 0) {
                            Tile removed = this.tileBag.remove((int) (Math.random() * (this.tileBag.size())));
                            removed.setInitialX(this.playerTwoTiles[i].getInitialX());
                            this.playerTwoTiles[i] = removed;
                        }
                        else {
                            this.playerTwoTiles[i] = null;
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Takes in a String, which represents an additional message added to the
     * message alerting the user of which player's turn it is. If it is
     * player one's turn, then the message displayed will state that, and if it is
     * player two's turn, the message will state that.
     */
    private void displayTurnMessage(String string){
        if (this.playerOneTurn){
            this.userMessage.setText(string + Constants.PLAYER_ONE_MESSAGE);
        }
        else {
            this.userMessage.setText(string + Constants.PLAYER_TWO_MESSAGE);
        }
        this.messageBox.toFront();
    }

    /**
     * Method to check whether the game is over by checking the size of the tileBag
     * first and seeing whether it is empty. If the tile bag is not empty, false is
     * automatically returned since the game is still ongoing. If it's empty and a
     * player has played all the tiles on their rack, then the game is over and true is returned.
     * If the player has not played all their tiles, then false is returned.
     */
    private boolean checkGameOver() {
        if (this.tileBag.size() == 0) {
            if (this.playerOneTurn) {
                // first find how many tiles player one had
                int numTiles = this.checkNumOfPlayerTiles(this.playerOneTiles);
                // if numtiles is same as the numplaced tiles
                if (this.placedTiles.size() == numTiles) {
                    return true;
                }
            }
            else {
                int numTiles = this.checkNumOfPlayerTiles(this.playerTwoTiles);
                    if (this.placedTiles.size() == numTiles){
                        return true;
                    }
                }
            }
        return false;
    }

    /**
     * Takes in an array of Tile objects representing a player's tiles
     * and iterates through them, increasing a counter each time that
     * tile is not null, meaning that the tile exists on the tile rack.
     * Returns the counter integer, representing the number of tiles
     * on a player's tile rack.
     */
    private int checkNumOfPlayerTiles(Tile[] playerTiles){
        int counter = 0;
        for (Tile tile : playerTiles){
            if (tile != null){
                counter++;
            }
        }
        return counter;
    }

    /**
     * If it is player one's turn, then all their tiles are iterated through
     * and made unable to be clicked nor dragged. If it is the second player's turn,
     * then all their tiles are made unable to be clicked nor dragged.
     */
    private void makeTilesUnclickable(){
        if (this.playerOneTurn){
            for (Tile tile : this.playerOneTiles){
                if (tile != null) {
                    tile.unresponsiveToMouse();
                }
            }
        }
        else {
            for (Tile tile : this.playerTwoTiles){
                if (tile != null) {
                    tile.unresponsiveToMouse();
                }
            }
        }
    }
}
