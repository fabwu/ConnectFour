package Player;

import java.io.Serializable;

/**
 * ConnectFour Player

 Repräsentiert einen Spieler.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public abstract class Player implements Serializable {

    private final int id;
    private String name;

    public Player(int id) {
        this.id = id;
    }

    public abstract int getNextMove();

    public abstract void invalidMove();

    public abstract void youWin();

    public abstract void youLose();

    public abstract void draw();

    public int getId() {
        return id;
    }

}
