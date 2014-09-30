package Player.NetPlayer;

import GameControl.GameControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

    private final ArrayList<ActionListener> listeners;

    public ServerThread() {
        super("Server Thread");
        listeners = new ArrayList<>();
    }

    public void addSuccessListener(ActionListener a) {
        listeners.add(a);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(GameControl.PORT);
            Socket clientSocket = serverSocket.accept();

            for (ActionListener a : listeners) {
                a.actionPerformed(new ActionEvent(clientSocket, 0, "Client gefunden"));
            }
        } catch (IOException ex) {
            //ToDo: Logger
        }
    }
}
