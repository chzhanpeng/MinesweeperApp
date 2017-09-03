public class Solver {

    // Search for a move and return coordinate as an array of size 2
    public static void search(Minesweeper game) {
        //int[] arr = new int[2];
        for(int r = 0; r < game.height(); r++) {
            for(int c = 0; c < game.width(); c++) {
                if(game.isVisible(r,c)){
                    if(Solver.foundAllAdjacentSafeTiles(game, r, c)) {
                        //System.out.printf("%d %d\n", r, c);
                        Solver.flagAdjacentMines(game, r , c);
                        //return;
                    }
                    if(Solver.foundAllAdjacentMines(game, r, c)) {
                        //System.out.printf("%d %d\n", r, c);
                        Solver.findAdjacentSafeTile(game, r, c);
                    }
                }
            }
        }
    }

    //
    private static void findAdjacentSafeTile(Minesweeper game, int r, int c) {
        if(r != 0 && c != 0) {
            if(!game.isFlagged(r-1, c-1)) {
                if(!game.isVisible(r-1, c-1)) {
                    game.reveal(r-1, c-1);
                }
            }
        }
        if(r != 0) {
            if(!game.isFlagged(r-1, c)) {
                if(!game.isVisible(r-1, c)) {
                    game.reveal(r-1, c);
                }
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(!game.isFlagged(r-1, c+1)) {
                if(!game.isVisible(r-1, c+1)) {
                    game.reveal(r-1, c+1);
                }
            }
        }
        if(c != 0) {
            if(!game.isFlagged(r, c-1)) {
                if(!game.isVisible(r, c-1)) {
                    game.reveal(r, c-1);
                }
            }
        }
        if(c != game.width()-1) {
            if(!game.isFlagged(r, c+1)) {
                if(!game.isVisible(r, c+1)) {
                    game.reveal(r, c+1);
                }
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(!game.isFlagged(r+1, c-1)) {
                if(!game.isVisible(r+1, c-1)) {
                    game.reveal(r+1, c-1);
                }
            }
        }
        if(r != game.height()-1) {
            if(!game.isFlagged(r+1, c)) {
                if(!game.isVisible(r+1, c)) {
                    game.reveal(r+1, c);
                }
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(!game.isFlagged(r+1, c+1)) {
                if(!game.isVisible(r+1, c+1)) {
                    game.reveal(r+1, c+1);
                }
            }
        }
    }
    // Check if all adjacent mines of a tile are flagged
    private static boolean foundAllAdjacentMines(Minesweeper game, int r, int c) {
        int count = 0;
        if(r != 0 && c != 0) {
            if(game.isFlagged(r-1, c-1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0) {
            if(game.isFlagged(r-1, c)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(game.isFlagged(r-1, c+1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != 0) {
            if(game.isFlagged(r, c-1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != game.width()-1) {
            if(game.isFlagged(r, c+1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(game.isFlagged(r+1, c-1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1) {
            if(game.isFlagged(r+1, c)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(game.isFlagged(r+1, c+1)) {
                count++;
                if(count == game.numSurroundingMines(r,c)) return true;
            }
        }
        return false;
    }

    // Check if all adjacent safe tiles of a tile are visible
    private static boolean foundAllAdjacentSafeTiles(Minesweeper game, int r, int c) {
        int count = 0;
        if(r != 0 && c != 0) {
            if(game.isVisible(r-1, c-1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0) {
            if(game.isVisible(r-1, c)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(game.isVisible(r-1, c+1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != 0) {
            if(game.isVisible(r, c-1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != game.width()-1) {
            if(game.isVisible(r, c+1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(game.isVisible(r+1, c-1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1) {
            if(game.isVisible(r+1, c)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(game.isVisible(r+1, c+1)) {
                count++;
                if(count == 8-game.numSurroundingMines(r,c)) return true;
            }
        }
        return false;
    }

    private static void flagAdjacentMines(Minesweeper game, int r, int c) {
        if(r != 0 && c != 0) {
            if(game.isVisible(r-1, c-1) == false)
            {
                if(!game.isFlagged(r-1,c-1))
                {
                    game.flag(r-1, c-1);
                }
            }
        }
        if(r != 0) {
            if(game.isVisible(r-1, c) == false)
            {
                if(!game.isFlagged(r-1,c))
                {
                    game.flag(r-1, c);
                }
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(game.isVisible(r-1, c+1) == false)
            {
                if(!game.isFlagged(r-1,c+1))
                {
                    game.flag(r-1, c+1);
                }
            }
        }
        if(c != 0) {
            if(game.isVisible(r, c-1) == false)
            {
                if(!game.isFlagged(r,c-1))
                {
                    game.flag(r, c-1);
                }
            }
        }
        if(c != game.width()-1) {
            if(game.isVisible(r, c+1) == false)
            {
                if(!game.isFlagged(r,c+1))
                {
                    game.flag(r, c+1);
                }
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(game.isVisible(r+1, c-1) == false)
            {
                if(!game.isFlagged(r+1,c-1))
                {
                    game.flag(r+1, c-1);
                }
            }
        }
        if(r != game.height()-1) {
            if(game.isVisible(r+1, c) == false)
            {
                if(!game.isFlagged(r+1,c))
                {
                    game.flag(r+1, c);
                }
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(game.isVisible(r+1, c+1) == false)
            {
                if(!game.isFlagged(r+1,c+1))
                {
                    game.flag(r+1, c+1);
                }
            }
        }
    }
}
