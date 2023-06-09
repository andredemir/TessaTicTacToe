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

    public void setToken2d(int m, int n, Player p) {
        if (board[m][n] == 0) {
            board[m][n] = (player1 == p) ? 1 : 2;
        }
    }

    public WinState checkWin() {
        int tilesLeft = 0;
        // Columns durchgehen
        for (int m = 0; m < getM(); m++) {
        //for (int n = 0; n <= getN() - 2; n++) {
        //old code
            //for (int n = 0; n <= getN() -2; n++) {

            // Rows durchgehen
            for (int n = 0; n < getN() ; n++) {
                // Überprüft, ob das Feld leer ist.
                int checkPlayer = board[m][n];
                // wenn nicht, dann wird überprüft, ob es einen Gewinner gibt.
                if (checkPlayer != 0) {
                    boolean win = false;
                    // Vertikal.
                    // sind noch K Spalten übrig um zu gewinnen?
                    if ((n + k <= getN())) {
                        win = true;
                        // iteriert durch die k Spalten, die überprüft werden müssen.
                        for (int i = 0; i < getK(); i++) {
                            //Wenn das erste Stück nicht dem Spieler gehört, der den Zug gemacht hat, ist es kein Gewinn.
                            if (checkPlayer != board[m][n + i]) {
                                win = false;
                                break;
                            }
                        }
                    }
                    // Horizontal.
                    // überprüft, ob es auf dem Spielbrett einen horizontalen Gewinn gibt.
                    // sind noch K spalten übrig um zu gewinnen?
                    if (!win && m + k <= getM()) {
                        win = true;
                        //Iteriere über die Länge der diagonalen Linie.
                        for (int i = 0; i < getK(); i++) {
                            // Wenn das erste Stück nicht dem Spieler gehört, der den Zug gemacht hat, ist es kein Gewinn.
                            if (checkPlayer != board[m + i][n]) {
                                win = false;
                                break;
                            }
                        }
                    }

                    // prüft diagonale Gewinne, die an einer Position auf dem Spielbrett beginnen,
                    // an der mindestens k Zeilen und k Spalten unter der Startposition verbleiben.
                    // Es wird überprüft, ob die k Stücke in der Diagonale entweder alle leer oder alle demselben Spieler gehören.
                    if (!win && (m + k <= getM()) && (n + k <= getN())) {
                        win = true;
                        //Iteriere über die Länge der diagonalen Linie.
                        for (int i = 0; i < getK(); i++) {
                            // Wenn das erste Stück nicht dem Spieler gehört, der den Zug gemacht hat, ist es kein Gewinn.
                            if (checkPlayer != board[m + i][n + i]) {
                                win = false;
                                break;
                            }
                    //  if (getM() < 3 && getN() < 3) {
                    //  win = true;
                    //  }
                        }
                    }

                    // überprüft auf diagonale Gewinne von links unten nach rechts oben,
                    // beginnend an einer Position auf dem Spielbrett,
                    // an der mindestens k Zeilen über der Startposition und mindestens k Spalten rechts von der Startposition verbleiben.
                    if (!win && (m + k <= getM()) && (n - (k - 1) >= 0)) {
                        win = true;
                        //Iteriere über die Länge der diagonalen Linie.
                        for (int i = 0; i < getK(); i++) {
                            //Wenn das erste Stück nicht dem Spieler gehört, der den Zug gemacht hat, ist es kein Gewinn.
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
        if (tilesLeft == 0){
            return WinState.tie;
        }
        return WinState.none;
    }
}
