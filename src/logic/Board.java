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

    public WinState checkWin() {
        int tilesLeft = 0;
        for (int m = 0; m < getM(); m++) {
        //for (int n = 0; n <= getN() - 2; n++) {
        //old code
            //for (int n = 0; n <= getN() -2; n++) {
            for (int n = 0; n < getN() ; n++) {

                int checkPlayer = board[m][n];
                if (checkPlayer != 0) {
                    boolean win = false;
                    // horizontal
                    // checks for a horizontal win on the game board.
                    // It checks whether there are k consecutive pieces
                    // belonging to the same player in a row starting at the position board[m][n].
                    if ((n + k <= getN())) {
                        win = true;
                        for (int i = 0; i < getK(); i++) {
                            if (checkPlayer != board[m][n + i]) {
                                win = false;
                                break;
                            }
                        }
                    }
                    // vertikal
                    // dieser Code block überprüft, ob es einen vertikalen Gewinn auf dem Spielbrett gibt.
                    // Es prüft mit einer Schleife durch jedes Feld der Spalte, ob es k aufeinanderfolgende Felder gibt,
                    // die dem gleichen Spieler gehören wie das Feld in der Startposition.
                    // Wenn ja, wird win auf true gesetzt. Wenn win bereits true ist,
                    // bleibt es jedoch true, und die Methode gibt den entsprechenden Gewinnzustand zurück.
                    if (!win && m + k <= getM()) {
                        win = true;
                        for (int i = 0; i < getK(); i++) {
                            if (checkPlayer != board[m + i][n]) {
                                win = false;
                                break;
                            }
                        }
                    }

                    // Check for diagonal wins
                    // where there are at least k rows and k columns remaining
                    // to the right and below the starting position.
                    // It checks if the k pieces in the diagonal
                    // are either all empty or all belong to the same player.
                    if (!win && (m + k <= getM()) && (n + k <= getN())) {
                        win = true;
                        for (int i = 0; i < getK(); i++) {
                            if (checkPlayer != board[m + i][n + i]) {
                                win = false;
                                break;
                            }
                    //if (getM() < 3 && getN() < 3) {
                    // win = true;
                    //}
                        }
                    }
                    // Check for diagonal wins from left bottom to right top
                    // starting with at least k rows above the starting position
                    // and at least k columns remaining to the right of the starting position.
                    // checks if the k pieces all empty or all belong to the same player
                    if (!win && (m + k <= getM()) && (n - (k - 1) >= 0)) {
                        win = true;
                        for (int i = 0; i < getK(); i++) {
                            if (checkPlayer != board[m + i][n - i]) {
                                win = false;
                                break;
                            }
                        }
                    }

                    if (win) {
                        return WinState.values()[checkPlayer];
                    }
                } else {
                    tilesLeft++;
                }
            }
        }
        if (tilesLeft == 0)
        {
            return WinState.tie;
        }
        return WinState.none;
    }
}
