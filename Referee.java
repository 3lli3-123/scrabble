package indy;

import javafx.scene.layout.Pane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class models a referee and handles all the tasks related to the rules of
 * Scrabble, mainly validating a move and calculating the score of a move. It
 * contains references to a gamePane, Tile objects, and a gameBoard,
 */
public class Referee {
    HashSet<String> validWords;
    Pane gamePane;
    String wordPlayed;
    ArrayList<Tile> placedTiles;
    ArrayList<Tile> wordTiles;
    ArrayList<ArrayList<Tile>> createdWords;
    Board gameBoard;
    // describes whether current word being validated is a vertical word or not
    boolean isVerticalWord;
    // indicates that it is the first move
    boolean tileBoardIsEmpty;
    // reflects whether the current move connects to at least one previously placed tile on the board.
    boolean foundPrevPlacedTile;
    int moveScore;

    /**
     * The Referee constructor takes in an arraylist of Tile objects representing
     * the tiles that were newly placed by a player as well as the Pane representing
     * the game pane and a board object representing the Scrabble board. It stores all
     * three of these parameters as instance variables. Then it initializes instance
     * variables such as the wordplayed and the move score, as well as instantiates
     * createdWords, the arraylist of all tiles forming valid words, and wordTiles
     * an arraylist of tiles creating a word. Finally, it sets up the hashset of
     * all valid Scrabble words.
     */
    public Referee(ArrayList<Tile> placedOnes, Pane theGame, Board theBoard) {
        this.placedTiles = placedOnes;
        this.gamePane = theGame;
        this.gameBoard = theBoard;
        this.tileBoardIsEmpty = true;
        this.foundPrevPlacedTile = false;
        this.wordPlayed = "";
        this.createdWords = new ArrayList<>();
        this.wordTiles = new ArrayList<>();
        this.moveScore = 0;
        this.setUpValidWordCollection();
    }

