import java.lang.StringBuilder;

public class Minesweeper {
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Game board consists a matrix of tile object as mines,
    protected Tile[][] board;
    // Difficulty of the game
    protected String difficulty;
    // Total number of mine of a game
    protected int numMines;
    // Total visible tiles
    protected int numRevealed;
    // Total tiles with flag
    protected int numFlags;
    // Keep track of first move, this helps in generating mines
    protected int fmRow, fmCol;
    // Whether it's brand new game/ no move taken
    protected boolean fresh;
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Constructor, initialize fields and generate mines
    public Minesweeper(int width, int height, String difficulty) {
        this.board = new Tile[width][height];
        this.numRevealed = 0;
        this.numMines = 0;
        this.numFlags = 0;
        this.fresh = true;
        this.difficulty = difficulty;
        // Set number of mines based on game diffculty
        if(difficulty.equals("easy")) {
            this.numMines = width*height/6;
        } else if(difficulty.equals("medium")) {
            this.numMines = width*height/5;
        } else {
            this.numMines = width*height/4;
        }
        this.initBoard();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Initialize board, create tiles for each position on board
    public void initBoard() {
        for(int r = 0; r < this.height(); r++) {
            for(int c = 0; c < this.width(); c++) {
                this.board[r][c] = new Tile(r,c,false);
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return tile at position i,j
    public Tile getTile(int i, int j) {
        return this.board[i][j];
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Check if player wins
    // Player wins by flagging all mines or reveal all safe tiles
    public boolean win() {
        return
        this.numFlags == this.numMines ||
        this.height()*this.width() - this.numRevealed == this.numMines ||
        this.numFlags + this.numRevealed == this.height()*this.width();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return first move row index
    public int getFMRow() {
        return fmRow;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return first move column index
    public int getFMCol() {
        return fmCol;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return number of mines in the field
    public int getNumMines() {
        return numMines;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Check if all mines are flagged
    public boolean allFlagged() {
        return numMines == numFlags ? true : false;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Check if a tile is visible
    public boolean isVisible(int row, int col) {
        return this.board[row][col].visible;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Check if a tile has a flag
    public boolean isFlagged(int row, int col) {
        return this.board[row][col].isFlagged();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return number of surrounding mines
    public int numSurroundingMines(int row, int col) {
        return this.board[row][col].numSurroundingMines;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return number of surrounding tiles, border tiles have less
    // surrounding tiles
    public int numSurroundingTiles(int row, int col) {
        int num = 8;
        if(row == (height()-1) || row == 0) {
            num -= 3;
            if(col == 0 || col == (width()-1)) {
                num++;
            }
        }
        if(col == 0 || col == (width()-1)) {
            num -= 3;
        }
        return num;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return game width
    public int width() {
        return this.board[0].length;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return game height
    public int height() {
        return this.board.length;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Flag a tile and increment numFlags
    public void flag(int r, int c) {
        board[r][c].flag();
        numFlags++;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // deflag a tile and decrement numFlags
    public void deflag(int r, int c) {
        this.board[r][c].deflag();
        this.numFlags--;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return true if check is needed for a tile
    public boolean needCheck(int r, int c) {
        return getTile(r,c).needCheck();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Skip tile on future check
    public void skip(int r, int c) {
        getTile(r, c).skip();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return difficulty of the game
    public String gameDifficulty() {
        return difficulty;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Reveal a tile that player choose, check if the move is first move,
    // generate game after first move; check if the tile is a mine, game is
    // over if so, reveal its neighbors if a tile is blank tile
    public void reveal(int r, int c) {
        if(fresh) {
            fmRow = r;
            fmCol = c;
            fresh = false;
            Generator gnrt = new Generator(this);
            gnrt.generate();
            reveal(r, c);
        } else {
            if(board[r][c].isMine()) {
                revealAll();
            } else {
                board[r][c].reveal();
                if(board[r][c].getNumSurroundingMines() == 0) {
                    skip(r, c);
                    revealNeighbor(r,c);
                }
                numRevealed++;
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Reveal safe neighboring tiles, Recursively revealNeighbor for the
    // adjacent safe tiles, stop until adjcent tile that has more than 0
    // numSurroundingMines
    private void revealNeighbor(int r, int c) {
        if(r != 0 && c != 0) {
            if(board[r-1][c-1].isRevealable()) {
                reveal(r-1,c-1);
            }
        }
        if(r != 0) {
            if(board[r-1][c].isRevealable()) {
                reveal(r-1,c);
            }
        }
        if(r != 0 && c != width()-1) {
            if(board[r-1][c+1].isRevealable()) {
                reveal(r-1,c+1);
            }
        }
        if(c != 0) {
            if(board[r][c-1].isRevealable()) {
                reveal(r,c-1);
            }
        }
        if(c != width()-1) {
            if(board[r][c+1].isRevealable()) {
                reveal(r,c+1);
            }
        }
        if(r != height()-1 && c != 0) {
            if(board[r+1][c-1].isRevealable()) {
                reveal(r+1,c-1);
            }
        }
        if(r != height()-1) {
            if(board[r+1][c].isRevealable()) {
                reveal(r+1,c);
            }
        }
        if(r != height()-1 && c != width()-1) {
            if(board[r+1][c+1].isRevealable()) {
                reveal(r+1,c+1);
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Reveal all tiles and print game
    protected void revealAll() {
        for(int r = 0; r < this.height(); r++) {
            for(int c = 0; c < this.width(); c++) {
                getTile(r,c).reveal();
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Cover all tiles
    protected void coverAll() {
        for(int r = 0; r < this.height(); r++) {
            for(int c = 0; c < this.width(); c++) {
                this.board[r][c].visible = false;
            }
        }
        this.numRevealed = 0;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // deflag all tiles
    protected void deflagAll() {
        for(int r = 0; r < this.height(); r++) {
            for(int c = 0; c < this.width(); c++) {
                this.board[r][c].deflag();
            }
        }
        this.numFlags = 0;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // A cheat for testing purpose ONLY, draw board with all mines marked
    //    0 1 2 3
    //-------------
    // 0|   1 1 1 |
    // 1| 1 2 * 1 |
    // 2| 1 * 2 1 |
    // 3| 1 1 1   |
    //-------------
    protected void cheat() {
        StringBuilder repr = new StringBuilder("   ");
        for(int i = 0; i < this.width(); i++) {
            repr.append(String.format("%2d",i));
        }
        repr.append("\n");
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        repr.append("\n");
        for(int r = 0; r < this.height(); r++) {
            repr.append(String.format("%2d| ",r));
            for(int c = 0; c < this.width(); c++) {
                if(this.board[r][c].mine == true) {
                    repr.append("* ");
                } else if(this.board[r][c].numSurroundingMines == 0) {
                    repr.append("  ");
                } else {
                    repr.append(this.board[r][c].numSurroundingMines + " ");
                }
            }
            repr.append("|\n");
        }
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        System.out.println(repr.toString());
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // String representation of game board for player
    // '?' for a unrevealed tile
    // '*' for a revealed tile with mine
    // ' ' for a revealed empty tile
    // '!' for tile with flag
    // '1-8' represen number of mines in surrounding tiles
    //    0 1 2 3
    //-------------
    // 0| ? ? ? ? |
    // 1| ? ? ! ? |
    // 2| ? ? ? ? |
    // 3| ? ? ? ? |
    //-------------
    public String toString() {
        StringBuilder repr = new StringBuilder("   ");
        for(int i = 0; i < this.width(); i++) {
            repr.append(String.format("%2d",i));
        }
        repr.append("\n");
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        repr.append("\n");
        for(int r = 0; r < this.height(); r++) {
            repr.append(String.format("%2d| ",r));
            for(int c = 0; c < this.width(); c++) {
                if(this.board[r][c].isFlagged()) {
                    repr.append("! ");
                } else if(this.board[r][c].visible == false) {
                    repr.append("? ");
                } else if(this.board[r][c].mine == true) {
                    repr.append("* ");
                } else if(this.board[r][c].numSurroundingMines == 0) {
                    repr.append("  ");
                } else {
                    repr.append(this.board[r][c].numSurroundingMines + " ");
                }
            }
            repr.append("|\n");
        }
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        return repr.toString();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
