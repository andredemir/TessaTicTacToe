package logic;

public class Board {

    private int n, m, k;
    private int board[][];
    private Player player1, player2;
    private Player active;

    public Board(int m, int n, int k, Player p1, Player p2) {
        this.setN(n);
        this.setM(m);
        this.setK(k);
        player1 = p1;
        player2 = p2;
        board = new int[m][n];
        active = player1;
    }

    public void nextTurn() {
        active = (active == player1) ? player2 : player1;
    }

    public Player getActivePlayer() {
        return active;
    }

    public int get2d(int m, int n) {
        return board[m][n];
    }

    public int getSize() {
        return getM() * getN();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getPlayerNameInField(int m, int n) {
        int val = get2d(m, n);
        String retStr;
        switch (val) {
        case 1:
            retStr = player1.getName();
            break;
        case 2:
            retStr = player2.getName();
            break;
        default:
            retStr = "        ";
        }
        return String.format("%5s", retStr);
    }

    public Player getPlayer2d(int m, int n) {
        int pid = get2d(m, n);
        switch (pid) {
        case 1:
            return player1;
        case 2:
            return player2;
        default:
            return null;
        }
    }

    public void resetGame() {
        active = player1;
        board = new int[m][n];
    }
    //:TODO: check if the field is already taken
    public void setToken2d(int m, int n, Player p) {
        if (board[m][n] == 0) {
            board[m][n] = (player1 == p) ? 1 : 2;
        }
    }

    /**
     * Check whether a player has won the game.
     * @return The state of the game (WinState.none, WinState.tie, or WinState.player1/player2).
     */
    public WinState checkWin() {
        int tilesLeft = 0;
        for (int row = 0; row < getM(); row++) {
            for (int col = 0; col <= getN() - 2; col++) {
                int player = board[row][col];
                if (player != 0) {
                    boolean win = checkHorizontalWin(row, col, player) || checkVerticalWin(row, col, player) || checkDiagonalWin(row, col, player);
                    if (win) {
                        return WinState.values()[player];
                    }
                } else {
                    tilesLeft++;
                }
            }
        }
        if (checkTie()) {
            return WinState.tie;
        }
        return WinState.none;
    }

    /**
     * Check whether there is a horizontal win starting at the given position for the given player.
     * @param row The row to start checking from.
     * @param col The column to start checking from.
     * @param player The player to check for.
     * @return True if there is a win, false otherwise.
     */
    private boolean checkHorizontalWin(int row, int col, int player) {
        for (int i = 0; i < getK(); i++) {
            if (player != board[row][col + i] || board[row][col + i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether there is a vertical win starting at the given position for the given player.
     * @param row The row to start checking from.
     * @param col The column to start checking from.
     * @param player The player to check for.
     * @return True if there is a win, false otherwise.
     */
    private boolean checkVerticalWin(int row, int col, int player) {
        for (int i = 0; i < getK(); i++) {
            if (player != board[row + i][col] || board[row + i][col] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether there is a diagonal win starting at the given position for the given player.
     * @param row The row to start checking from.
     * @param col The column to start checking from.
     * @param player The player to check for.
     * @return True if there is a win, false otherwise.
     */
    private boolean checkDiagonalWin(int row, int col, int player) {
        // Check for diagonal win with slope of +1
        if (row + getK() <= getM() && col + getK() <= getN()) {
            for (int i = 0; i < getK(); i++) {
                if (player != board[row + i][col + i] || board[row + i][col + i] == 0) {
                    return false;
                }
            }
            return true;
        }
        // Check for diagonal win with slope of -1
        if (row + getK() <= getM() && col - getK() + 1 >= 0) {
            for (int i = 0; i < getK(); i++) {
                if (player != board[row + i][col - i] || board[row + i][col - i] == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /**
     * Prüft, ob das Spiel unentschieden ist.
     * @return True, wenn das Spiel unentschieden ist, false sonst.
     */
    public boolean checkTie() {
        for (int row = 0; row < getM(); row++) {
            for (int col = 0; col < getN(); col++) {
                if (board[row][col] == 0) {
                    // There is an empty cell, so the game is not a tie
                    return false;
                }
            }
        }
        // All cells are occupied, so the game is a tie
        return true;
    }
}
