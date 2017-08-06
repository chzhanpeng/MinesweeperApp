import java.util.ArrayList;
import java.util.Random;

// Solve the MineSweeper game
public class Solver
{
    protected MineSweeper game;
    protected boolean random;    // Determine whether solver selec first pick randomly

    public Solver(MineSweeper game, boolean random)
    {
        this.game = game;
        this.random = random;
    }
    public static void main(String[] args)
    {
        MineSweeper game = new MineSweeper(10, 10, "medium");
        Solver solver = new Solver(game, false);
        solver.solve();
    }

    // Method that solve the puzzle, first move always random
    // Iterate through the board and use foundAllAdjacentMines to find mines
    // and use findAdjacentSafeMines to find safe tiles until victory
    public boolean solve()
    {
        //System.out.println(game.toStringForDebugging());
        //System.out.println(game);
        if(this.random)
        {
            this.randomFirstMove();
        }
        else
        {
            this.firstMove(game.fmRow, game.fmCol);
        }
        // Keep searching until win
        this.gameLoop();
        return this.checkResult();
    }

    // Main game loop, continue until winning or lost
    protected void gameLoop()
    {
        while(!game.win() && !game.lose())
        {
            String prevRound = game.toString();
            this.search();
            if(prevRound.equals(game.toString()))
            {
                //System.out.println("No move can be taken without gussing.");
                break;
            }
            //System.out.println(game.toStringForDebugging());
            //System.out.println(game);
        }
    }

    // Reveal given tile as first move
    protected void firstMove(int row, int col)
    {
        //System.out.println(String.format("First Move: %d %d", row, col));
        game.reveal(row, col);
    }

    // Pick and reveal randomly as first move
    protected void randomFirstMove()
    {
        Random rd = new Random();
        int r,c;                             // First move
        r = rd.nextInt(game.height());
        c = rd.nextInt(game.width());
        //System.out.println(String.format("First Move: %d %d",r,c));
        game.reveal(r,c);
    }

    // Check result after game also print result
    protected boolean checkResult()
    {
        //System.out.println(game);
        //System.out.println(game.toStringForDebugging());
        if(game.win())
        {
            //System.out.println("Master, I win!");
            return true;
        }
        else if(game.lose())
        {
            //System.out.println("Sorry Master, I lost.");
            return false;
        }
        else
        {
            //System.out.println("ERROR!");
            return false;
        }
    }

    // Search through board to findMines and find safe tile
    // uses findMines to find mines and use foundAllAdjacentMines
    // to find safe tiles
    protected void search()
    {
        for(int r = 0; r < game.height(); r++)
        {
            for(int c = 0; c < game.width(); c++)
            {
                if(this.game.needCheck(r,c))
                {
                    if(foundAllAdjacentSafeTiles(r,c))
                    {
                        //this.game.skip(r,c);
                        this.flagAdjacentMines(r,c);
                    }
                    if(this.foundAllAdjacentMines(r,c))
                    {
                        //this.game.skip(r,c);
                        this.revealAdjacentSafeTiles(r,c);
                    }
                }
                //System.out.println(game);
            }
        }
        //System.out.println(game);
    }


