package GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * ConnectFour Kreis
 *
 * Stellt ein Spielstein dar.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class Kreis extends JPanel {

    //FILEDS--------------------------------------------------------------------
    private final int color;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen Spielstein mit der Farbe des Spielers
     *
     * @param n
     */
    public Kreis(int n) {
        super();
        this.color = n;
        setBackground(new java.awt.Color(0, 0, 204));
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Zeichnet den Spielstein auf das gewünschte Feld
     *
     * @param g Spielfeld auf welches gezeichnet werden soll
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        switch (color) {
            case 1:
                g.setColor(Color.red);
                g.fillOval(0, 0, getWidth(), getHeight());
                break;
            case 2:
                g.setColor(Color.yellow);
                g.fillOval(0, 0, getWidth(), getHeight());
                break;
            default:
                g.setColor(Color.white);
                g.fillOval(0, 0, getWidth(), getHeight());
                break;
        }

        g.setColor(Color.black);
        g.drawOval(0, 0, getWidth(), getHeight());
    }

}
