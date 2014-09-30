package GUI;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectFour WaitForOtherPlayerDialog
 *
 * Dialog während der Spielersuche
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 * @author S. Erni <simon.erni@stud.hslu.ch>
 * 
 * @version 1.0
 */
public class WaitForOtherPlayerDialog extends JDialog implements ActionListener {

    //FIELDS--------------------------------------------------------------------
    private final JProgressBar progressBar;
    private final JLabel lblIPAdress;

    
    private String getLocalIPAdresses()
    {
        String ipadress = "";
        try {
            String localHost = InetAddress.getLocalHost().getHostName();
            ipadress = "<html><h1>Lokaler PC: </h1>";
            for (InetAddress ia : InetAddress.getAllByName(localHost)) {
                ipadress = ipadress + ia + "<br>";
            }
            ipadress = ipadress + "</html>";
        } catch (UnknownHostException ex) {
            Logger.getLogger(WaitForOtherPlayerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ipadress;
    }
    
    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt ein Fenster mit einer Progressbar während der Spielersuche
     *
     * @param owner
     * @param title
     */
    public WaitForOtherPlayerDialog(Frame owner, String title) {
        super(owner, title, true);       

        this.progressBar = new JProgressBar();
        this.lblIPAdress = new JLabel(getLocalIPAdresses());
        
        this.progressBar.setIndeterminate(true);

        setLayout(new BorderLayout());
        
        add(this.progressBar,BorderLayout.SOUTH);
        add(this.lblIPAdress,BorderLayout.NORTH);
        
        setLocationRelativeTo(owner);
        pack();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getID()==0)
        {
            this.setVisible(false);
        }
    }
}
