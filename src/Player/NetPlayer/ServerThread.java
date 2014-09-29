package Player.NetPlayer;

import GameControl.GameControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private ServerSocket serverSocket; 
    private final ArrayList<ActionListener> listeners;

    public ServerThread(GameControl control) {
        super("Server Thread");
        listeners = new ArrayList<>();
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

    public void addActionListener(ActionListener a) {
        listeners.add(a);
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(GameControl.PORT);
            Socket clientSocket = serverSocket.accept();

            for (ActionListener a : listeners) {
                a.actionPerformed(new ActionEvent(clientSocket, 0, "Client gefunden"));
            }
        } catch (IOException ex) {
        }
    }
}
