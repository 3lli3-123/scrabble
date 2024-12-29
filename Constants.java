package indy;

/**
 * Class containing all the constants needed for Scrabble.
 */
public class Constants {
    public static final int BOARD_ROW = 15;
    public static final int BOARD_COL = BOARD_ROW;
    public static final int BOARD_CENTER_ROW_COL = BOARD_ROW / 2;

    public static final double SQUARE_WIDTH = 50;
    public static final double SQUARE_MIDPOINT = SQUARE_WIDTH / 2;
    public static final double TILE_RACK_OFFSET = 4.5;

    public static final double SCENE_WIDTH = SQUARE_WIDTH * BOARD_COL + SQUARE_WIDTH;

    public static final double SCENE_HEIGHT = SQUARE_WIDTH * BOARD_COL + 2*SQUARE_WIDTH;

    public static final double BUTTON_PANE_HEIGHT = SQUARE_WIDTH;
    public static final double BOARDER_STROKE = 3.5;
    public static final double GAME_PANE_HEIGHT = SCENE_HEIGHT - BUTTON_PANE_HEIGHT;
    public static final String TITLE = "SCRABBLE";

    public static final String BUTTON_PANE_COLOR = "-fx-background-color: #FF0000";

    public static final String TILE_COLOR = "-fx-background-color: #DEB887";
    public static final String BUTTON_PROPERTIES = "-fx-font-family: 'Arial'; -fx-font-size: 15px;" +
            " -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #800000";

    public static final double FONT_SIZE = 50;
    public static final double LETTER_FONT_SIZE = 30;
    public static final double POINT_VAL_FONT_SIZE = 13;

    public static final double MESSAGE_Y = GAME_PANE_HEIGHT / 2;

    public static final String VALID_MOVE_MESSAGE = "Valid Move!";

    public static final String INVALID_MOVE_MESSAGE = "Oops! NOT a Valid Move! \n Click Revert Tiles to Rack";

    public static final String STARTING_PLAYER_MESSAGE = "Welcome to Scrabble! \n \n" +
            "Drag Tiles to Form a Word \n \n" + "Press Enter or Click the \n Play" +
            " Button to Play a Move \n \n  Press the Spacebar \n to Start the Game!";

    public static final String PLAYER_ONE_MESSAGE = "Player One's Turn!";

    public static final String PLAYER_TWO_MESSAGE = "Player Two's Turn!";

    public static final String PLAYER_ONE_SCORE = "  Player One Score is: ";

    public static final String PLAYER_TWO_SCORE = "Player Two Score is: ";

    public static final String GAME_OVER_MESSAGE = "GAME OVER!";

    public static final String WINNING_PLAYER_DECLAR_ONE = "Player One is the Winner!";

    public static final String WINNING_PLAYER_DECLAR_TWO = "Player Two is the Winner!";

    public static final String TIE_MESSAGE = "It's a Tie!";

    public static final String NO_PLACED_TILES_MESSAGE = "Oops! \n No Tiles Were Placed!";

    public static final String INVALID_BLANK_TILE_INPUT = "Invalid Input, Please Enter a Single Letter: ";

    public static final String RESIGN_MESSAGE_PLAYER_ONE = "Player One Resigned! \n" + GAME_OVER_MESSAGE;

    public static final String RESIGN_MESSAGE_PLAYER_TWO = "Player Two Resigned! \n" + GAME_OVER_MESSAGE;

    public static final String BLANK_TILE_INPUT = "Blank Tile Input";

    public static final String ENTER_LETTER = "Enter a Letter for the Blank Tile";

    public static final String LETTER = "Letter:";

    public static final String VALID_WORDS_FILEPATH = "src/indy/ScrabbleWords.txt";

    public static final String STAR_IMAGE = "indy/StarSquare.png";

    public static final String DOUBLE_LETTER_IMAGE = "indy/DoubleLetterSquare.png";

    public static final String DOUBLE_WORD_IMAGE = "indy/DoubleWordSquare.png";

    public static final String TRIPLE_LETTER_IMAGE = "indy/TripleLetterSquare.png";

    public static final String TRIPLE_WORD_IMAGE = "indy/TripleWordSquare.png";

    public static final double SCORE_SPACING = 7.5 * SQUARE_WIDTH;

    public static final double SCORE_FONT = 17.5;

    public static final double INNER_SHADOW = 3.0f;

    public static final int STARTING_PLAYER_TILES = 7;

    public static final int DOUBLE_FACTOR = 2;

    public static final int TRIPLE_FACTOR = 3;

    public static final int AEIOULNSTR_POINT_VAL = 1;
    public static final int DG_POINT_VAL = 2;
    public static final int BCMP_POINT_VAL = 3;
    public static final int FHVWY_POINT_VAL = 4;
    public static final int K_POINT_VAL = 5;
    public static final int JX_POINT_VAL = 8;
    public static final int QZ_POINT_VAL = 10;
    public static final int BLANK_POINT_VAL = 0;

    public static final int ZXQJK_FREQ = 1;
    public static final int YWVHFPMCB_FREQ = 2;
    public static final int G_FREQ = 3;
    public static final int DUSL_FREQ = 4;
    public static final int TRN_FREQ = 6;
    public static final int O_FREQ = 8;
    public static final int IA_FREQ = 9;
    public static final int E_FREQ = 12;

    public static final int THREE_COL_DLS = 3;
    public static final int THREE_ROW_DLS = 3;
    public static final int SIX_COL_DLS = 6;
    public static final int TWO_ROW_DLS = 2;
    public static final int TWO_COL_DLS = 2;
    public static final int SIX_ROW_DLS = 6;
    public static final int EIGHT_ROW_DLS = 8;
    public static final int EIGHT_COL_DLS = 8;
    public static final int TWELVE_ROW_DLS = 12;
    public static final int TWELVE_COL_DLS = 12;
    public static final int ELEVEN_COL_DLS = 11;
    public static final int ELEVEN_ROW_DLS = 11;


    public static final int FIVE_ROW_DWS = 5;
    public static final int NINE_ROW_DWS = 9;

    public static final int FIVE_ROW_TLS = 5;
    public static final int NINE_ROW_TLS = 9;
    public static final int THIRTEEN_ROW_TLS = 13;
    public static final int THIRTEEN_COL_TLS = 13;
    public static final int FIVE_COL_TLS = 5;
    public static final int NINE_COL_TLS = 9;

    public static final int BONUS = 50;
    public static final int MERGE_SORT_DIVISOR = 2;

    public static final int CENTER_COORD = STARTING_PLAYER_TILES;
    public static final double LOCATION_OFFSET = 0.5;
}
