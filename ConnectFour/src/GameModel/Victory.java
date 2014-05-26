package GameModel;

/**
 * ConnectFour Victory
 *
 * Prüft ob das Gewinnlimit erreicht wurde.
 *
 * @author S. Winterberger <stefan.winterberger@stud.hslu.ch>
 * @author R. Ritter <reto.ritter@stud.hslu.ch>
 * @author D. Niederberger <david.niederberger@stud.hslu.ch>
 * @author F. Wüthrich <fabian.wuethrich.01@stud.hslu.ch>
 *
 * @version 1.0
 */
public class Victory {

    //FIELDS--------------------------------------------------------------------
    private static final boolean debug = false;

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Prüft das Spielfeld auf einen Gewinner
     *
     * @param matrix Spielfeld welches geprüft werden soll
     * @param row Reihe des zuletzt eingefügter Steins
     * @param col Spalte des zuletzt eingefügten Steins
     * @param winLimit Anzahl Steine die es für einen Sieg braucht
     * @param player Spieler für welchen auf Sieg geprüft wird
     * @return true= Sieger gefunden false=kein Sieger gefunden
     */
    public static boolean isVictory(int[][] matrix, int row, int col, int winLimit, int player) {

        if (Victory.debug) {
            System.out.println("\nGewinnerprüfung für Spieler " + player);
        }

        //Nach links und rechts
        int counter_horizontal = checkHorizontal(matrix, row, player);
        //Nach unten und oben
        int counter_vertikal = checkVertikal(matrix, col, player);
        //Hauptdiagonale (Von links oben nach rechts unten)
        int counter_diagonal1 = checkDiag1(matrix, row, col, player);
        //2te Diagonale (Von rechts oben nach links unten)
        int counter_diagonal2 = checkDiag2(matrix, row, col, player);

        return counter_horizontal == winLimit
                | counter_vertikal == winLimit
                | counter_diagonal1 == winLimit
                | counter_diagonal2 == winLimit;
    }

    //PRIVATE METHODS-----------------------------------------------------------
    /*
     * Gibt die Anzahl Steine eines Spielers zurück die in einer REIHE
     * aufeinander folgen
     *
     * @param matrix Matrix
     * @param row reihe die geprüft wird
     * @param player Spieler von dem die Steine geprüft werden
     * @return Anzahl aufeinanderfolgende Spielesteine eines Spielers
     */
    private static int checkHorizontal(int[][] matrix, int row, int player) {
        int counter = 0;
        int steineinfolge = 0;

        for (int i = 0; i < 7; i++) {
            if (matrix[row][i] == player) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        if (Victory.debug) {
            System.out.println("In der Reihe " + row + " wurden " + steineinfolge + " Steine "
                    + "von Spieler " + player + " gezählt");
        }

        return steineinfolge;
    }

    /*
     * Gibt die Anzahl Steine eines Spielers zurück die in einer SPALTE
     * aufeinander folgen
     *
     * @param matrix Matrix
     * @param col spalte die geprüft wird
     * @param player Spieler von dem die Steine geprüft werden
     * @return Anzahl aufeinanderfolgende Spielesteine eines Spielers
     */
    private static int checkVertikal(int[][] matrix, int col, int player) {
        int counter = 0;
        int steineinfolge = 0;

        for (int i = 0; i < 6; i++) {
            if (matrix[i][col] == player) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        if (Victory.debug) {
            System.out.println("In dieser Spalte wurden " + steineinfolge + " Steine "
                    + "von Spieler " + player + " gezählt");
        }

        return steineinfolge;
    }

    /*
     * Gibt die Anzahl Steine eines Spielers zurück die in der HAUPTDIAGONALE
     * aufeinander folgen
     *
     * @param matrix Matrix
     * @param col spalte die geprüft wird
     * @param row reihe die geprüft wird
     * @param player Spieler von dem die Steine geprüft werden
     * @return Anzahl aufeinanderfolgende Spielesteine eines Spielers
     */
    private static int checkDiag1(int[][] matrix, int row, int col, int player) {
        // Nach oben links suchen
        int counter = 1;
        int steineinfolge = 1;
        for (int i = 1; i <= (Math.min(0 + col, 0 + row)); i++) {
            if (matrix[row - i][col - i] == player) {
                counter += 1;
            } else {
                counter = 0;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        // Wenn gegen Links Oben keine Steinreihe gefunden wurde
        if (counter < 1) {
            counter = 1;
        }

        // Nach unten rechts suchen
        for (int i = 1; i <= Math.min(6 - col, 5 - row); i++) {
            if (matrix[row + i][col + i] == player) {
                counter += 1;
            } else {
                counter = 0;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        if (Victory.debug) {
            System.out.println("In der Diagonale1 (links obben - rechts unten) "
                    + "wurden " + steineinfolge + " Steine von Spieler "
                    + player + " gezählt");
        }

        return steineinfolge;
    }

    /*
     * Gibt die Anzahl Steine eines Spielers zurück die in der NEBENDIAGONALE
     * aufeinander folgen
     *
     * @param matrix Matrix
     * @param col spalte die geprüft wird
     * @param row reihe die geprüft wird
     * @param player Spieler von dem die Steine geprüft werden
     * @return Anzahl aufeinanderfolgende Spielesteine eines Spielers
     */
    private static int checkDiag2(int[][] matrix, int row, int col, int player) {
        // Nach oben rechts suchen
        int counter = 1;
        int steineinfolge = 1;
        for (int i = 1; i <= (Math.min(6 - col, 0 + row)); i++) {
            if (matrix[row - i][col + i] == player) {
                counter += 1;
            } else {
                counter = 1;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        // Wenn gegen Rechts Oben keine Steinreihe gefunden wurde
        if (counter < 1) {
            counter = 1;
        }

        // Nach unten links suchen
        for (int i = 1; i <= Math.min(0 + col, 5 - row); i++) {
            if (matrix[row + i][col - i] == player) {
                counter += 1;
            } else {
                counter = 1;
            }
            if (counter > steineinfolge) {
                steineinfolge = counter;
            }
        }

        if (Victory.debug) {
            System.out.println("In der Diagonale2 (links unten - rechts oben) "
                    + "wurden " + steineinfolge + " Steine von Spieler "
                    + player + " gezählt");
        }

        return steineinfolge;
    }
}
