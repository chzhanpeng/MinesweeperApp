public class Solver {

    // Search for a move. It'll only search tiles that are visible and tiles
    // that need check. For each tile, it will check if all adjacent mines
    // are flagged or all adjacent safe tiles are revealed.
    public static void search(Minesweeper game) {
        for(int r = 0; r < game.height(); r++) {
            for(int c = 0; c < game.width(); c++) {
                if(game.isVisible(r,c) && game.needCheck(r, c) &&
                 game.numSurroundingMines(r, c) != 0 ){
                    if(Solver.foundAllAdjacentSafeTiles(game, r, c)) {
                        System.out.printf("X: %d %d\n", r, c);
                        if(foundAllAdjacentMines(game, r, c)) {
                            game.skip(r, c);
                            return;
                        }
                        Solver.flagAdjacentMines(game, r , c);
                        return;
                    }
                    if(Solver.foundAllAdjacentMines(game, r, c)) {
                        System.out.printf("Y: %d %d\n", r, c);
                        if(foundAllAdjacentSafeTiles(game, r, c)) {
                            game.skip(r, c);
                            return;
                        }
                        Solver.revealAdjacentSafeTile(game, r, c);
                        return;
                    }
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

    // Reveal all adjacent safe tiles, use this after checking using
    // foundAllAdjacentMines
    private static void revealAdjacentSafeTile(Minesweeper game, int r, int c) {
        if(r != 0 && c != 0) {
            if(!game.isFlagged(r-1, c-1)) {
                if(!game.isVisible(r-1, c-1)) {
                    game.reveal(r-1, c-1);
                    return;
                }
            }
        }
        if(r != 0) {
            if(!game.isFlagged(r-1, c)) {
                if(!game.isVisible(r-1, c)) {
                    game.reveal(r-1, c);
                    return;
                }
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(!game.isFlagged(r-1, c+1)) {
                if(!game.isVisible(r-1, c+1)) {
                    game.reveal(r-1, c+1);
                    return;
                }
            }
        }
        if(c != 0) {
            if(!game.isFlagged(r, c-1)) {
                if(!game.isVisible(r, c-1)) {
                    game.reveal(r, c-1);
                    return;
                }
            }
        }
        if(c != game.width()-1) {
            if(!game.isFlagged(r, c+1)) {
                if(!game.isVisible(r, c+1)) {
                    game.reveal(r, c+1);
                    return;
                }
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(!game.isFlagged(r+1, c-1)) {
                if(!game.isVisible(r+1, c-1)) {
                    game.reveal(r+1, c-1);
                    return;
                }
            }
        }
        if(r != game.height()-1) {
            if(!game.isFlagged(r+1, c)) {
                if(!game.isVisible(r+1, c)) {
                    game.reveal(r+1, c);
                    return;
                }
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(!game.isFlagged(r+1, c+1)) {
                if(!game.isVisible(r+1, c+1)) {
                    game.reveal(r+1, c+1);
                    return;
                }
            }
        }
    }

    // Check if all adjacent safe tiles of a tile are visible
    private static boolean foundAllAdjacentSafeTiles(Minesweeper game, int r, int c) {
        int count = 0;
        if(r != 0 && c != 0) {
            if(game.isVisible(r-1, c-1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0) {
            if(game.isVisible(r-1, c)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(game.isVisible(r-1, c+1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != 0) {
            if(game.isVisible(r, c-1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(c != game.width()-1) {
            if(game.isVisible(r, c+1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(game.isVisible(r+1, c-1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1) {
            if(game.isVisible(r+1, c)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(game.isVisible(r+1, c+1)) {
                count++;
                if(count == game.numSurroundingTiles(r,c)-game.numSurroundingMines(r,c)) return true;
            }
        }
        return false;
    }

    // Flag all adjacent miness, use this after checking using
    // foundAllAdjacentSafeTiles
    private static void flagAdjacentMines(Minesweeper game, int r, int c) {
        if(r != 0 && c != 0) {
            if(game.isVisible(r-1, c-1) == false) {
                if(!game.isFlagged(r-1,c-1)) {
                    game.flag(r-1, c-1);
                    return;
                }
            }
        }
        if(r != 0) {
            if(game.isVisible(r-1, c) == false) {
                if(!game.isFlagged(r-1,c)) {
                    game.flag(r-1, c);
                    return;
                }
            }
        }
        if(r != 0 && c != game.width()-1) {
            if(game.isVisible(r-1, c+1) == false) {
                if(!game.isFlagged(r-1,c+1)) {
                    game.flag(r-1, c+1);
                    return;
                }
            }
        }
        if(c != 0) {
            if(game.isVisible(r, c-1) == false) {
                if(!game.isFlagged(r,c-1)) {
                    game.flag(r, c-1);
                    return;
                }
            }
        }
        if(c != game.width()-1) {
            if(game.isVisible(r, c+1) == false) {
                if(!game.isFlagged(r,c+1)) {
                    game.flag(r, c+1);
                    return;
                }
            }
        }
        if(r != game.height()-1 && c != 0) {
            if(game.isVisible(r+1, c-1) == false) {
                if(!game.isFlagged(r+1,c-1)) {
                    game.flag(r+1, c-1);
                    return;
                }
            }
        }
        if(r != game.height()-1) {
            if(game.isVisible(r+1, c) == false) {
                if(!game.isFlagged(r+1,c)) {
                    game.flag(r+1, c);
                    return;
                }
            }
        }
        if(r != game.height()-1 && c != game.width()-1) {
            if(game.isVisible(r+1, c+1) == false) {
                if(!game.isFlagged(r+1,c+1)) {
                    game.flag(r+1, c+1);
                    return;
                }
            }
        }
    }
}
