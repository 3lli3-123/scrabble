package indy;

import javafx.scene.layout.Pane;
import java.util.ArrayList;

/**
 * This class models the board in Scrabble, and it contains references to a
 * gameBoard representing squares on the board and a tileBoard representing
 * tiles that have been added to the board.
 */
public class Board {
    private GameSquare[][] gameBoard;
    private Tile[][] tileBoard;

    /**
     * The Board constructor takes in a Pane which is passed into the
     * method for generating the board squares in gameBoard.
     * It also instantiates two 2D arrays, one for the gameBoard
     * and one for the tileBoard. Finally, it calls the method to
     * generate all the board squares on the gameBoard.
     */
    public Board(Pane game) {
        this.gameBoard = new GameSquare[Constants.BOARD_ROW][Constants.BOARD_COL];
        this.tileBoard = new Tile[Constants.BOARD_ROW][Constants.BOARD_COL];
        this.generateBoardSquares(game);
    }

    /**
     * Helper method to fill the gameBoard with the squares on the board logically and graphically.
     * It takes in a game Pane, which the graphically generated board is added to.
     * This method also sets each square in the correct x and y coordinate based on its
     * gameBoard indices as well as adds the special board squares in the correct location
     */
    private void generateBoardSquares(Pane gamePane) {
        for (int row = 0; row < Constants.BOARD_ROW; row++) {
            for (int col = 0; col < Constants.BOARD_COL; col++) {
                // declare local var GS as GS for now
                GameSquare square = new GameSquare();

                // first add all the TWS
                if (row == 0 || row == Constants.BOARD_CENTER_ROW_COL || row == Constants.BOARD_ROW - 1) {
                    if (col == 0 || col == Constants.BOARD_CENTER_ROW_COL || col == Constants.BOARD_COL - 1) {
                        square = new TripleWordSquare();
                        // make an exception to this for center square, which is DWS
                        if (row == Constants.BOARD_CENTER_ROW_COL && col == Constants.BOARD_CENTER_ROW_COL) {
                            square = new DoubleWordSquare();
                            // add white star onto square
                            square.setImage(Constants.STAR_IMAGE);
                        }
                    }
                    // add DLS
                    else if (col == Constants.THREE_COL_DLS || col == Constants.ELEVEN_COL_DLS) {
                        square = new DoubleLetterSquare();
                    }
                }

                // add all special squares on both diagonals
                else if (row >= 1 && row == col || row + col == Constants.BOARD_COL - 1) {
                    if (row < Constants.FIVE_ROW_DWS || row > Constants.NINE_ROW_DWS
                            && row < Constants.BOARD_ROW-1) {
                        square = new DoubleWordSquare();
                    }

                    else if (row == Constants.FIVE_ROW_TLS || row == Constants.NINE_ROW_TLS) {
                        square = new TripleLetterSquare();
                    }

                    else if (row == Constants.SIX_ROW_DLS || row == Constants.EIGHT_ROW_DLS) {
                        square = new DoubleLetterSquare();
                    }
                }

                // finally add all special squares in boomerang formation on four sides of board
                // unfortunately must kinda brute force these since there is no numerical pattern

                // tackle remaining TLS
                else if ((row == 1 || row == Constants.THIRTEEN_ROW_TLS) && (col == Constants.FIVE_COL_TLS
                        || col == Constants.NINE_COL_TLS) ||
                        (row == Constants.FIVE_ROW_TLS || row == Constants.NINE_ROW_TLS) &&
                                (col == 1 || col == Constants.THIRTEEN_COL_TLS)) {
                    square = new TripleLetterSquare();
                }

                // tackle remaining DLS
                else if ((row == Constants.TWO_ROW_DLS || row == Constants.TWELVE_ROW_DLS) &&
                        (col == Constants.SIX_COL_DLS || col == Constants.EIGHT_COL_DLS) ||
                        (row == Constants.THREE_ROW_DLS || row == Constants.ELEVEN_ROW_DLS) &&
                        (col == 0 || col == Constants.BOARD_CENTER_ROW_COL || col == Constants.BOARD_COL - 1)
                        || (row == Constants.SIX_ROW_DLS || row == Constants.EIGHT_ROW_DLS) &&
                        (col == Constants.TWO_COL_DLS || col == Constants.TWELVE_COL_DLS)) {
                    square = new DoubleLetterSquare();
                }

                // add square graphically and in correct location
                square.addSquareToPane(gamePane);
                square.setSquareLoc((Constants.SQUARE_WIDTH*(Constants.LOCATION_OFFSET + col)),
                        (Constants.SQUARE_WIDTH*(1 + row)));

                // add square logically
                this.gameBoard[row][col] = square;
            }
        }
    }

    /**
     * Takes in a Tile object and drops it onto the square on the gameBoard that
     * the tile's center is in. Does not drop the tile onto a square that
     * contains a previously placed or newly placed tile. Returns true if tile
     * can be dropped and false if not.
     */
    public boolean dropTileToBoard(Tile tile) {
        for (int row = 0; row < Constants.BOARD_ROW; row++) {
            for (int col = 0; col < Constants.BOARD_COL; col++) {
                GameSquare square = this.gameBoard[row][col];
                double xMin = square.getSquareX();
                double xMax = xMin + Constants.SQUARE_WIDTH;

                double yMin = square.getSquareY();
                double yMax = yMin + Constants.SQUARE_WIDTH;

                if ((tile.getCenterX() >= xMin && tile.getCenterX() < xMax) && (tile.getCenterY() >= yMin
                        && tile.getCenterY() < yMax)){

                    // only do this if there is no tile on the
                    // tileBoard at that location whether that is a previously or newly placed tile
                    if (this.findTileAtRowCol(row, col) == null && !square.getOccupiedStatus()) {
                        tile.setTileLocation(xMin, yMin);
                        // set the board square's status to occupied
                        square.setSquareOccupied(true);
                        // set the square beneath the tile
                        tile.setSquareBeneath(square);

                        // indicate that the tile can drop
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Takes in an arraylist of Tile objects representing the tiles
     * that have been newly placed, which must also be added permanently
     * to the tileBoard so that they are no longer draggable. Loops through
     * each tile in arraylist and adds the tile to the tileBoard logically
     * as well as sets the onBoardStatus of the tile to true, meaning it
     * has been added to the tileBoard, and finally makes the tile
     * unresponsive to mouse events.
     */
    public void addTileToBoard(ArrayList<Tile> placedTiles){
        // loop through all placedTiles
        for (Tile tile : placedTiles){
            // first obtain game square below placed tiles
            GameSquare square = tile.getSquareBeneath();

            //convert its coords to row col
            int row = square.convertYtoRow(square.getSquareY());
            int col = square.convertXtoCol(square.getSquareX());

            // use row col to add tile to tileBoard in same spot as square
            this.tileBoard[row][col] = tile;

            // change onBoardStatus
            tile.setOnBoardStatus(true);

            // finally make tile unresponsive to mouse
            tile.unresponsiveToMouse();

        }
    }


    /**
     * Takes in two ints representing the row and col and locates the location
     * on the tileBoard with those row col coordinates. If there is a tile at that
     * location, meaning a tile has been added to the board there, that tile
     * is returned. If there is no tile at that location on the board, null
     * is returned.
     */
    public Tile findTileAtRowCol(int row, int col){
        // first check that parameters exist on gameboard
        if (row < 0 || col < 0 || row > Constants.BOARD_ROW-1 || col > Constants.BOARD_COL-1){
            return null;
        }

        Tile tile = this.tileBoard[row][col];

        if (tile != null){
            return tile;
        }

        return null;
    }
}