package GameModel;

import Player.Player;
import java.io.Serializable;
import java.util.Observable;

/**
 * ConnectFour Model
 *
 * Speichert die Daten des Spiels.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 * @author S. Erni <simon.erni@stud.hslu.ch>
 *
 * @version 1.0
 */
public class GameModel extends Observable implements Serializable {

    //FIELDS--------------------------------------------------------------------
    private int[][] matrix;
    private Player player1, player2;
    private Player actualPlayer;  //Spieler der aktuell an der Reihe ist (1 oder 2)
    private final int rowLength;
    private final int colLength;
    private final int winLimit;

    //States for ClientPlayer
    private boolean draw = false,
            invalid = false,
            won = false,
            lose = false;

    //CONSTRUCTOR---------------------------------------------------------------
    /**
     * Konstruktor initialisiert Einstellungen
     */
    public GameModel() {
        this.rowLength = 6;
        this.colLength = 7;
        this.winLimit = 4;
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Setzt das Model in den Anfangszustand (Neues Spiel)
     *
     * @param player1 Spieler 1 kann ein lokaler, AI oder NetPlayer sein
     * @param player2 Spieler 2 kann ein AI oder NetPlayer sein
     */
    public void initModel(Player player1, Player player2) {
        setChanged();
        this.matrix = new int[this.rowLength][this.colLength];

        this.player1 = player1;
        this.player2 = player2;

        this.actualPlayer = player1;
        notifyObservers();
    }

    /**
     * Setzt das Model in den Anfangszustand (Spiel laden)
     *
     * @param player1 Spieler 1 kann ein lokaler, AI oder NetPlayer sein
     * @param player2 Spieler 2 kann ein AI oder NetPlayer
     * @param matrix Matrix die das Spiel abbildet
     * @param actualPlayer Spieler der an der Reihe ist.
     */
    public void initModel(Player player1, Player player2, int[][] matrix, Player actualPlayer) {
        setChanged();
        this.matrix = matrix;

        this.player1 = player1;
        this.player2 = player2;

        this.actualPlayer = actualPlayer;
        notifyObservers();
    }

    /**
     * Fügt einen Spielstein in die Matrix ein
     *
     * @param col Spalte in welche der Stien eingefügt wird
     * @throws InvalidMoveException
     */
    public void insertCellContent(int col) throws InvalidMoveException , DrawException, GameOverException{
        validateColumn(col);
        int row = getRowToInsert(col);
        
        setChanged();
        matrix[row][col] = actualPlayer.getId();
        notifyObservers();

        checkVictory(col, row);
        checkDraw();
    }

    /**
     * Prüft das Spiel auf Unentschieden
     */
    private void checkDraw() throws DrawException {
        for (int row = 0; row < rowLength; row++) {
            for (int col = 0; col < colLength; col++) {
                if (matrix[row][col] == 0) {
                    return;
                }
            }
        }
        throw new DrawException();

    }

    /**
     * Prüft das Spiel auf einen Gewinner
     *
     * @param col Spalte des zuletzt eingefügten Steins
     * @param row Zeile des Zuletzt eingefügten Steins
     * @throws GameOverException
     */
    private void checkVictory(int col, int row) throws GameOverException {
        if (Victory.isVictory(this.matrix, row, col, this.winLimit, this.actualPlayer.getId())) {
            throw new GameOverException();
        }
    }

    /**
     * Gibt die Reihe zurück in welcher der Stein zu liegen kommt
     *
     * @param column Spalte welche untersucht werden soll
     * @return Reihe in welcher der Spielstein zu liegen kommt
     */
    private int getRowToInsert(int column) {
        //Lokale Variable um die Einzufügende Zeile zu ermitteln (Initialisiert auf die unterste Reihe)
        int rowToInsert = 5;

        //Ermittelt die einzufügende Zeile (die unterste Reihe welche nicht belegt ist)
        for (int i = 5; i >= 0; i--) {
            if (matrix[i][column] != 0) {
                rowToInsert = i - 1;
            } else {
                break;
            }
        }
        return rowToInsert;
    }

    /**
     * Wechselt den aktuellen Spieler
     */
    public void changePlayer() {
        setChanged();
        if (this.actualPlayer == player1) {
            this.actualPlayer = player2;
        } else if (actualPlayer == player2) {
            actualPlayer = player1;
        }
        notifyObservers();
    }

    //GETTER and SETTER --------------------------------------------------------
    public int[][] getMatrix() {
        return matrix;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
        this.setChanged();
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
        this.setChanged();
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
        this.setChanged();
    }

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
        this.setChanged();
    }

    //PRIVATE METHODS-----------------------------------------------------------
    /**
     * Prüft ob der Spielzug in der Spalte gültig ist (Spalte nicht voll + Spalte
     * im Range)
     *
     * @param rowToInsert Welche Reihe soll auf gültigkeit überprüft werden
     * @return True wenn der Zug gültig ist. False wenn der Zug ungültig ist.
     */
    private void validateColumn(int column) throws InvalidMoveException {              
        if (column < 0 | column > (colLength - 1) | matrix[0][column]!= 0) {
            throw new InvalidMoveException();
        }
    }
}
