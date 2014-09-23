package GUI;

import GameControl.GameControl;
import GameModel.GameModel;
import Player.LocalPlayer;
import Player.NetPlayer.ClientThread;
import Player.NetPlayer.ServerThread;
import Player.Player;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * ConnectFour GUI
 *
 * Stellt eine grafische Benutzeroberfläche zur Verfügung.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class ConnectFourGUI extends javax.swing.JFrame implements Observer {

    //FIELDS--------------------------------------------------------------------
    private javax.swing.JButton jButtonColumn1;
    private javax.swing.JButton jButtonColumn2;
    private javax.swing.JButton jButtonColumn3;
    private javax.swing.JButton jButtonColumn4;
    private javax.swing.JButton jButtonColumn5;
    private javax.swing.JButton jButtonColumn6;
    private javax.swing.JButton jButtonColumn7;
    private javax.swing.JLabel jLabelGegner;
    private javax.swing.JLabel jLabelSpieler;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemClient;
    private javax.swing.JMenuItem jMenuItemComputer;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHelp;
    private javax.swing.JMenuItem jMenuItemLoad;
    private javax.swing.JMenuItem jMenuItemLokal;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemServer;
    private javax.swing.JMenu jMenuMultiplayer;
    private javax.swing.JMenu jMenuSingleplayer;
    private javax.swing.JPanel jPanelColumnButtons;
    private javax.swing.JPanel jPanelPlayer;
    private javax.swing.JPanel jPanelPlayground;
    private javax.swing.JPopupMenu.Separator jSeparator;
    private final GameControl control;
    private static final String FILE_EXTENSION = "dat";
    private final JFileChooser jFileChooser;
    private final Border borderline;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Initialisiert eine neues Spielfeld (GUI)
     *
     * @param control GameController
     */
    public ConnectFourGUI(GameControl control) {
        super("Vier Gewinnt");

        // Controller zuweisen
        this.control = control;

        // GUI Komponenten initialisieren
        initComponents();

        // Fenster in Bildschirmmitte anzeigen
        setLocationRelativeTo(getParent());

        // Rahmen erstellen
        this.borderline = BorderFactory.createLineBorder(Color.black);

        // FileChooser erstellen
        this.jFileChooser = new CustomFileChooser(FILE_EXTENSION);

    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Fügt ActionListener zu Spaltenbuttons hinzu.
     *
     * @param listener ActionListener
     */
    public void addColumnButtonListener(ActionListener listener) {
        removeColumnButtonListener();
        // Liest alle Spalten Buttons aus
        Component[] allButtons = jPanelColumnButtons.getComponents();

        // Fügt allen Spalten Buttons einen ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            button.addActionListener(listener);
        }
    }

    /**
     * Fügt eine Liste von ActionListener zu jedem Spaltenbutton hinzu
     *
     * @param allListener Liste mit ActionListener
     */
    public void addColumnButtonListener(ActionListener[] allListener) {
        removeColumnButtonListener();
        // Liest alle Spalten Buttons aus
        Component[] allButtons = jPanelColumnButtons.getComponents();

        // Fügt allen Spalten Buttons eine Liste von ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            for (ActionListener listener : allListener) {
                button.addActionListener(listener);
            }
        }
    }

    /**
     * Zeichnet das gesamt Spielfeld neu Anhand des aktuellen Models Wird
     * ausgeführt wenn das ObserverPattern eine Änderung meldet.
     *
     * @param o Observer vom Typ GameModel
     * @param arg
     */
    @Override
    public void update(final Observable o, Object arg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GameModel model = (GameModel) o;
                int[][] matrix = model.getMatrix();
                Player actualPlayer = model.getActualPlayer();
                Player player1 = model.getPlayer1();
                Player player2 = model.getPlayer2();

                jPanelPlayground.removeAll();

                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 7; col++) {
                        jPanelPlayground.add(new Kreis(matrix[row][col]));
                    }
                }

                if (actualPlayer.getId() != player1.getId()) {
                    if (!(player2 instanceof LocalPlayer)) {
                        disableColumnButtons();
                    }
                    jLabelGegner.setBorder(borderline);
                    jLabelSpieler.setBorder(null);
                } else {
                    if (!(player2 instanceof LocalPlayer)) {
                        enableColumnButtons();
                    }

                    jLabelSpieler.setBorder(borderline);
                    jLabelGegner.setBorder(null);
                }
            }
        });
    }

    /**
     * Gibt eine Fehlermeldung (Ungültiger Zug) aus.
     */
    public void invalidMove() {
        JOptionPane.showMessageDialog(null, "Dies ist ein Ungültiger Zug", "Ungültiger Zug", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Gibt eine Gewinnermeldung aus.
     */
    public void youWin() {
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(null, "Glückwunsch du hast gewonnen", "Gewonnen!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Gibt eine Verlorenmeldung aus.
     */
    public void youLose() {
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(null, "Du hast leider verloren", "Verloren!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Gibt eine Unentschiedenmeldung aus.
     */
    public void draw() {
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(null, "Das Spiel endet Unentschieden", "Unentschieden!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zeigt eine Meldung an wenn das Spiel erfolgreich gespeichert wurde.
     */
    public void saveSuccessful() {
        JOptionPane.showMessageDialog(rootPane, "Spiel erfolgreich gespeichert");
    }

    /**
     * Gibt eine Fehlermeldung aus
     *
     * @param errorMessage Fehlermeldung
     */
    public void errorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(rootPane, errorMessage, "Es ist ein Fehler aufgetreten", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Deaktiviert alle Spalten-Buttons
     */
    public void disableColumnButtons() {
        Component[] allButtons = jPanelColumnButtons.getComponents();

        for (Component button : allButtons) {
            button.setEnabled(false);
        }
    }

    /**
     * Aktiviert alle Spalten-Buttons
     */
    public void enableColumnButtons() {
        Component[] allButtons = jPanelColumnButtons.getComponents();

        for (Component button : allButtons) {
            button.setEnabled(true);
        }
    }

    /**
     * Deaktiviert den Speichern-Button
     */
    public void disableSaveButton() {
        this.jMenuItemSave.setEnabled(false);
    }

    /**
     * Aktiviert den Speichern-Button
     */
    public void enableSaveButton() {
        this.jMenuItemSave.setEnabled(true);
    }

    //PRIVATE METHODS-----------------------------------------------------------
    /*
     * Initialisiert alle Komponenten des GUI (wird vom GUI-Form in NetBeans
     * erzeugt und bearbeitet)
     */
    private void initComponents() {

        jPanelPlayground = new javax.swing.JPanel();
        jPanelColumnButtons = new javax.swing.JPanel();
        jButtonColumn1 = new javax.swing.JButton();
        jButtonColumn2 = new javax.swing.JButton();
        jButtonColumn3 = new javax.swing.JButton();
        jButtonColumn4 = new javax.swing.JButton();
        jButtonColumn5 = new javax.swing.JButton();
        jButtonColumn6 = new javax.swing.JButton();
        jButtonColumn7 = new javax.swing.JButton();
        jPanelPlayer = new javax.swing.JPanel();
        jLabelSpieler = new javax.swing.JLabel();
        jLabelGegner = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuSingleplayer = new javax.swing.JMenu();
        jMenuItemComputer = new javax.swing.JMenuItem();
        jMenuItemLokal = new javax.swing.JMenuItem();
        jMenuMultiplayer = new javax.swing.JMenu();
        jMenuItemServer = new javax.swing.JMenuItem();
        jMenuItemClient = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemLoad = new javax.swing.JMenuItem();
        jSeparator = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanelPlayground.setBackground(new java.awt.Color(0, 0, 204));
        jPanelPlayground.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelPlayground.setLayout(new java.awt.GridLayout(6, 0, 10, 5));

        jPanelColumnButtons.setLayout(new java.awt.GridLayout(1, 7, 6, 0));

        jButtonColumn1.setActionCommand("0");
        jPanelColumnButtons.add(jButtonColumn1);

        jButtonColumn2.setActionCommand("1");
        jPanelColumnButtons.add(jButtonColumn2);

        jButtonColumn3.setActionCommand("2");
        jPanelColumnButtons.add(jButtonColumn3);

        jButtonColumn4.setActionCommand("3");
        jPanelColumnButtons.add(jButtonColumn4);

        jButtonColumn5.setActionCommand("4");
        jPanelColumnButtons.add(jButtonColumn5);

        jButtonColumn6.setActionCommand("5");
        jPanelColumnButtons.add(jButtonColumn6);

        jButtonColumn7.setActionCommand("6");
        jPanelColumnButtons.add(jButtonColumn7);

        jPanelPlayer.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jLabelSpieler.setForeground(new java.awt.Color(204, 0, 0));
        jLabelSpieler.setText("Spieler");
        jLabelSpieler.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanelPlayer.add(jLabelSpieler);

        jLabelGegner.setForeground(new java.awt.Color(255, 204, 0));
        jLabelGegner.setText("Gegner");
        jLabelGegner.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanelPlayer.add(jLabelGegner);

        jMenuFile.setText("Datei");

        jMenuSingleplayer.setText("Singleplayer");

        jMenuItemComputer.setText("Computer");
        jMenuItemComputer.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemComputerActionPerformed(evt);
            }
        });
        jMenuSingleplayer.add(jMenuItemComputer);

        jMenuItemLokal.setText("Lokal");
        jMenuItemLokal.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLokalActionPerformed(evt);
            }
        });
        jMenuSingleplayer.add(jMenuItemLokal);

        jMenuFile.add(jMenuSingleplayer);

        jMenuMultiplayer.setText("Multiplayer");

        jMenuItemServer.setText("New Host");
        jMenuItemServer.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemServerActionPerformed(evt);
            }
        });
        jMenuMultiplayer.add(jMenuItemServer);

        jMenuItemClient.setText("Connect with Host");
        jMenuItemClient.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClientActionPerformed(evt);
            }
        });
        jMenuMultiplayer.add(jMenuItemClient);

        jMenuFile.add(jMenuMultiplayer);

        jMenuItemSave.setText("Save Game");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuItemLoad.setText("Load Game");
        jMenuItemLoad.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemLoad);
        jMenuFile.add(jSeparator);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenu4.setText("Help");

        jMenuItemHelp.setText("Help");
        jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemHelp);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanelColumnButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanelPlayground, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                        .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelColumnButtons, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanelPlayground, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(145, 145, 145)
                                        .addComponent(jPanelPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void removeColumnButtonListener() {
        // Liest alle Spalten Buttons aus
        Component[] allButtons = jPanelColumnButtons.getComponents();

        // Fügt allen Spalten Buttons einen ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            for (ActionListener actionListener : button.getActionListeners()) {
                if (actionListener != null) {
                    button.removeActionListener(actionListener);
                }
            }
        }
    }

    private void jMenuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Das Ziel des Spiels ist es 4 "
                + "Steine seiner Farbe in einer\nLinie (Senkrecht, "
                + "Waagrecht oder Diagonal) zu platzieren.\n"
                + "\n Developed by: Team 1, PRG2-FS2014: \nD. Niederberger\nR. Ritter\nS. Winterberger\nF. Wüthrich", "Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(EXIT_ON_CLOSE);
    }

    private void jMenuItemLoadActionPerformed(java.awt.event.ActionEvent evt) {
        String confirmMessage = "Willst du das aktuelle Spiel beenden und ein neues Spiel laden?";

        int choice = JOptionPane.showConfirmDialog(rootPane, confirmMessage, "",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            choice = jFileChooser.showOpenDialog(rootPane);
            if (choice == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith("." + FILE_EXTENSION)) {
                    path += "." + FILE_EXTENSION;
                }
                this.control.loadGame(path);
            }
        }
    }

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {
        int result = jFileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String path = jFileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith("." + FILE_EXTENSION)) {
                path += "." + FILE_EXTENSION;
            }
            control.saveGame(path);
        }
    }

    private void jMenuItemServerActionPerformed(java.awt.event.ActionEvent evt) {
        final WaitForPlayerDialog dialog = new WaitForPlayerDialog(this, "Auf Spieler warten...");
        final ServerThread serverThread = new ServerThread(this.control, dialog);

        dialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Thread abbrechen");
                serverThread.exit();
                dialog.setVisible(false);
            }
        });

        serverThread.start();

        dialog.setVisible(true);
    }

    private void jMenuItemComputerActionPerformed(java.awt.event.ActionEvent evt) {
        this.control.createLocalComputerGame();
    }

    private void jMenuItemLokalActionPerformed(java.awt.event.ActionEvent evt) {
        this.control.createLocalLocalGame();
    }

    private void jMenuItemClientActionPerformed(java.awt.event.ActionEvent evt) {
        String hostname = JOptionPane.showInputDialog(null, "Adresse des Servers:", "localhost");
        if (!hostname.equals("")) {
            final WaitForPlayerDialog dialog = new WaitForPlayerDialog(this, "Auf Server warten...");
            final ClientThread clientThread = new ClientThread(this.control, dialog, hostname);

            dialog.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clientThread.exit();
                    dialog.setVisible(false);
                }
            });

            clientThread.start();

            dialog.setVisible(true);
        } else {
            errorMessage("Gib einen Hostnamen ein!");
        }
    }

}
