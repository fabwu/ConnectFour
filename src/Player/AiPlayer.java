package Player;

import GameModel.GameModel;
import java.util.Observable;
import java.util.Observer;

/**
 * ConnectFour AiPlayer
 *
 * Repräsentiert einen Computerspieler (Intelligent)
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class AiPlayer extends Player implements Observer {

    //FIELDS--------------------------------------------------------------------
    private int[][] matrix;
    private GameTree gametree;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen KI-Spieler
     *
     * @param id Spieler 1 oder 2
     */
    public AiPlayer(int id) {
        super(id);
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Berechnet den nächsten Spielzug der KI
     *
     * @return nächster Spielzug
     */
    @Override
    public int getNextMove() {
        gametree = new GameTree(matrix, getId(), 5);
        return gametree.getNextmove();
    }

    /**
     * Zeichnet das Spielfeld anhand des aktuellen Models neu
     *
     * @param o Observer vom Typ GameModel
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        GameModel model = (GameModel) o;
        this.matrix = model.getMatrix();
    }

    @Override
    public void invalidMove() {

    }

    @Override
    public void youWin() {

    }

    @Override
    public void youLose() {

    }

    @Override
    public void draw() {

    }

}
