package Opponent.NetPlayer;

import GameControl.GameControl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;

/**
 * ConnectFour ServerThread
 *
 * Represäntiert einen Spieler(SERVER) welcher über Netzwerk Spielt
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class ServerThread extends Thread {

    private final GameControl control;
    private final JDialog panel;
    private ServerSocket serverSocket;

    public ServerThread(GameControl control, JDialog panel) {
        super("Server Thread");
        this.control = control;
        this.panel = panel;
    }

    public void exit() {
        if (this.serverSocket instanceof ServerSocket) {
            try {
                this.serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(GameControl.PORT);
            Socket clientSocket = serverSocket.accept();

            this.panel.setVisible(false);

            this.control.createServerGame(clientSocket);
        } catch (IOException ex) {
        }
    }
}
