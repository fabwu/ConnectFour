package Opponent.NetPlayer;

import GameControl.GameControl;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JDialog;

/**
 * ConnectFour ClientThread
 *
 * Thread zum Verbindungsaufbau mit einem Server
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class ClientThread extends Thread {

    //FIELDS--------------------------------------------------------------------
    private final GameControl control;
    private final JDialog dialog;
    private Socket socket;
    private final String hostname;
    private boolean exit;
    private boolean isConnected;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Thread zum Verbindungsaufbau
     *
     * @param control
     * @param panel
     * @param hostname
     */
    public ClientThread(GameControl control, JDialog panel, String hostname) {
        super("Client Thread");

        this.control = control;
        this.dialog = panel;
        this.hostname = hostname;
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Bricht den Verbindunsaufbau ab
     */
    public void exit() {
        this.exit = true;
        this.dialog.setVisible(false);
    }

    /**
     * Ablauf des Verbindungsaufbaus zum Server
     */
    @Override
    public void run() {
        try {
            do {
                try {
                    this.socket = new Socket(hostname, GameControl.PORT);
                    this.isConnected = true;
                } catch (ConnectException ex) {
                    System.out.println("Versuche nochmals einen Server zu finden");
                }
            } while (!this.isConnected && !this.exit);

            if (this.isConnected) {
                this.dialog.setVisible(false);
                System.out.println(this.socket instanceof Socket);
                this.control.createClientGame(this.socket);
            }
        } catch (UnknownHostException ex) {
            // 
        } catch (IOException ex) {

        }
    }
}
