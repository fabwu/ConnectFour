package GameControl;

import GUI.ConnectFourGUI;
import GameModel.*;
import Player.AiPlayer;
import Player.LocalPlayer;
import Player.NetPlayer.ClientPlayer;
import Player.NetPlayer.ServerPlayer;
import Player.Player;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectFour Controller
 *
 * Erstellt das Model und das GUI. Regelt den Spielablauf.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 * @author S. Erni <simon.erni@stud.hslu.ch>
 *
 * @version 1.0
 */
public class GameControl implements Runnable {

    //FIELDS--------------------------------------------------------------------
    private final GameModel gamemodel;
    private final ConnectFourGUI gamegui;
    private Thread game;

    public static final int PORT = 8888;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Konstruktor erstellt die nötigen Komponenten des Spiels (Model, View)
     */
    public GameControl() {
        // Model erstellen
        this.gamemodel = new GameModel();

        // GUI erstellen und Controller übergeben
        this.gamegui = new ConnectFourGUI(this);

        createLocalLocalGame();
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Speichert das Spiel, in dem das GameModel serialisiert und in eine Datei
     * abgelegt wird.
     *
     * @param path Pfad unter dem das Spiel gespeichert wird
     */
    public void saveGame(String path) {
        try {
            FileOutputStream aFileOutputStream = new FileOutputStream(path);
            try (ObjectOutputStream aObjectOutputStream = new ObjectOutputStream(aFileOutputStream)) {
                aObjectOutputStream.writeObject(this.gamemodel);

                this.gamegui.saveSuccessful();
            }
        } catch (IOException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lädt ein gespeichertes Spiel
     *
     * @param path Pfad zum Spielstand
     */
    public void loadGame(String path) {
        interruptGameCycle();

        try {
            FileInputStream aFileInputStream = new FileInputStream(path);
            try (ObjectInputStream aObjectInputStream = new ObjectInputStream(aFileInputStream)) {
                // Temporäres Model erstellen
                GameModel tempModel = (GameModel) aObjectInputStream.readObject();

                // Spiel mit dem temporärer Model erstellen
                createLoadGame(tempModel);
            } catch (ClassNotFoundException ex) {
                //TODO Fehlermeldung ausgeben
                this.gamegui.errorMessage("Das Spiel konnte nicht geladen werden");
            }
        } catch (IOException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Erstellt ein Spiel aus einem abgespeicherten Spielstand
     *
     * @param tempModel Das abgespeicherte Model
     */
    private void createLoadGame(GameModel tempModel) {
        LocalPlayer player1 = (LocalPlayer) tempModel.getPlayer1();
        Player player2 = tempModel.getPlayer2();

        // Gamegui übergeben weil diese nicht serialisiert wird
        player1.setGameui(this.gamegui);

        // Model mit gespeicherten Daten initialisieren
        this.gamemodel.initModel(player1, player2, tempModel.getMatrix(), tempModel.getActualPlayer());

        // Observer/Listener hinzufügen, die für beide Spieltypen (Lokal/Lokal, Lokal/Computer) gelten
        this.gamemodel.addObserver(gamegui);
        this.gamegui.addColumnButtonListener(player1);

        if (player2 instanceof AiPlayer) {
            // Falls ein Spiel gegen den Computer gespeichert wurde
            AiPlayer aiPlayer = (AiPlayer) player2;
            this.gamemodel.addObserver(aiPlayer);
        } else {
            // Falls ein Spiel gegen einen lokalen Spieler gespeichert wurde
            LocalPlayer localPlayer = (LocalPlayer) player2;
            this.gamegui.addColumnButtonListener(localPlayer);
        }

        // Buttons aktivieren
        this.gamegui.enableColumnButtons();
        this.gamegui.enableSaveButton();

        // Spiel starten
        this.game = new Thread(this, "Game-Cycle (Load)");
        this.game.start();
    }

    /**
     * Erstellt ein Spiel gegen einen Spieler am gleichen Bildschirm
     */
    public void createLocalLocalGame() {
        interruptGameCycle();
        
        // Lokaler Spieler erstellen
        LocalPlayer localPlayer1 = new LocalPlayer(1, this.gamegui);

        // Computerspieler erstellen
        LocalPlayer localPlayer2 = new LocalPlayer(2, this.gamegui);

        // Listener registrieren
        this.gamemodel.addObserver(gamegui);
        ActionListener[] actionListenerArray
                = {
                    localPlayer1, localPlayer2
                };
        this.gamegui.addColumnButtonListener(actionListenerArray);

        // Model initialisieren (Neues Spiel)
        this.gamemodel.initModel(localPlayer1, localPlayer2);

        // Buttons aktivieren
        this.gamegui.enableColumnButtons();
        this.gamegui.enableSaveButton();

        // GUI sichtbar machen
        this.gamegui.setVisible(true);

        // Spiel starten
        this.game = new Thread(this, "Game-Cycle (LocalLocal)");
        this.game.start();
    }

    /**
     * Erstellt ein Spiel gegen den Computer
     */
    public void createLocalComputerGame() {
        interruptGameCycle();
        
        // Lokaler Spieler erstellen
        LocalPlayer localPlayer = new LocalPlayer(1, this.gamegui);

        // Computerspieler erstellen
        AiPlayer aiPlayer = new AiPlayer(2);

        // Listener registrieren
        this.gamemodel.addObserver(gamegui);
        this.gamemodel.addObserver(aiPlayer);

        this.gamegui.addColumnButtonListener(localPlayer);

        // Model initialisieren (Neues Spiel)
        this.gamemodel.initModel(localPlayer, aiPlayer);

        // Buttons aktivieren
        this.gamegui.enableColumnButtons();
        this.gamegui.enableSaveButton();

        // GUI sichtbar machen
        this.gamegui.setVisible(true);

        // Spiel starten
        this.game = new Thread(this, "Game-Cycle (LocalComputer)");
        this.game.start();
    }

    /**
     * Erstellt ein Spiel übers Netzwerk
     *
     * @param clientSocket
     */
    public void createServerGame(Socket clientSocket) {
        interruptGameCycle();
        
        // Lokaler Spieler erstellen
        LocalPlayer localPlayer = new LocalPlayer(1, this.gamegui);

        // Computerspieler erstellen
        ServerPlayer netPlayer = new ServerPlayer(2, clientSocket);

        // Listener registrieren
        this.gamemodel.addObserver(gamegui);
        this.gamemodel.addObserver(netPlayer);

        this.gamegui.addColumnButtonListener(localPlayer);

        // Model initialisieren (Neues Spiel)
        this.gamemodel.initModel(localPlayer, netPlayer);

        // Buttons aktivieren
        this.gamegui.enableColumnButtons();
        this.gamegui.disableSaveButton();

        // GUI sichtbar machen
        this.gamegui.setVisible(true);

        // Spiel starten
        this.game = new Thread(this, "Game-Cycle (Network)");
        this.game.start();
    }

    /**
     * Erstellt ein neues Serverspiel als Client
     *
     * @param serverSocket
     */
    public void createClientGame(Socket serverSocket) {
        interruptGameCycle();
        
        // Client erstellen
        ClientPlayer client = new ClientPlayer(serverSocket, this.gamegui);

        this.gamegui.addColumnButtonListener(client);
        // Buttons aktivieren
        this.gamegui.enableColumnButtons();
        this.gamegui.disableSaveButton();

        // GUI sichtbar machen
        this.gamegui.setVisible(true);
    }

    /**
     * Durchläuft den Spielablauf
     */
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                gamemodel.insertCellContent(gamemodel.getActualPlayer().getNextMove());
                gamemodel.changePlayer();
            } catch (InvalidMoveException ex) {
                gamemodel.setInvalid(true);
                gamemodel.notifyObservers();
                gamemodel.getActualPlayer().invalidMove();

            } catch (GameOverException ex) {
                gamemodel.getActualPlayer().youWin();

                if (gamemodel.getActualPlayer().equals(gamemodel.getPlayer1())) {
                    gamemodel.setLose(true);
                    gamemodel.notifyObservers();

                    gamemodel.getPlayer2().youLose();

                } else {
                    gamemodel.setWon(true);
                    gamemodel.notifyObservers();

                    gamemodel.getPlayer1().youLose();
                }
                break;
            } catch (DrawException ex) {
                gamemodel.setDraw(true);
                gamemodel.notifyObservers();
                gamemodel.getPlayer1().draw();
                gamemodel.getPlayer2().draw();
                break;
            }
        }

    }

    /*
     *  Unterbricht den Spielezyklus
     */
    private void interruptGameCycle() {
        // Game-Thread unterbrechen
        if (this.game != null) {
            while (this.game.isAlive()) {
                this.game.interrupt();
            }
        }
    }

}
