package GUI;

import java.io.File;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ConnectFour CustomFileChooser
 *
 * FileChooser zum Abspeichern eines ConnectFour-Spielstands
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class CustomFileChooser extends JFileChooser {

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen FileChooser mit dem Passenden FileFilter
     *
     * @param FileExtension
     */
    public CustomFileChooser(String FileExtension) {
        super();
        // FileFilter hinzufügen
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Spielstand", FileExtension);
        super.setFileFilter(filter);
        super.setAcceptAllFileFilterUsed(false);
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Fragen ob Spielstand überschreiben
     */
    @Override
    public void approveSelection() {
        File f = getSelectedFile();
        if (f.exists() && getDialogType() == SAVE_DIALOG) {
            int result = JOptionPane.showConfirmDialog(this, "Der Spielstand existiert bereits, überschreiben?", "Spielstand existiert bereits", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CLOSED_OPTION:
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
            }
        }
        super.approveSelection();
    }
}
