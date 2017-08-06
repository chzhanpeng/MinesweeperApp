import java.util.Random;

// Class to generate mines
public class Generator{

    // Generate mines randomly
    // Randomly select random tile to be mine; if tile is already a mine,
    // select another tile, repeat until there are enough mines
    protected void randomGenerate(MineSweeper game, int numMines)
    {
        Random rd = new Random();
        int curNumMines = 0;
        // Keep adding mines until desired number of mines
        while(curNumMines < numMines)
        {
            // Randomly generate coordinate to place mine, regenerate if an
            // coordinate is a neighbor of first-move coordinate
            int r = rd.nextInt(game.height());
            int c = rd.nextInt(game.width());
            while(r < game.fmRow+2 && r > game.fmRow-2 &&
                  c < game.fmCol+2 && c > game.fmCol-2)
            {
                r = rd.nextInt(game.height());
                c = rd.nextInt(game.width());
            }
            if(game.board[r][c].mine == false)
            {
                game.board[r][c].mine = true;
                curNumMines++;
            }
        }
        this.countAdjacentMines(game);
    }

    // Generate guess-free game, use solver to check if game is solvable
    // each time a mine is added
    protected void smartGenerate(MineSweeper game, int numMines)
    {
        Random rd = new Random();
        Solver solver = new Solver(game, false);
        //System.out.println("Before: "+ game);
        int curNumMines = 0;
        while(curNumMines < numMines)
        {
            // Randomly generate coordinate to place mine, regenerate if an
            // coordinate is a neighbor of first-move coordinate
            int r = rd.nextInt(game.height());
            int c = rd.nextInt(game.width());
            while(r < game.fmRow+2 && r > game.fmRow-2 &&
               c < game.fmCol+2 && c > game.fmCol-2)
            {
                r = rd.nextInt(game.height());
                c = rd.nextInt(game.width());
            }
            if(game.board[r][c].mine == false)
            {
                game.board[r][c].mine = true;
                game.coverAll();
                this.resetNumSurroundingMines(game);
                this.countAdjacentMines(game);
                if(!solver.solve())
                {
                    game.board[r][c].mine = false;
                    continue;
                }
                System.out.println(game);
                game.coverAll();
                curNumMines++;
            }
        }
        game.clearAllFlags();
    }

    // Reset number of surrounding mines for each tiles on game board
    protected void resetNumSurroundingMines(MineSweeper game)
    {
        for(int r = 0; r < game.height(); r++)
        {
            for(int c = 0; c < game.width(); c++)
            {
                game.board[r][c].numSurroundingMines = 0;
            }
        }
    }

    // Count number of mines in adjacent tiles for each tile
    // This method iterate though the board, find tiles with mine
    // and increment numSurroundingMines of neighbor tiles
    protected void countAdjacentMines(MineSweeper game)
    {
        for(int r = 0; r < game.height(); r++)
        {
            for(int c = 0; c < game.width(); c++)
            {
                if(game.board[r][c].mine == true)
                {
                    try{ game.board[r-1][c-1].numSurroundingMines++; }
                    catch(IndexOutOfBoundsException e) {}; // Up left
                    try{ game.board[r-1][c].numSurroundingMines++;   }
                    catch(IndexOutOfBoundsException e) {}; // Up
                    try{ game.board[r-1][c+1].numSurroundingMines++; }
                    catch(IndexOutOfBoundsException e) {}; // Up right
                    try{ game.board[r][c-1].numSurroundingMines++;   }
                    catch(IndexOutOfBoundsException e) {}; // Left
                    try{ game.board[r][c+1].numSurroundingMines++;   }
                    catch(IndexOutOfBoundsException e) {}; // Right
                    try{ game.board[r+1][c-1].numSurroundingMines++; }
                    catch(IndexOutOfBoundsException e) {}; // Down left
                    try{ game.board[r+1][c].numSurroundingMines++;   }
                    catch(IndexOutOfBoundsException e) {}; // Down
                    try{ game.board[r+1][c+1].numSurroundingMines++; }
                    catch(IndexOutOfBoundsException e) {}; // Down right
                }
            }
        }
    }
}
