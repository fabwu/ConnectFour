package GUI;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 * ConnectFour WaitForPlayerDialog
 *
 * Dialog während der Spielersuche
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class WaitForPlayerDialog extends JDialog {

    //FIELDS--------------------------------------------------------------------
    private final JProgressBar progressBar;
    private final JButton exitButton;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt ein Fenster mit einer Progressbar während der Spielersuche
     *
     * @param owner
     * @param title
     */
    public WaitForPlayerDialog(Frame owner, String title) {
        super(owner, title, true);

        this.progressBar = new JProgressBar();
        this.exitButton = new JButton("Abbrechen");

        this.progressBar.setIndeterminate(true);

        setLayout(new FlowLayout());
        add(this.progressBar);
        add(this.exitButton);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(300, 65);
        setLocationRelativeTo(owner);
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Fügt den Listener für den AbbrechButton hinzu
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        this.exitButton.addActionListener(listener);
    }

}
