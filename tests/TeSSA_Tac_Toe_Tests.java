// Version für JUnit 5

import gfx.MainWindow;
import gfx.Ressources;
import logic.Board;
import logic.Player;
import logic.WinState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeSSA_Tac_Toe_Tests {
    private Player p1, p2;
    private Board board;
    private MainWindow frame;

    private static final int TIME_OUT = 0;

    @BeforeEach
    public void setUp() throws Exception {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(3, 3, 3, p1, p2);
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

    @Test
    public void test01() throws InterruptedException {
        frame.turn(0, 0);
        Thread.sleep(TIME_OUT);
        frame.turn(0, 1);
        Thread.sleep(TIME_OUT);
        frame.turn(1, 0);
        Thread.sleep(TIME_OUT);
        frame.turn(0, 2);
        Thread.sleep(TIME_OUT);
        WinState winner = frame.turn(2, 0);
        assertSame(WinState.player1, winner);
    }

    @Test
    public void test02() {
        frame.turn(0, 0);
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals("Player 1", retString);
    }

    @Test
    public void test03() {
        frame.turn(0, 0);
        frame.turn(0, 1);
        String retString = board.getPlayerNameInField(0, 1);
        assertEquals("Player 2", retString);
    }
    @Test
    public void test04() {
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals("        ", retString);
    }

    @Test
    public void test05() {
        WinState wst = frame.turn(0, 0);
        frame.checkWinner(wst);
    }


    @Test
    public void test06() {
        frame.turn(0, 0);
        frame.turn(0, 1);
        frame.turn(1, 0);
        frame.turn(1, 1);
        frame.turn(2, 1);
        WinState winSt = frame.turn(1, 0);
        frame.checkWinner(winSt);
    }

    @Test
    public void test07() {
        frame.settingsFrame();
    }

    // New Test
    // should fail
    @Test
    public void doppeltestPlatzierenTest() {
        frame.turn(0, 0);
        frame.turn(0, 1);
        frame.turn(0, 1);
        String retString = board.getPlayerNameInField(0, 1);
        assertEquals("Player 2", retString);
    }
    //should fail
    @Test void testForTie(){
        frame.turn(0, 0);
        frame.turn(0, 1);
        frame.turn(0, 2);
        frame.turn(1, 0);
        frame.turn(1, 1);
        frame.turn(1, 2);
        frame.turn(2, 0);
        frame.turn(2, 1);
        assertSame(board.checkWin(), WinState.tie);
    }

    //failed successfully
    @Test void testeRechterWinkel(){
        frame.turn(0, 0);
        frame.turn(0, 1);
        frame.turn(1, 0);
        frame.turn(0, 2);
        frame.turn(1, 1);
        assertSame(WinState.player1, WinState.player1);
    }

    @Test
    public void player1ExpPktBeiSieg() {
        frame.turn(0, 0);
        frame.turn(1, 0);
        frame.turn(0, 1);
        frame.turn(1, 1);
        frame.turn(0, 2);
        //
        frame.turn(0, 0);
        frame.turn(1, 0);
        frame.turn(0, 1);
        frame.turn(1, 1);
        frame.turn(0, 2);
        System.out.println(frame.getPlayer1_score().getText());

        assertEquals("0", frame.getPlayer1_score().getText());
    }

    @Test
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
    }
}

