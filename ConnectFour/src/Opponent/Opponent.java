package Opponent;

import java.io.Serializable;

/**
 * ConnectFour Opponent
 *
 * Repräsentiert einen Spieler.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public abstract class Opponent implements Serializable {

    //FIELDS--------------------------------------------------------------------
    private final int id;
    private String name;

    //CONSTRUCTORS--------------------------------------------------------------
    public Opponent(int id) {
        this.id = id;
    }

    //ABSTRACT METHODS----------------------------------------------------------
    public abstract int getNextMove();

    public abstract void invalidMove();

    public abstract void youWin();

    public abstract void youLose();

    public abstract void draw();

    //PUBLIC METHODS------------------------------------------------------------
    public int getNextMove(int[][] currentMatrix) {
        return 0;
    }

    //GETTER and SETTER---------------------------------------------------------
    public int getId() {
        return id;
    }

}
