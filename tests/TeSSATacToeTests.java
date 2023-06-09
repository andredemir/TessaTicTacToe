// Version für JUnit 5

import gfx.MainWindow;
import gfx.Ressources;
import logic.Board;
import logic.Player;
import logic.WinState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class TeSSATacToeTests {
    private Player p1, p2;
    private Board board;
    private MainWindow frame;
    private ClickOk click;

    private static final int TIME_OUT = 0;

    @BeforeEach
    public void setUp() throws Exception {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(4, 4, 3, p1, p2);
        frame = new MainWindow(p1, p2, board);
        frame.setVisible(true);
        MainWindow.setDebugg(true);
        click = new ClickOk();
    }

    @AfterEach
    public void tearDown() throws Exception {
        p1 = p2 = null;
        board = null;
        frame = null;
    }

    // Hilfsmethoden für page object model
    public void zug(int reihe, int spalte) {
        frame.turn(reihe, spalte);
    }

    public WinState testCheckWinState() {
        return board.checkWin();
    }

    public void testCheckWinner(WinState wst) {
        frame.checkWinner(wst);
    }

    public String getFieldEntry(int reihe, int spalte) {
        return board.getPlayerNameInField(reihe, spalte);
    }

    // ------------------- OUR TESTS START -------------------
    @Test
    public void doppelPlatzierenNichtMoeglichTest() {
        //Arrange
        zug(0, 0);
        //Act
        zug(0, 0);
        String retString = getFieldEntry(0, 0);
        //Assert
        assertEquals("Player 1", retString);
    }

    @Test
    public void clickOnFieldWorkOneWorks(){
        frame.getButtonArr()[0][0].doClick();
        assertEquals("Player 1", getFieldEntry(0, 0));
    }

    // Fehler 2
    //failed successfully
    @Test
    @DisplayName("Man gewinnt über eine L formation")
    void gewinnUberEckeTest() {
        //Arrange
        zug(0, 0);
        zug(0, 1);
        zug(1, 0);
        zug(0, 2);
        //Act
        zug(1, 1);
        //Assert
        assertSame(WinState.none, testCheckWinState());
    }

    public String getPlayer1Score() {
        return frame.getPlayer1_score().getText();
    }

    public String getPlayer2Score() {
        return frame.getPlayer2_score().getText();
    }

    // Ein Test der testet ob der Spieler 1 Punkte bekommt, wenn er gewinnt
    @Test
    public void player1EinPktBeiSieg() {
        zug(0, 0);
        zug(1, 0);
        zug(0, 1);
        zug(1, 1);
        zug(0, 2);
        //Todo: In eigene Klasse
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        testCheckWinner(WinState.player1);
        //System.out.println(getPlayer1Score());
        assertEquals(1, Integer.valueOf(getPlayer1Score()));
    }

    // Fehler 3
    // Ein Test der testet ob der Spieler 1 Punkte bekommt, wenn er gewinnt
    @Test
    public void player2PositivePktBeiSieg() {
        zug(0, 0);
        zug(1, 0);
        zug(0, 1);
        zug(1, 1);
        zug(2, 2);
        zug(1, 2);
        click.click();
        testCheckWinner(WinState.player2);
        //System.out.println(Integer.valueOf(frame.getPlayer2_score().getText()));

        assertEquals(1, Integer.valueOf(getPlayer2Score()));
    }

    // Fehler 4.1: !
    @Test
    void keinGewinnDreiInEinerSpalteMitLeerzeichen() {
        zug(0, 0); //x
        zug(2, 0); //o
        zug(1, 0); //x
        zug(0, 1); //o
        zug(3, 0); //x
        assertSame(WinState.none, testCheckWinState());
    }

    // Fehler 4.2: i
    @Test
    void keinGewinnDreiInEinerMitLeerzeicheni() {
        zug(0, 0); //x
        zug(1, 0); //o
        zug(2, 0); //x
        zug(0, 1); //o
        zug(3, 0); //x
        assertSame(WinState.none, testCheckWinState());
    }

    @Test
    public void testBeiUntentschiedenkeinPunkt1() {
        zug(0, 1);//x
        zug(0, 0);//o
        zug(0, 2);//x
//Reihe 1
        zug(1, 1);//o
        zug(1, 0);//x
        zug(1, 2);//o
//Reihe 2
        zug(2, 1);//x
        zug(2, 0);//o
        zug(2, 2);//x
//Reihe 3
        zug(3, 1);//o
        zug(3, 0);//x
//Act
        zug(3, 2);//o
//Assert
        click.click();

        testCheckWinner(WinState.tie);
        assertEquals(0, Integer.valueOf(getPlayer1Score()));
    }

    @Test
    public void testBeiUntentschiedenkeinPunkt2() {
        zug(0, 1);//x
        zug(0, 0);//o
        zug(0, 2);//x
//Reihe 1
        zug(1, 1);//o
        zug(1, 0);//x
        zug(1, 2);//o
//Reihe 2
        zug(2, 1);//x
        zug(2, 0);//o
        zug(2, 2);//x
//Reihe 3
        zug(3, 1);//o
        zug(3, 0);//x
//Act
        zug(3, 2);//o
//Assert
        click.click();
        testCheckWinner(WinState.tie);
        assertEquals(0, Integer.valueOf(getPlayer2Score()));
    }

    // Fehler 5
    @Test
    void vFuehrtZumSieg() {
        zug(0, 0);
        zug(0, 1);
        zug(1, 1);
        zug(1, 2);
        zug(0, 2);
        assertEquals(testCheckWinState(), WinState.none);
    }


    @Test
    public void testTwentyMoves() {
        //Arrange
        for (int i = 0; i < 19; i++) {
            zug(2, 1);
            frame.resetBoard();
        }
        //Act
        zug(1, 1);
        // Assert
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals("        ", retString);
    }

    @Test
    public void testFieldBottomRightClickable() {
        //Arrange

        //Act
        zug(3, 3);
        //Assert
        String actualResult = getFieldEntry(3, 3);
        assertEquals("Player 1", actualResult);
    }

    @Test
    public void testTie() {
        //Arrange
        //Reihe 0
        zug(0, 1);//x
        zug(0, 0);//o
        zug(0, 2);//x
        //Reihe 1
        zug(1, 1);//o
        zug(1, 0);//x
        zug(1, 2);//o
        //Reihe 2
        zug(2, 1);//x
        zug(2, 0);//o
        zug(2, 2);//x
        //Reihe 3
        zug(3, 1);//o
        zug(3, 0);//x
        //Act
        zug(3, 2);//o
        //Assert

        assertEquals(WinState.none, testCheckWinState());
        //actual result
        //assertEquals(WinState.tie, board.checkWin());
    }


    @Test
    public void testSlashDiagonalWinOnRightBorder() {
        //Arrange
        zug(2, 1);//x
        zug(0, 0);//o
        zug(1, 2);//x
        zug(1, 0);//o
        //Act
        zug(0, 3);//x
        //Assert
        assertEquals(WinState.player1, testCheckWinState());
        //actual result
        //assertEquals(WinState.none, board.checkWin());
    }

    @DisplayName("Test [4|0] führt zum Sieg")
    @Test
    public void testFourZero() {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(3, 5, 3, p1, p2);
        frame = new MainWindow(p1, p2, board);

        frame.turn(0, 4);

        frame.turn(1, 0);

        frame.turn(1, 3);
        frame.turn(2, 0);

        frame.turn(2, 2);
        //frame.turn(0, 0);

        assertSame(WinState.player1, board.checkWin());
    }

    //todo Ist fertig; Bugfix in MainWindow Z.184
    @Test
    @DisplayName("Können zwei Icons dasselbe Icon haben? Sollte nicht sein")
    public void testSetNotSameIcon() {
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("X");
        player2Icon.setSelectedItem("X");
        click.click();
        saveButton.doClick();
        //click.click();
        assertNotEquals(p1.getIcon(),p2.getIcon());
    }

    //todo Ist fertig; Bugfix in Ressource Z.13
    @Test
    @DisplayName("Die falsche Farbe wurde angezeigt. Die richtigen Ressourcen müssen genommen werden")
    public void tessaBlueFunktioniertIconRessource() {
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("TeSSA Blue");
        saveButton.doClick();

        Player p3 = new Player("Player 3", Ressources.icon_x);
        p3.setIcon(new ImageIcon(MainWindow.class.getResource("/res/tessa_b.png")));

        assertEquals(p3.getIcon().toString(), p1.getIcon().toString());
    }

    //todo Ist fertig; Bugfix in MainWindow Z.207
    @Test
    @DisplayName("Auswählen des richtigen Icons nicht möglich; " +
            "Spieler 2 wird beim ändern des Icons in den Settings, " +
            "wenn Spieler 1 -X- ist, zu -Tessa Blue- nachdem man den Button -save changes- klickt")
    public void tessaBlueBeiSpieler1XSpieler2TessaBlue() {
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("X");
        player2Icon.setSelectedItem("O");

        saveButton.doClick();

        assertEquals(Ressources.icon_o, p2.getIcon());

    }

    // ------------------- OUR TESTS END -------------------

    //------------------- TESTS ANFANG -------------------
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

    // nicht im  Dokument
    // New Test
    // should fail


    // ------------------- TESTS ENDE -------------------

    /*
    1. rechts unten - /done Christoph
    2. gewinn über ecke - check /done (André)
    3. exp Punkte minus Punkte - check aber nochmal drueber schauen /not done (André)
    4. i und ! Sieg - Chrissy
    5. wth Test von uns der passen könnte - Chrissy
    6. 20. Zug - carina 🖐
    7. v = Sieg - André /done
    8. unentschieden, obwohl felder frei - /done Christoph
    9. backlash- formation kein Sieg, wenn es an die rechte Spalte grenzt - /done Christoph
    10. [4][0} nicht ausgewertet = kein Sieg, wenn benutzt wird - carina 🖐
    */


    // nicht im Dokument
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
                // The board should be empty
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

    //Testet nur ob der frame nicht null ist
    @Test
    public void testMainWindowCreation() {
        Player p1 = new Player("Player 1", Ressources.icon_x);
        Player p2 = new Player("Player 2", Ressources.icon_o);
        Board board = new Board(4, 5, 3, p1, p2);
        MainWindow frame = new MainWindow(p1, p2, board);
        assertNotNull(frame);
    }

    //Testet nur ob der Debug mode richtig gesetzt wird
    @Test
    public void testDebugMode() {
        TeSSA_Tac_Toe.DEBUG = true;
        assertTrue(TeSSA_Tac_Toe.DEBUG);
        TeSSA_Tac_Toe.DEBUG = false;
        assertFalse(TeSSA_Tac_Toe.DEBUG);
    }

    //Testet gar nichts
    @Test
    public void testMainMethodTrue() {
        String[] args = {"1"};
        TeSSA_Tac_Toe.main(args);
        assertTrue(TeSSA_Tac_Toe.DEBUG = true);
    }


    //Testet
    @Test
    public void testGetOIconString() {
        // Arrange
        String expected = "X";

        // Act
        String actual = p1.getIconString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTessaRedIconString() {
        Player p3 = new Player("Bob", Ressources.icon_tessa_red);
        // Arrange
        String expected = "TeSSA red";

        // Act
        String actual = p3.getIconString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTessaBlueIconString() {
        Player p3 = new Player("Bob", Ressources.icon_tessa_blue);
        // Arrange
        String expected = "TeSSA blue";

        // Act
        String actual = p3.getIconString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetXIconString() {
        // Arrange
        String expected = "O";

        // Act
        String actual = p2.getIconString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyIconString() {
        // Arrange
        Player p3 = new Player("Bob", Ressources.icon_none);
        String expected = "";

        // Act
        String actual = p3.getIconString();

        // Assert
        assertEquals(expected, actual);
    }


    //Testet ob der Debug mode nach den Argumenten richtig gsetzt wird
    @Test
    public void testMainDebugFalse() {
        String[] args = {"0"};
        TeSSA_Tac_Toe.main(args);
        assertFalse(TeSSA_Tac_Toe.DEBUG);
    }

    //Testet ob das default Board richtig gesetzt wird vielleicht mit anderen Werten testen
    @Test
    public void testMainDefaultBoard() {
        TeSSA_Tac_Toe.main(new String[]{}); // default board
        assertEquals(4, board.getN());
        assertEquals(4, board.getM());
        assertEquals(3, board.getK());
    }

    //Test, ob mit den Argumenten das richtig board gesetzt wird
    @Test
    public void testMainCustomBoard() {
        TeSSA_Tac_Toe.main(new String[]{"4", "4", "3"});
        assertEquals(4, board.getN());
        assertEquals(4, board.getM());
        assertEquals(3, board.getK());
    }

    @Test
    public void testeSpielFeldGroesse() {
        assertEquals(16, board.getSize());
    }

    @Test
    public void testSymbolXtoString() {
        p1.setIcon(Ressources.icon_x);
        assertSame("X", p1.getIconString());
    }

    @Test
    public void testSymbolOtoString() {
        p1.setIcon(Ressources.icon_o);
        assertSame("O", p1.getIconString());
    }

    @Test
    public void testSymbolRedtoString() {
        p1.setIcon(Ressources.icon_tessa_red);
        assertSame("TeSSA red", p1.getIconString());
    }

    @Test
    public void testSymbolBluetoString() {
        p1.setIcon(Ressources.icon_tessa_blue);
        assertSame("TeSSA blue", p1.getIconString());
    }

    @Test
    public void testSetDifferentIcon() {
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("O");
        player2Icon.setSelectedItem("TeSSA Red");
        System.out.println(p1.getIconString());
        System.out.println(p2.getIconString());
        saveButton.doClick();
        assertNotEquals(p1.getIcon(), p2.getIcon());
    }

    @Test
    public void testIconPlayer1TeSSARed(){
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("TeSSA Red");

        saveButton.doClick();

        assertEquals(Ressources.icon_tessa_red, p1.getIcon());
    }

    @Test
    public void testIconPlayer1TeSSABlue(){
        JPanel settingsPanel = (JPanel) frame.settingsFrame().getContentPane().getComponent(0);
        JComboBox player1Icon = (JComboBox) settingsPanel.getComponent(1);
        JComboBox player2Icon = (JComboBox) settingsPanel.getComponent(3);
        JButton saveButton = (JButton) settingsPanel.getComponent(4);

        player1Icon.setSelectedItem("TeSSA Blue");

        saveButton.doClick();

        assertEquals(Ressources.icon_tessa_blue, p1.getIcon());
    }

    @Test
    public void testKlienerDreiMitZweiWinWin(){
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(2, 2, 2, p1, p2);
        frame = new MainWindow(p1, p2, board);
        frame.turn(0, 0);
        frame.turn(1, 0);
        frame.turn(0, 1);
        assertSame(WinState.player1, board.checkWin());
    }
    public class ClickOk extends Thread {

        public void click(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        Robot robot = new Robot();
                        robot.keyPress(KeyEvent.VK_ENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }



}