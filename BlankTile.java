package indy;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This class models a blank tile and is a subclass of the Tile class. It overrides
 * some of Tile's methods because it needs to prompt the user for a letter
 * as well as have an appearance without a letter or a number.
 */
public class BlankTile extends Tile {

    /**
     * The constructor for BlankTile takes in a String representing the tile's letter
     * and an int representing the tile's point value and passes them to the superclass
     * constructor for them to be assigned to the corresponding instance variables.
     */
    public BlankTile(String letter, int value){
        super(letter, value);
    }

    /**
     * Takes in a mouse event, arraylist of tiles, and a board object,
     * and it calls the superclass's method to drop the tile onto a square and
     * lock it in, then partially overrides it to prompt the user to enter a letter
     * to display on the blank tile. It will keep prompting the user until a single
     * letter is entered.
     */
    @Override
    public void reactToRelease(MouseEvent e, ArrayList<Tile> placedTiles, Board board){
        super.reactToRelease(e, placedTiles, board);

        // only if the tile is dropped onto board
        if (this.getSquareY() > 0) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(Constants.BLANK_TILE_INPUT);
            dialog.setHeaderText(Constants.ENTER_LETTER);
            dialog.setContentText(Constants.LETTER);

            Optional<String> result;
            do {
                result = dialog.showAndWait();
                if (result.isPresent()) {
                    String input = result.get();
                    if (input.matches("[a-zA-Z]")){
                        // store valid letter in tile
                        this.setLetter(input);
                        break;
                    }
                    else {
                        dialog.setContentText(Constants.INVALID_BLANK_TILE_INPUT);
                    }
                }
            }
            while (result.isPresent());
        }
    }

    /**
     * Takes in two doubles which represent the desired x and y location of the tile.
     * Calls superclass method to set tile's location and partially overrides it to
     * clear whatever letter was previously on the tile before it was set.
     */
    @Override
    public void setTileLocation(double x, double y){
        super.setTileLocation(x, y);
        this.clearTileLetter();
    }
}
