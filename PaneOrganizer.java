package indy;

import javafx.geometry.Pos;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This top-level graphic class is in charge of all things related to panes. Containing
 * references to a root and coverPage, It instantiates the root, as well as
 * the cover page that shows up at the start of the game and contains the
 * welcome message and playing instructions. It also adds a keyevent handler to respond
 * to a space bar being pressed, in which case the Scrabble game manifests.
 */
public class PaneOrganizer {
    private BorderPane root;
    private HBox coverPage;

    /**
     * This is the constructor for the PaneOrganizer, which instantiates the root
     * and coverpage. There is a method to handle the pressing of the space bar,
     * which instantiates the Scrabble game as well as the buttonPane that contains
     * all the buttons.
     */
    public PaneOrganizer(){
        this.root = new BorderPane();
        this.root.setFocusTraversable(false);

        this.coverPage = new HBox();
        this.setUpCoverPage();

        this.coverPage.setOnKeyPressed((KeyEvent e) -> this.reactToKeyPress(e));
    }

    /**
     * Helper method to fully set up the coverpage by adding
     * the text containing welcome message and game instructions
     * onto it, set colors, as well as add the coverpage to the
     * center of the root, enabling the coverpage and its text to
     * graphically appear.
     */
    private void setUpCoverPage(){
        Text startMessage = new Text(Constants.STARTING_PLAYER_MESSAGE);
        startMessage.setTextAlignment(TextAlignment.CENTER);
        startMessage.setFont(Font.font("Verdana", FontWeight.BOLD, Constants.FONT_SIZE));
        startMessage.setFill(Color.MAROON);

        InnerShadow shadow = new InnerShadow();
        shadow.setOffsetX(Constants.INNER_SHADOW);
        shadow.setOffsetY(Constants.INNER_SHADOW);
        startMessage.setEffect(shadow);

        this.coverPage.setFocusTraversable(true);
        this.coverPage.setAlignment(Pos.CENTER);
        this.coverPage.getChildren().add(startMessage);
        this.coverPage.setStyle(Constants.TILE_COLOR);
        this.root.setCenter(this.coverPage);
    }

    /**
     * Takes in a KeyEvent and only responds if the key pressed
     * is the space bar, in which case a game is instantiated and
     * the coverpage is supplanted by the game pane containing the
     * game board and tiles. A buttonPane meant to hold all the
     * buttons is also instantiated, allowing the addition of
     * buttons onto the game.
     */
    private void reactToKeyPress(KeyEvent e){
        KeyCode keyPressed = e.getCode();
        if (keyPressed == KeyCode.SPACE){

            // set focus traversable of coverPane to false
            this.coverPage.setFocusTraversable(false);

            Pane gamePane = new Pane();
            this.setUpGamePane(gamePane);

            HBox buttonPane = new HBox();
            this.setUpButtonPane(buttonPane);
            new Game(gamePane, buttonPane);
        }

        e.consume();
    }

    /**
     * Helper method to set the color of the game pane
     * as well as set it to the center of
     * the root, which will enable it to graphically
     * appear, along with the Scrabble board,
     * tiles, and buttons that it contains.
     */
    private void setUpGamePane(Pane gamePane){
        gamePane.setFocusTraversable(true);
        gamePane.setStyle(Constants.BUTTON_PANE_COLOR);
        this.root.setCenter(gamePane);
    }

    /**
     * A method to create the buttonPane HBox that contains all the
     * buttons in the game, then add that HBox to the bottom of the root pane,
     * making any buttons added to it graphically appear on the bottom of
     * the game window.
     */
    private void setUpButtonPane(HBox myButtonPane){
        myButtonPane.setFocusTraversable(false);
        myButtonPane.setPrefSize(Constants.SCENE_WIDTH, Constants.BUTTON_PANE_HEIGHT);
        myButtonPane.setStyle(Constants.BUTTON_PANE_COLOR);
        myButtonPane.setSpacing(Constants.SQUARE_WIDTH);
        myButtonPane.setAlignment(Pos.CENTER);
        this.root.setBottom(myButtonPane);
    }

    /**
     * An accessor method for the root BorderPane returning the root.
     */
    public BorderPane getRoot(){
        return this.root;
    }
}
