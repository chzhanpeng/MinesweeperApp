import java.lang.StringBuilder;

public class MineSweeper {

    // Game board consists a matrix of tile object as mines,
    protected Tile[][] board;
    // Difficulty of the game
    protected String difficulty;
    // Total number of mine of a game
    protected int numMines;
    // Total visible tiles
    protected int numVisible;
    // Total tiles with flag
    protected int numFlags;
    // Whether the game is over
    protected boolean gameOver;
    // Keep track of first move, this helps in generating mines
    protected int fmRow, fmCol;
    // Whether it's brand new game/ no move taken
    protected boolean fresh;

    // Constructor, initialize fields and generate mines
    public MineSweeper(int width, int height, String difficulty)
    {
        this.board = new Tile[width][height];
        this.numVisible = 0;
        this.numMines = 0;
        this.numFlags = 0;
        this.fresh = true;
        this.difficulty = difficulty;
        // Set number of mines based on game diffculty
        if(difficulty.equals("easy"))
        {
            this.numMines = width*height/6;
        }
        else if(difficulty.equals("medium"))
        {
            this.numMines = width*height/5;
        }
        else
        {
            this.numMines = width*height/4;
        }
        this.initBoard();
    }
    // Return tile at position i,j
    public Tile getTile(int i, int j) {
        return this.board[i][j];
    }

    // Check if player lost a game
    // Player loses by revealing a mine
    public boolean lose()
    {
        return this.gameOver;
    }

    // Check if player wins
    // Player wins by flagging all mines or reveal all safe tiles
    public boolean win()
    {
        return
        this.numFlags == this.numMines ||
        this.height()*this.width() - this.numVisible == this.numMines ||
        this.numFlags + this.numVisible == this.height()*this.width();

    }

    // Whether check is needed for this tile
    protected boolean needCheck(int row, int col)
    {
        return this.board[row][col].check;
    }

    // Set check to false, which mean solver can skip this tile while searching
    // for move
    protected void skip(int row, int col)
    {
        this.board[row][col].check = false;
    }

