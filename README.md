# Scrabble

## Overview
The main purpose of my program was to implement a version of the game
Scrabble, which is a game where the objective is to amass the greatest number of
points by placing tiles horizontally or vertically to form words. Each tile has
a point value, and some of the squares on the board add additional boosts to those
point values. The game is over when a player resigns, or when there are no more tiles
in the tile bag and a player has played all of their remaining tiles, and the winner
is the player with the greater number of points. In addition to a top level logic
class, Game, a top level graphical class PaneOrganizer, and a Constants class, my
program also features a GameSquare superclass that all four special square classes
(double letter, double word, triple letter, triple word) as well as the Tile class
are subclasses of. My BlankTile class is a subclass of the Tile class. Finally,
there is a Board class to model the board and a Referee class to handle move
validation and scoring. As for class associations, my Game class knows about many
of the other classes such as Referee, Tile, and Board because it is in charge of
the overarching logistics of Scrabble and needs access to many of those class's methods.
Similarly, the Referee class also knows about the same Board and Tile classes.
My Tile class knows about the GameSquare class because it stores the GameSquare
beneath each Tile object as an instance variable.

## Design Choices
I used inheritance for my Tile and the squares on the board because
I realized that a tile and a square on the board are the same size, same shape,
and have the same color outline, so I wanted to utilize the constructor of a
superclass GameSquare, to initialize all these properties, whereas if I used an
interface, I wouldn't have a constructor to use. Since a blank square is a Tile
and has all the capabilities of a Tile with some additional adjustments to functions
like being dropped onto the board, I decided to make it a subclass of Tile. As for
new data structures, I used a hashmap to populate the tile bag of 100 tiles in my
Game class's setUpTileBag method, using the key of the hashmap as the instance of a certain
tile with a specified letter and point value, and the value of the hashmap being
the frequency of that tile in the tilebag. Those frequency values were then used
as the iteration count in the for loop used to add each type of tile into the tile bag.
Then, within my Referee class's setUpHashset method, I used a hashset to represent the
collection of all valid Scrabble words, using a Scanner to add every valid word
into the hashset from a file. To check whether a word was valid or not, I just
used the contains method of the hashset, since that has a very small runtime.
The major algorithm in my game was to check for word validation in a move within
the Referee class and mostly the reactToPlay method of that class,
which worked by locating the 'first tile' of all the tiles placed, either the
topmost tile if they were placed vertically and the leftmost tile if they were
placed horizontally. Then, for every tile starting from the first tile placed,
the algorithm checks for adjacent tiles above and if they exist, stores their letters
in a String called prefix. Then it checks for adjacent tiles below and if they exist,
stores their letters as part of suffix. Then, prefix and suffix are concatenated
to form a word. If that word is more than one letter and valid, then the algorithm
proceeds to check for preceding and subsequent tiles that are horizontally adjacent,
find the prefix and suffix, form another word, then check the word for validity.
If that word is valid, then the algorithm proceeds to focusing on the next tile
in placedTiles and checking for vertically then horizontally adjacent tiles.
Another algorithm I had was to find the total score of a move, which found the
point value of every word created in the move by iterating through all the arraylists
of Tiles composing those words, and for each arraylist of Tile objects, declaring two
local variables: one representing the word score (the total point value of the word
created by the tiles in the current arraylist) and another representing the word factor
(the amount to multiply the whole word's value by.) For every tile in the arraylist,
the tilefactor (the total amount to multiply a tile's value by) of the GameSquare on
the board beneath it was obtained, then multiplied by the tile's point value to obtain
the true value of the tile, then that value was added to the wordscore local variable.
The wordFactor of the gamesquare beneath was also obtained, and that was multiplied by
the wordFactor local variable, to be used later. After all the tiles had been iterated
through, the wordscore should represent the score of the word when all the double and
triple letter squares are taken into account, but before the double or triple word squares
were taken into account. To take the latter special squares into account, the wordScore
is multiplied by the wordFactor, and we get the total score of a single word. That value
is then added to the moveScore instance variable in the Referee class, representing the
total score of a move. The same process repeats for any other words created, and ultimately
moveScore becomes the sum of all the wordScores calculated.

## Known Bugs
None.