    /**
     * Instantiates validWords, the hashset representing the collection
     * of all valid scrabble words by using a Scanner object to read
     * each line of the file containing all valid words and add the word
     * on each line to the hashset.
     */
    private void setUpValidWordCollection() {
        this.validWords = new HashSet<>();

        try {
            Scanner scanner = new Scanner(new File(Constants.VALID_WORDS_FILEPATH));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                this.validWords.add(word);
            }
            scanner.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to validate a move that returns true if the move is valid
     * and false if invalid. If a move is valid, the score of the move
     * is also calculated. Checks the following criteria for move validity
     * in the following order: if the move is the first one of the game,
     * checks if one of the tiles were placed on the center square, then
     * checks that all the tiles were placed in one line, either vertically
     * or horizontally. Then it checks the validity of each word that is
     * created, and if all the words are valid and at least one of the
     * words created contains all the letters from the newly placed tiles,
     * and at least one of the newly placed tiles is connected to a previously
     * placed tile, then the move is valid. If even one of these criteria
     * are not met, the move is invalid.
     */
    public boolean reactToPlay() {
        // reset any values set from checking previous moves
        this.resetVals();

        // if it's the first move of the game, check that one of the tiles were placed on
        // the center tile, otherwise, move not valid
        if (this.tileBoardIsEmpty) {
            if (!this.checkPlacedOnCenter()) {
                return false;
            }
        }

        if (!this.arrangeTilesPlayed()) {
            return false;
        }

        // determine all the words that are created, break out of method if false is returned
        if (!this.findWordsCreated()) {
            return false;
        }

        // check if one of the words in createdWords contains all the placed Tiles
        if (this.findPlacedTileWord() && (this.foundPrevPlacedTile || this.tileBoardIsEmpty)) {

            // change status of tileBoardIsEmpty to false now
            this.tileBoardIsEmpty = false;

            // calculate score
            this.findScore();

            // add the tiles to the board
            this.gameBoard.addTileToBoard(this.placedTiles);

            // return true, meaning the play was validated successfully
            return true;
        }

        // if the word is invalid, return false
        else {
            return false;
        }
    }

    /**
     * Resets values set while checking validity of previous
     * moves such as the String and Tiles subsisting the wordPlayed,
     * moveScore, and arraylist of all createdWords.
     */
    private void resetVals() {
        this.resetWordPlayed();
        this.moveScore = 0;
        this.createdWords.clear();
        this.foundPrevPlacedTile = false;
    }

    /**
     * Resets String representing the wordPlayed in a move to
     * an empty string, and clears the arraylist of tiles
     * representing the tiles subsisting a word.
     */
    private void resetWordPlayed(){
        this.wordPlayed = "";
        this.wordTiles.clear();
    }

    /**
     * Checks whether any of the newly placed tiles were
     * placed on the center square of the board with the
     * star. Returns true if so, and false if not.
     */
    private boolean checkPlacedOnCenter() {
        for (Tile placedTile : this.placedTiles) {
            int row = placedTile.convertYtoRow(placedTile.getSquareY());
            int col = placedTile.convertXtoCol(placedTile.getSquareX());

            if ((row == Constants.CENTER_COORD) && (col == Constants.CENTER_COORD)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Arranges newly placed tiles logically in the same order that they appear
     * on the board by first determining whether the tiles were placed
     * vertically or horizontally. If either case is true, then the order
     * of the placedTiles array is sorted to reflect the order in which they
     * appear on the board and true is returned indicating that the tiles were
     * indeed arranged. If neither case is true, it means that the tiles were
     * not placed in one line and false is returned meaning that the tiles were
     * not arranged.
     */
    private boolean arrangeTilesPlayed() {
        // create arraylists of the x and y coords
        ArrayList<Double> xCoords = new ArrayList<>();
        ArrayList<Double> yCoords = new ArrayList<>();

        for (Tile myTile : this.placedTiles) {
            // populate arraylists with x and y vals of tiles

            xCoords.add(myTile.getSquareX());
            yCoords.add(myTile.getSquareY());
        }

        if (this.checkForSameValArray(xCoords)) {
            this.mergeSort(yCoords);
            for (int i = 0; i < yCoords.size(); i++) {
                // iterate through placedTiles to find the tile that corresponds to each coord in coords
                for (int j = 0; j < this.placedTiles.size(); j++) {
                    if ((yCoords.get(i).doubleValue()) == (this.placedTiles.get(j).getSquareY())) {
                        // sort placedTiles
                        Tile old = this.placedTiles.get(i);
                        this.placedTiles.set(i, this.placedTiles.get(j));
                        this.placedTiles.set(j, old);
                    }
                }
            }
            return true;
        }

        else if (this.checkForSameValArray(yCoords)) {
            this.mergeSort(xCoords);
            for (int i = 0; i < xCoords.size(); i++) {
                // iterate through placedTiles to find the tile that corresponds to each coord in coords
                for (int j = 0; j < this.placedTiles.size(); j++) {
                    if ((xCoords.get(i).doubleValue()) == (this.placedTiles.get(j).getSquareX())) {
                        // sort placedTiles
                        Tile old = this.placedTiles.get(i);
                        this.placedTiles.set(i, this.placedTiles.get(j));
                        this.placedTiles.set(j, old);
                    }
                }
            }
            return true;
        }

        // if neither the xcoords nor ycoords are the same, then tiles are not in line
        else {
            return false;
        }
    }

    /**
     * Takes in an arraylist of doubles and returns true if every
     * value is the same, false if not.
     */
    private boolean checkForSameValArray(ArrayList<Double> coords) {
        Double benchmarkVal = coords.get(0);
        for (Double num : coords) {
            if (!num.equals(benchmarkVal)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that takes in an arraylist of doubles
     * and merge sorts them in ascending order.
     */
    private void mergeSort(ArrayList<Double> coords) {
        if (coords.size() > 1) {
            int mid = coords.size() / Constants.MERGE_SORT_DIVISOR;
            ArrayList<Double> left = new ArrayList<>(coords.subList(0, mid));
            ArrayList<Double> right = new ArrayList<>(coords.subList(mid, coords.size()));
            this.mergeSort(left);
            this.mergeSort(right);
            this.merge(coords, left, right);
        }
    }

    /**
     * Helper method for merge sort that takes in three arraylists of doubles
     * and merges the latter two into the first one to create a sorted arraylist
     */
    private void merge(ArrayList<Double> nums, ArrayList<Double> left, ArrayList<Double> right) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i) <= right.get(j)) {
                nums.set(k++, left.get(i++));
            } else {
                nums.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            nums.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            nums.set(k++, right.get(j++));
        }
    }

    /**
     * Finds all the words created in a move by starting from the first tile
     * of placedTiles (either the uppermost or leftmost tile) and
     * checking what word is formed by tiles vertically and horizontally
     * adjacent. If those words are valid dictionary words, they are added
     * to the createdWords arraylist. If the word created is more than one
     * letter and not a valid dictionary word, then false is returned,
     * indicating that the move is invalid. Otherwise, the same process is applied
     * to the next tile in placedTiles. At the end, createdWords should
     * contain all the words formed which are valid dictionary words and
     * return true, indicating all words created in move are valid dictionary words.
     */
    private boolean findWordsCreated() {
        for (Tile tile : this.placedTiles) {
            this.isVerticalWord = true;
            this.constructWordPlayed(tile);

            if (this.wordPlayed.length() > 1) {
                if (!this.checkWordCanBeAdded()) {
                    return false;
                }
            }

            this.isVerticalWord = false;
            this.constructWordPlayed(tile);
            if (this.wordPlayed.length() > 1) {
                if (!this.checkWordCanBeAdded()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Searches for a word among all words created that contains every
     * tile that was newly placed. If this word is found, method returns
     * true and if not, the method returns false. This word being found
     * guarantees that each newly placed tiles is adjacent to other tiles.
     */
    private boolean findPlacedTileWord() {
        int numSameTile = 0;
        for (ArrayList<Tile> word : this.createdWords) {
            for (Tile tile : word) {
                if (!tile.getOnBoardStatus()) {
                    for (Tile placedTile : this.placedTiles) {
                        if (tile.equals(placedTile)) {
                            numSameTile++;
                        }
                    }
                }
                // if you found all the placed tiles in a single word
                if (numSameTile == this.placedTiles.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method that takes in a Tile object to start from and check
     * whether there are any newly or previously placed tiles above
     * that tile, store their letters if so, and create a prefix string.
     * The same goes for tiles directly below the parameter tile, which
     * creates a suffix string. Then, the prefix, tile's letter, and suffix
     * strings are concatenated to construct a word that was created by a move.
     */
    private void constructWordPlayed(Tile tile){
        String prefix = this.checkPrecedingTiles(tile);
        String suffix = this.checkSubsequentTiles(tile);
        this.wordPlayed = prefix + tile.getLetter() + suffix;
    }

    /**
     * Takes in a Tile object and if isVerticalWord is true, checks
     * if there are any tiles directly above that tile that were either
     * newly placed or previously placed. As long as there continue to
     * be adjacent tiles upwards, their letters are stored in a String
     * prefix. If there are no tiles, then prefix is an empty string.
     * If isVerticalWord is false, then the same functions occur
     * but going leftwards of the tile passed in. Prefix is returned.
     */
    private String checkPrecedingTiles(Tile tile){
        this.resetWordPlayed();
        // declare an empty string prefix to represent the letters of the all the tiles
        // preceding the ones played if there are any
        String prefix = "";

        if (this.isVerticalWord){
            // find tile's row and col value and use them to find row col of square directly above
            int rowOfSquareAbove = tile.convertYtoRow(tile.getSquareY()) - 1;
            int col = tile.convertXtoCol(tile.getSquareX());

            Tile boardTile = this.gameBoard.findTileAtRowCol(rowOfSquareAbove, col);
            Tile placedTile = this.checkForPlacedTile(rowOfSquareAbove, col);

            while (boardTile != null || placedTile != null){
                if (boardTile!= null) {
                    // change status of foundPrevTile to true since we found connection to prev tile
                    this.foundPrevPlacedTile = true;
                    // add the tile to the arraylist
                    this.wordTiles.add(0, boardTile);
                    // get its letter and add it to the front of the prefix string
                    prefix = boardTile.getLetter() + prefix;
                }

                else {
                    this.wordTiles.add(0, placedTile);
                    prefix = placedTile.getLetter() + prefix;
                }

                // then set boardSquare to the next square up and check again
                rowOfSquareAbove--;
                boardTile = this.gameBoard.findTileAtRowCol(rowOfSquareAbove, col);
                placedTile = this.checkForPlacedTile(rowOfSquareAbove, col);
            }
        }

        // if it is a horizontal move then same row, different col
        else {
            int row = tile.convertYtoRow(tile.getSquareY());
            int colOfSquareLeft = tile.convertXtoCol(tile.getSquareX()) - 1;

            Tile boardTile = this.gameBoard.findTileAtRowCol(row, colOfSquareLeft);
            Tile placedTile = this.checkForPlacedTile(row, colOfSquareLeft);

            while (boardTile != null || placedTile != null) {

                if (boardTile != null){
                    this.foundPrevPlacedTile = true;
                    // add the tile to the arraylist
                    this.wordTiles.add(0, boardTile);
                    prefix = boardTile.getLetter() + prefix;
                }
                else {
                    this.wordTiles.add(0, placedTile);
                    prefix = placedTile.getLetter() + prefix;
                }
                colOfSquareLeft--;
                boardTile = this.gameBoard.findTileAtRowCol(row, colOfSquareLeft);
                placedTile = this.checkForPlacedTile(row, colOfSquareLeft);
            }
        }
        return prefix;
    }

    /**
     * Takes in a Tile object and if isVerticalWord is true, checks
     * if there are any tiles directly below that tile that were either
     * newly placed or previously placed. As long as there continue to
     * be adjacent tiles downwards, their letters are stored in a String
     * suffix. If there are no tiles, then suffix is an empty string.
     * If isVerticalWord is false, then the same functions occur
     * but going rightwards of the tile passed in. Suffix is returned.
     */
    private String checkSubsequentTiles(Tile tile){
        // add tile to wordTiles
        this.wordTiles.add(tile);

        String suffix = "";

        if (this.isVerticalWord){
            int rowOfSquareBelow = tile.convertYtoRow(tile.getSquareY()) + 1;
            int col = tile.convertXtoCol(tile.getSquareX());

            Tile boardTile = this.gameBoard.findTileAtRowCol(rowOfSquareBelow, col);
            Tile placedTile = this.checkForPlacedTile(rowOfSquareBelow, col);

            // while there exists a tile on the tileBoard or within placedTiles in the row below firstTile
            while (boardTile != null || placedTile!= null){

                // if the tile exists on the tileBoard, add its letter
                if(boardTile != null) {
                    this.foundPrevPlacedTile = true;
                    // add the tile to the arraylist
                    this.wordTiles.add(boardTile);
                    suffix = suffix + boardTile.getLetter();
                }
                // if a placedTile exists in the row below, add its letter
                if (placedTile != null) {
                    // add the tile to the arraylist
                    this.wordTiles.add(placedTile);
                    suffix = suffix + placedTile.getLetter();
                }

                rowOfSquareBelow++;
                boardTile = this.gameBoard.findTileAtRowCol(rowOfSquareBelow, col);
                placedTile = this.checkForPlacedTile(rowOfSquareBelow, col);
            }
        }

        else {
            int row = tile.convertYtoRow(tile.getSquareY());
            int colOfSquareRight = tile.convertXtoCol(tile.getSquareX()) + 1;

            Tile boardTile = this.gameBoard.findTileAtRowCol(row, colOfSquareRight);
            Tile placedTile = this.checkForPlacedTile(row, colOfSquareRight);

            while (boardTile != null || placedTile != null) {
                // if the tile exists on the tileBoard, add its letter
                if(boardTile!=null) {
                    this.foundPrevPlacedTile = true;
                    suffix = suffix + boardTile.getLetter();
                    // add the tile to the arraylist
                    this.wordTiles.add(boardTile);
                }
                // if a placedTile exists in the row below, add its letter
                else {
                    this.wordTiles.add(placedTile);
                    suffix = suffix + placedTile.getLetter();
                }

                colOfSquareRight++;
                boardTile = this.gameBoard.findTileAtRowCol(row, colOfSquareRight);
                placedTile = this.checkForPlacedTile(row, colOfSquareRight);
            }
        }
        return suffix;
    }

    /**
     * Checks whether the tiles of a word can be added to the arraylist of
     * createdWords by first checking that it is a valid word in the dictionary,
     * then checking that createdWords does not already have a word composed of
     * the same tiles. If both checks are passed, then a copy of the tiles are added to
     * createdWords and true is returned. Even if the latter check is not passed,
     * true is still returned because those tiles could be eligible to be added
     * to the arraylist since they make a valid dictionary word. If the word formed
     * by the tiles is not a valid dictionary word false is returned.
     */
    private boolean checkWordCanBeAdded(){
        if (this.checkValidWord()) {
            ArrayList<Tile> tiles = this.copyArrayList();
            if (!this.createdWords.contains(tiles)) {
                this.createdWords.add(tiles);
                return true;
            }
        }
        // return false if not valid word
        else {
            return false;
        }

        return true;
    }

    /**
     * Checks if the wordPlayed String, representing a single word
     * created in a move, is 'dictionary' valid by checking whether
     * the hashset of Scrabble validWords contains wordPlayed or not.
     * If it does contain it, that means the word is dictionary valid
     * and true is returned. If validWords does not contain the wordPlayed
     * that means that the word is invalid and false is returned.
     */
    private boolean checkValidWord(){
        if (this.validWords.contains(this.wordPlayed)){
            return true;
        }
        return false;
    }

    /**
     * Makes a copy of the wordTiles, an arraylist of Tile objects
     * representing the tiles of a single word that is created in a move.
     */
    private ArrayList<Tile> copyArrayList(){
        ArrayList<Tile> newList = new ArrayList<>();
        for (Tile tile : this.wordTiles){
            newList.add(tile);
        }
        return newList;
    }

    /**
     * Takes in two ints representing the row and col position of a tile, then
     * searches through placedTiles to see if there is a tile with that position.
     * Returns that tile if it exists, or returns null if no placed tile has that
     * position.
     */
    private Tile checkForPlacedTile(int row, int col){
        for (Tile tile : this.placedTiles){
            if(tile.convertYtoRow(tile.getSquareY()) == row && tile.convertXtoCol(tile.getSquareX()) == col){
                return tile;
            }
        }
        return null;
    }

    /**
     * Calculates the score of the move, moveScore, by finding the score of each word
     * played while taking the special board squares into account and summing them.
     * Adds 50 point bonus to moveScore if all seven tiles are successfully played.
     */
    private void findScore(){
        // wordTiles are the collection of tiles composing a word created in move

        for (ArrayList<Tile> wordTiles : this.createdWords){
            int wordScore = 0;
            int wordFactor = 1;
            for (Tile tile : wordTiles){
                GameSquare squareBelow = tile.getSquareBeneath();

                // only if the tile has not been added to the board yet, meaning it is a recently placed
                // tile, then get the tilefactor and word factor of squarebeneath
                if (!tile.getOnBoardStatus()) {
                    wordScore += tile.getTileValue() * squareBelow.getTileFactor();
                    wordFactor *= squareBelow.getWordFactor();
                }

                // if the tile has already been added to the board, then regardless of what square
                //was beneath it, the tile and word factors are reverted to initial values
                else {
                    wordScore += tile.getTileValue();
                }
            }
            wordScore *= wordFactor;

            //check if all seven tiles have been played, which means there should be a 50 point bonus
            if (this.placedTiles.size() == Constants.STARTING_PLAYER_TILES){
                wordScore += Constants.BONUS;
            }

            this.moveScore += wordScore;
        }
    }


    /**
     * Returns an int representing the total score of the move
     */
    public int getScore(){
        return this.moveScore;
    }
}
