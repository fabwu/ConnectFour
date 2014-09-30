package Player.NetPlayer;

import GameControl.GameControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ConnectFour ServerSearchThread

 Thread zum Verbindungsaufbau mit einem Server
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 * @author S. Erni <simon.erni@stud.hslu.ch>
 * 
 * @version 1.0
 */
public class ServerSearchThread extends Thread {

    //FIELDS--------------------------------------------------------------------
    private final String hostname;
    private final ArrayList<ActionListener> listeners;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Thread zum Verbindungsaufbau
     *
     *
     * @param hostname Der Hostname (oder IP Adresse) mit dem Verbunden werden
     * soll.
     */
    public ServerSearchThread(String hostname) {
        super("Client Thread");

        this.hostname = hostname;
        listeners = new ArrayList<>();
    }

    public void addSuccessListener(ActionListener a) {
        listeners.add(a);
    }

    /**
     * Ablauf des Verbindungsaufbaus zum Server
     */
    @Override
    public void run() {
        Socket clientSocket = null;
        while (clientSocket == null) {
            try {
                clientSocket = new Socket(hostname, GameControl.PORT);
            } catch (IOException ex) {
                //ToDo: Logger                 Logger.getLogger(ServerSearchThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (ActionListener a : listeners) {
            a.actionPerformed(new ActionEvent(clientSocket, 0, "Mit Server verbunden"));
        }
    }
}