    // Check if a tile has adjacent mines all flagged
    // Check if numSurroundingMines equals number of surrounding flagged tiles
    // Return true if all neighbor mines have been found
    // Ignore IndexOutOfBoundsException
    protected boolean foundAllAdjacentMines(int r, int c)
    {
        int count = 0;
        try{          // Up left
            if(game.isFlagged(r-1,c-1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Up
            if(game.isFlagged(r-1,c))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Up right
            if(game.isFlagged(r-1,c+1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Left
            if(game.isFlagged(r,c-1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Right
            if(game.isFlagged(r,c+1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Down left
            if(game.isFlagged(r+1,c-1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Down
            if(game.isFlagged(r+1,c))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{          // Down right
            if(game.isFlagged(r+1,c+1))
            {
                count++;
                if(count==game.numSurroundingMines(r,c)){
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        return false;
    }

    // Reveal all safe unflagged adjacent tiles
    // Use foundAllAdjacentMines to check if tile is safe to use this method
    protected void revealAdjacentSafeTiles(int r, int c)
    {
        try{                                      // Up left
            if(!game.isFlagged(r-1,c-1))
            {
                game.reveal(r-1,c-1);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                      // Up
            if(!game.isFlagged(r-1,c))
            {
                game.reveal(r-1,c);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                      // Up right
            if(!game.isFlagged(r-1,c+1))
            {
                game.reveal(r-1,c+1);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                      // Left
            if(!game.isFlagged(r,c-1))
            {
                game.reveal(r,c-1);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                      // Right
            if(!game.isFlagged(r,c+1))
            {
                game.reveal(r,c+1);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                       // Down left
            if(!game.isFlagged(r+1,c-1))
            {
                game.reveal(r+1,c-1);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                       // Down
            if(!game.isFlagged(r+1,c))
            {
                game.reveal(r+1,c);
            }
        }
        catch(IndexOutOfBoundsException e){}
        try{                                        // Down right
            if(!game.isFlagged(r+1,c+1))
            {
                game.reveal(r+1,c+1);
            }
        }
        catch(IndexOutOfBoundsException e){}
    }

    // Check if number of surrouding hidden tiles equals numSurroundingMines
    // If condition is met, that means all hidden tiles are mines,
    // Return those hidden tiles as a list
    // Ignore IndexOutOfBoundsException
    protected boolean foundAllAdjacentSafeTiles(int r, int c)
    {
        int count = 0;
        try{                                 // Up left
            if(game.isVisible(r-1,c-1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Up
            if(game.isVisible(r-1,c))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Up right
            if(game.isVisible(r-1,c+1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Left
            if(game.isVisible(r,c-1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Right
            if(game.isVisible(r,c+1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Down left
            if(game.isVisible(r+1,c-1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Down
            if(game.isVisible(r+1,c))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        try{                                 // Down right
            if(game.isVisible(r+1,c+1))
            {
                count++;
                if(count == 8-game.numSurroundingMines(r,c))
                {
                    return true;
                }
            }
        }
        catch(IndexOutOfBoundsException e){};
        return false;
    }

    // Flag all nonvisible as mines, use this after checking if a tile has
    // foundAllAdjacentMines
    protected void flagAdjacentMines(int r, int c)
    {
        try                                              // Up left
        {
            if(game.isVisible(r-1, c-1) == false)
            {
                if(!game.isFlagged(r-1,c-1))
                {
                    game.flag(r-1, c-1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                              // Up
        {
            if(game.isVisible(r-1, c) == false)
            {
                if(!game.isFlagged(r-1, c))
                {
                    game.flag(r-1, c);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                          // Up right
        {
            if(game.isVisible(r-1, c+1) == false)
            {
                if(!game.isFlagged(r-1, c+1))
                {
                    game.flag(r-1, c+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                          // Left
        {
            if(game.isVisible(r, c-1) == false)
            {
                if(!game.isFlagged(r, c-1))
                {
                    game.flag(r, c-1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                              // Right
        {
            if(game.isVisible(r, c+1) == false)
            {
                if(!game.isFlagged(r, c+1))
                {
                    game.flag(r, c+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                              // Down left
        {
            if(game.isVisible(r+1, c-1) == false)
            {
                if(!game.isFlagged(r+1, c-1))
                {
                    game.flag(r+1, c-1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                              // Down
        {
            if(game.isVisible(r+1, c) == false)
            {
                if(!game.isFlagged(r+1, c))
                {
                    game.flag(r+1, c);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
        try                                              // Down right
        {
            if(game.isVisible(r+1, c+1) == false)
            {
                if(!game.isFlagged(r+1, c+1))
                {
                    game.flag(r+1, c+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){}
    }
}
