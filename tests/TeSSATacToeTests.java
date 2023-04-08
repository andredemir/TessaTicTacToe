// Version für JUnit 5

import gfx.MainWindow;
import gfx.Ressources;
import logic.Board;
import logic.Player;
import logic.WinState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeSSATacToeTests {
    private Player p1, p2;
    private Board board;
    private MainWindow frame;

    private static final int TIME_OUT = 0;

    @BeforeEach
    public void setUp() throws Exception {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(4, 4, 4, p1, p2);
        frame = new MainWindow(p1, p2, board);
        frame.setVisible(true);
        MainWindow.setDebugg(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        p1 = p2 = null;
        board = null;
        frame = null;
    }

    // für page object model
    public void zug(int reihe, int spalte){
        frame.turn(reihe, spalte);
    }

    public String getFieldEntry(int reihe, int spalte){
        return board.getPlayerNameInField(reihe,spalte);
    }

    // ------------------- OUR TESTS START -------------------
    @Test
    public void doppelPlatzierenTest() {
        //Arrange
        zug(0, 0);
        zug(0, 1);
        //Act
        zug(0, 1);
        String retString = board.getPlayerNameInField(0, 1);
        //Assert
        assertEquals("Player 2", retString);
    }

    // Fehler 2
    //failed successfully
    @Test void gewinnUberEckeTest(){
        //Arrange
        zug(0, 0);
        zug(0, 1);
        zug(1, 0);
        zug(0, 2);
        //Act
        zug(1, 1);
        //Assert
        assertSame(WinState.player1, board.checkWin());
    }

    // Fehler 3
    // Ein Test der testet ob der Spieler 1 Punkte bekommt, wenn er gewinnt
    //
    public String getPlayer1Score(){
        return frame.getPlayer1_score().getText();
    }
    public String getPlayer2Score(){
        return frame.getPlayer2_score().getText();
    }
    @Test
    public void player1ExpPktBeiSieg() {
        zug(0, 0);
        zug(1, 0);
        zug(0, 1);
        zug(1, 1);
        zug(0, 2);
        testCheckWinner(WinState.player1);
        //System.out.println(getPlayer1Score());
        assertEquals("1", Integer.valueOf(getPlayer1Score()));
    }

    // Fehler 3
    // Ein Test der testet ob der Spieler 1 Punkte bekommt, wenn er gewinnt
    @Test
    public void player2MinusPktBeiSieg() {
        zug(0, 0);
        zug(1, 0);
        zug(0, 1);
        zug(1, 1);
        zug(2, 2);
        zug(1, 2);
        testCheckWinner(WinState.player2);
        //System.out.println(Integer.valueOf(frame.getPlayer2_score().getText()));

        assertEquals(1, Integer.valueOf(getPlayer2Score()));
    }

    // Fehler 4.1: !
    @Test void gewinnDreiInEinerReiheMitLeerzeichen(){
        zug(0, 0); //x
        zug(2, 0); //o
        zug(1, 0); //x
        zug(0, 1); //o
        zug(3, 0); //x
        assertSame(WinState.player1, testCheckWinState());
    }

    // Fehler 4.2: i
    @Test void gewinnDreiInEinerReiheMitLeerzeicheni(){
        zug(0, 0); //x
        zug(1, 0); //o
        zug(2, 0); //x
        zug(0, 1); //o
        zug(3, 0); //x
        assertSame(WinState.player1, testCheckWinState());
    }

    // Fehler 5
    @Test void vFuehrtZumSieg(){
        zug(0, 0);
        zug(0, 1);
        zug(1, 1);
        zug(1, 2);
        zug(0, 2);
        assertEquals(testCheckWinState(), WinState.none);
    }

    @Test
    public void testTwentyMoves() {
        for (int i = 0; i < 3; i++) {
            zug(2, 1);
            zug(0, 2);
            zug(0, 1);
            zug(1, 2);
            zug(1, 1);
            frame.resetBoard();
        }
        zug(2, 1);
        zug(0, 2);
        zug(0, 1);
        zug(1, 2);
        //Act
        zug(1, 1);
        // Assert
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals(" ", retString);
    }
}

    /*
    1. rechts unten - /not done Christoph
    2. gewinn über ecke - check /done (André)
    3. exp Punkte minus Punkte - check aber nochmal drueber schauen /not done (André)
    4. i und ! Sieg - Chrissy
    5. wth Test von uns der passen könnte - Chrissy
    6. 20. Zug - carina 🖐
    7. v = Sieg - André done
    8. unentschieden, obwohl felder frei - Christoph
    9. backlash- formation kein Sieg, wenn es an die rechte Spalte grenzt - Christoph
    10. [4][0} nicht ausgewertet = kein Sieg, wenn benutzt wird - carina 🖐
    */



    // nicht im Dokument
    /*@Test
    public void testNextTurn() {
        Player p1 = new Player("Alice", Ressources.icon_x);
        Player p2 = new Player("Bob", Ressources.icon_o);
        Board board = new Board(3, 3, 3, p1, p2);

        // Initially, the active player should be player 1
        assertEquals(p1, board.getActivePlayer());

        // After calling nextTurn(), the active player should be player 2
        board.nextTurn();
        assertEquals(p2, board.getActivePlayer());

        // After calling nextTurn() again, the active player should be player 1 again
        board.nextTurn();
        assertEquals(p1, board.getActivePlayer());
    }


    @Test
    public void testSetToken2d() {
        Player p1 = new Player("Alice", Ressources.icon_x);
        Player p2 = new Player("Bob", Ressources.icon_o);
        Board board = new Board(3, 3, 3, p1, p2);

        // Initially, the board should be empty
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(0, board.get2d(i, j));
            }
        }

        // After setting a token, the board should reflect the change
        board.setToken2d(0, 0, p1);
        assertEquals(1, board.get2d(0, 0));

        // After setting another token, the board should reflect the change
        board.setToken2d(1, 1, p2);
        assertEquals(2, board.get2d(1, 1));
    }

    @Test
    public void testCheckWin() {
        Player p1 = new Player("Alice", Ressources.icon_x);
        Player p2 = new Player("Bob", Ressources.icon_o);
        Board board = new Board(3, 3, 3, p1, p2);

        // Initially, there should be no winner
        assertEquals(board.checkWin(), WinState.none);

        // After setting a row of tokens, player 1 should win
        board.setToken2d(0, 0, p1);
        board.setToken2d(0, 1, p1);
        board.setToken2d(0, 2, p1);
        assertEquals(WinState.player1, board.checkWin());

        // After resetting the game, there should be no winner again
        board.resetGame();
        assertEquals(board.checkWin(), WinState.none);

        // After setting a diagonal of tokens, player 2 should win
        board.setToken2d(0, 0, p2);
        board.setToken2d(1, 1, p2);
        board.setToken2d(2, 2, p2);
        assertEquals(WinState.player2, board.checkWin());
    }

    //nicht relevant
    @Test
    public void testMainWindowCreation() {
        Player p1 = new Player("Player 1", Ressources.icon_x);
        Player p2 = new Player("Player 2", Ressources.icon_o);
        Board board = new Board(4, 5, 3, p1, p2);
        MainWindow frame = new MainWindow(p1, p2, board);
        assertNotNull(frame);
    }

    @Test
    public void testDebugMode() {
        TeSSA_Tac_Toe.DEBUG = true;
        assertTrue(TeSSA_Tac_Toe.DEBUG);
        TeSSA_Tac_Toe.DEBUG = false;
        assertFalse(TeSSA_Tac_Toe.DEBUG);
    }

    @Test
    public void testMainMethod() {
        String[] args = {"1"};
        TeSSA_Tac_Toe.main(args);
    }*/

}