    // Check if all mines are flagged
    public boolean allFlagged()
    {
        for(int r=0; r<this.height(); r++)
        {
            for(int c=0; c<this.width(); c++)
            {
                if(this.board[r][c].mine == true)
                {
                    if(this.isFlagged(r,c))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Check if a tile is visible
    public boolean isVisible(int row, int col)
    {
        return this.board[row][col].visible;
    }

    // Check if a tile has a flag
    public boolean isFlagged(int row, int col)
    {
        return this.board[row][col].flag;
    }

    // Return number of surrounding mines
    public int numSurroundingMines(int row, int col)
    {
        return this.board[row][col].numSurroundingMines;
    }

    // Return game width
    public int width()
    {
        return this.board[0].length;
    }

    // Return game height
    public int height()
    {
        return this.board.length;
    }

    // Flag a tile and increment numFlags
    public void flag(int r, int c)
    {
        this.board[r][c].flag = true;
        this.numFlags++;
    }

    // Unflag a tile and decrement numFlags
    public void unflag(int r, int c)
    {
        this.board[r][c].flag = false;
        this.numFlags--;
    }

    // Return difficulty of the game
    public String gameDifficulty()
    {
        return difficulty;
    }

    // Initialize board, create tiles for each position on board
    public void initBoard()
    {
        for(int r = 0; r < this.height(); r++)
        {
            for(int c = 0; c < this.width(); c++)
            {
                this.board[r][c] = new Tile(r,c,false);
            }
        }
    }

    // Generate game after first move
    public void generate(int r, int c)
    {
        this.fresh = false;
        this.fmRow = r;
        this.fmCol = c;
        Generator gnrt = new Generator();
        gnrt.smartGenerate(this, this.numMines);
        //gnrt.smartGenerate(this, this.numMines);
        this.numVisible++;
        this.board[r][c].visible = true;
        // reveal adjacent tiles when it's blank tile
        if(this.board[r][c].numSurroundingMines == 0)
        {
            this.revealNeighbor(r,c);
        }
    }

    // Reveal a tile that player choose, check if the move is first move,
    // generate game after first move; check if the tile is a mine, game is
    // over if so, reveal its neighbors if a tile is blank tile
    public void reveal(int r, int c)
    {
        if(this.fresh)
        {
            this.generate(r, c);
        }
        else
        {
            if(this.board[r][c].mine == true) // Check if a tile is a mine
            {
                this.gameOver = true;
                this.revealAll();
            }
            else
            {
                if(this.board[r][c].visible == false)
                {
                    this.numVisible++;
                    this.board[r][c].visible = true;
                    // reveal adjacent tiles when it's blank tile
                    if(this.board[r][c].numSurroundingMines == 0)
                    {
                        //this.skip(r,c);
                        this.revealNeighbor(r,c);
                    }
                }
            }
        }
    }

    // Reveal safe neighboring tiles, Recursively revealNeighbor for the
    // adjacent safe tiles, stop until adjcent tile that has more than 0
    // numSurroundingMines
    protected void revealNeighbor(int r, int c)
    {
        try{                                     // Up left
            this.reveal(r-1,c-1);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Up
            this.reveal(r-1,c);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Up right
            this.reveal(r-1,c+1);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Left
            this.reveal(r,c-1);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Right
            this.reveal(r,c+1);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Down left
            this.reveal(r+1,c-1);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Down
            this.reveal(r+1,c);
        }
        catch(IndexOutOfBoundsException e) {};
        try{                                     // Down right
            this.reveal(r+1,c+1);
        }
        catch(IndexOutOfBoundsException e) {};
    }

    // Reveal all tiles and print game
    protected void revealAll()
    {
        for(int r = 0; r < this.height(); r++)
        {
            for(int c = 0; c < this.width(); c++)
            {
                this.board[r][c].visible = true;
            }
        }
        System.out.println(this);
    }

    // Cover all tiles
    protected void coverAll()
    {
        for(int r = 0; r < this.height(); r++)
        {
            for(int c = 0; c < this.width(); c++)
            {
                this.board[r][c].visible = false;
            }
        }
        this.numVisible = 0;
    }

    // Unflag all tiles
    protected void clearAllFlags()
    {
        for(int r = 0; r < this.height(); r++)
        {
            for(int c = 0; c < this.width(); c++)
            {
                this.board[r][c].flag = false;
            }
        }
        this.numFlags = 0;
    }

    // A cheat for testing purpose ONLY, draw board with all mines marked
    //    0 1 2 3
    //-------------
    // 0|   1 1 1 |
    // 1| 1 2 * 1 |
    // 2| 1 * 2 1 |
    // 3| 1 1 1   |
    //-------------
    protected void cheat()
    {
        StringBuilder repr = new StringBuilder("   ");
        for(int i = 0; i < this.width(); i++)
        {
            repr.append(String.format("%2d",i));
        }
        repr.append("\n");
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        repr.append("\n");
        for(int r = 0; r < this.height(); r++)
        {
            repr.append(String.format("%2d| ",r));
            for(int c = 0; c < this.width(); c++)
            {
                if(this.board[r][c].mine == true)
                {
                    repr.append("* ");
                }
                else if(this.board[r][c].numSurroundingMines == 0)
                {
                    repr.append("  ");
                }
                else
                {
                    repr.append(this.board[r][c].numSurroundingMines + " ");
                }
            }
            repr.append("|\n");
        }
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        System.out.println(repr.toString());
    }

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
    public String toString()
    {
        StringBuilder repr = new StringBuilder("   ");
        for(int i = 0; i < this.width(); i++)
        {
            repr.append(String.format("%2d",i));
        }
        repr.append("\n");
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        repr.append("\n");
        for(int r = 0; r < this.height(); r++)
        {
            repr.append(String.format("%2d| ",r));
            for(int c = 0; c < this.width(); c++)
            {
                if(this.board[r][c].flag == true)
                {
                    repr.append("! ");
                }
                else if(this.board[r][c].visible == false)
                {
                    repr.append("? ");
                }
                else if(this.board[r][c].mine == true)
                {
                    repr.append("* ");
                }
                else if(this.board[r][c].numSurroundingMines == 0)
                {
                    repr.append("  ");
                }
                else
                {
                    repr.append(this.board[r][c].numSurroundingMines + " ");
                }
            }
            repr.append("|\n");
        }
        repr.append(new String(new char[this.width()*2 +5]).replace("\0","-"));
        return repr.toString();
    }

    // Show game information such as number of mine, game difficulty etc.
    public String gameInfo()
    {
        return String.format("Number of Mines: %s\n",this.numMines) +
        String.format("Number of Flags: %s\n", this.numFlags) +
        String.format("Number of Visible: %s\n", this.numVisible);
    }

    // Return a preset game, modify as needed
    private static MineSweeper presetGame()
    {
        Generator gnrt = new Generator();
        MineSweeper game = new MineSweeper(6,6,"easy");
        for(int r = 0; r < game.height(); r++)
        {
            for(int c = 0; c < game.width(); c++)
            {
                game.board[r][c].mine = false;
                game.board[r][c].visible = false;
                game.board[r][c].numSurroundingMines = 0;
            }
        }
        // Modify this part to preset game
        game.board[1][1].mine = true;
        game.board[1][1].mine = true;
        game.board[1][1].mine = true;
        gnrt.countAdjacentMines(game);
        return game;
    }

}
