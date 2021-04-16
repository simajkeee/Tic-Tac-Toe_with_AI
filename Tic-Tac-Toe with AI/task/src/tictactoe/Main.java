package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Scanner s = new Scanner(System.in);
    static Random r = new Random();
    final static String x = "X";
    final static String o = "O";
    final static String empty = "_";
    private static String[][] table = new String[3][3];

    public static void main(String[] args) {
        // write your code here
        String userInput = "_________";
        System.out.println("");
        setTable(userInput);

        boolean end = false;
        while (!end) {
            System.out.print("Input command: ");
            String[] userOption = s.nextLine().split("\\s");
            switch(userOption[0]) {
                case "exit":
                    end = true;
                    break;
                case "start":
                    if (userOption.length < 3) {
                        System.out.println("Bad parameters!");
                        break;
                    }

                    if (!userOption[1].equals("user") && !isProperAiLvl(userOption[1]) || !userOption[2].equals("user") && !isProperAiLvl(userOption[2])) {
                        System.out.println("Bad parameters!");
                        break;
                    }
                    outputTable(table);
                    startGameCycle(new Player(userOption[1], Main.x), new Player(userOption[2], Main.o));
                    clearTable(table);
                    break;
                default:
                    System.out.println("Bad parameters!");
            }
        }
    }

    public static boolean isProperAiLvl(String lvl) {
        return lvl.equals("easy") || lvl.equals("medium") || lvl.equals("hard");
    }

    public static void startGameCycle(Player user1, Player user2) {
        Player currentPlayer = user1;
        Player nextPlayer = user2;
        while(true) {
            callPlayerMove(currentPlayer, nextPlayer);
            outputTable(table);
            if (isPlayerWinner(currentPlayer, table)) {
                System.out.println(currentPlayer.getSign() + " wins");
                break;
            } else if(isDraw(currentPlayer, nextPlayer, table)) {
                System.out.println("Draw");
                break;
            } else {
                Player intermediate = currentPlayer;
                currentPlayer = nextPlayer;
                nextPlayer = intermediate;
            }
        }
    }

    public static void callPlayerMove(Player pl, Player nextPl) {
        String playerStatus = pl.getStatus();
        switch (playerStatus) {
            case "user":
                playerMove(pl);
                break;
            case "easy":
                System.out.println("Making move level \"" + pl.getStatus() + "\"");
                aiMoveEasy(pl);
                break;
            case "medium":
                System.out.println("Making move level \"" + pl.getStatus() + "\"");
                aiMoveMedium(pl, nextPl);
                break;
            case "hard":
                System.out.println("Making move level \"" + pl.getStatus() + "\"");
                aiMoveHard(pl, nextPl);
                break;
        }
    }

    public static void aiMoveHard(Player pl, Player nextPl) {
        Minimax mm = new Minimax(pl, nextPl);
        Move bestMove = mm.minimax(table);
        setCellValue(bestMove.getNextMoveRow(), bestMove.getNextMoveCol(), pl.getSign());
    }

    public static void aiMoveEasy(Player currentPlayer) {
        int c1 = r.nextInt(3);
        int c2 = r.nextInt(3);
        while(!setCellValue(c1, c2, currentPlayer.getSign())) {
            c1 = r.nextInt(3);
            c2 = r.nextInt(3);
        }
    }

    public static void aiMoveMedium(Player currentPlayer, Player nextPLayer) {
        if (setWinningMoveRow(currentPlayer)) return;
        if (preventOpponentWinningMove(currentPlayer, nextPLayer)) return;
        aiMoveEasy(currentPlayer);
    }

    public static boolean preventOpponentWinningMove(Player currentPlayer, Player nextPLayer) {
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j].equals(nextPLayer.getSign())) {
                    ++count;
                }
            }
            if (count == 2) {
                int emptyCell = getEmptyCellInRow(table, i);
                if (emptyCell != -1) {
                    setCellValue(i, getEmptyCellInRow(table, i), currentPlayer.getSign());
                    return true;
                }
            }
            count = 0;
        }

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[j][i].equals(nextPLayer.getSign())) {
                    ++count;
                }
            }
            if (count == 2) {
                int emptyCol = getEmptyCellInColumn(table, i);
                if (emptyCol != -1) {
                    setCellValue(getEmptyCellInColumn(table, i), i, currentPlayer.getSign());
                    return true;
                }
            }
            count = 0;
        }

        for (int i = 0; i < table.length; i++) {
            if (table[i][i].equals(nextPLayer.getSign())) {
                ++count;
            }
        }

        if (count == 2) {
            int emptyMain = getEmptyCellMainDiagonal(table);
            if (emptyMain != -1) {
                setCellValue(emptyMain, emptyMain, currentPlayer.getSign());
                return true;
            }

        }

        count = 0;
        for (int i = table.length - 1; i >= 0; i--) {
            if (table[i][table.length - 1 - i].equals(nextPLayer.getSign())) {
                ++count;
            }
        }

        if (count == 2) {
            int emptySide = getEmptyCellSideDiagonal(table);
            if (emptySide != -1) {
                setCellValue(emptySide, table.length - 1 - emptySide, currentPlayer.getSign());
                return true;
            }
        }

        return false;
    }

    public static boolean setWinningMoveRow(Player currentPlayer) {
        for (int i = 0; i < table.length; i++) {
            int counter = 0;
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j].equals(currentPlayer.getSign())) {
                    ++counter;
                }
            }
            if (counter == 2) {
                int emptyCell = getEmptyCellInRow(table, i);
                if (emptyCell != -1) {
                    setCellValue(i, getEmptyCellInRow(table, i), currentPlayer.getSign());
                    return true;
                }
            }
        }
        return false;
    }

    private static int getEmptyCellSideDiagonal(String[][] t) {
        for (int i = table.length - 1; i >= 0; i--) {
            if (table[i][table.length - 1 - i].equals(" ")) {
                return i;
            }
        }
        return -1;
    }

    private static int getEmptyCellMainDiagonal(String[][] t) {
        for (int i = 0; i < t.length; i++) {
            if(t[i][i].equals(" ")) {
                return i;
            }
        }
        return -1;
    }

    private static int getEmptyCellInColumn(String[][] t, int col) {
        for (int i = 0; i < t.length; i++) {
            if (t[i][col].equals(" ")) {
                return i;
            }
        }
        return -1;
    }

    private static int getEmptyCellInRow(String[][] t, int row) {
        for (int i = 0; i < t[row].length; i++) {
            if (t[row][i].equals(" ")) {
                return i;
            }
        }
        return -1;
    }

    public static void playerMove(Player currentPlayer) {
        boolean coordsCorrect = false;
        String coords = "";
        while(!coordsCorrect) {
            System.out.print("Enter the coordinates: ");
            coords = s.nextLine();
            System.out.println("");
            Pattern pattern = Pattern.compile("[^0-9\\s]+", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(coords);
            if (matcher.find()) {
                System.out.println("You should enter numbers!");
                continue;
            }

            int[] coordsArr = stringToIntArr(coords.split("\\s"));
            if (!coordsAreCorrectRange(coordsArr)) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            if (!setCellValue(coordsArr[0] - 1, coordsArr[1] - 1, currentPlayer.getSign())) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            coordsCorrect = true;
        }
    }

    public static boolean isDraw(Player p1, Player p2, String[][] t) {
        if (isPlayerWinner(p1, t) || isPlayerWinner(p2, t)) {
            return false;
        }

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (t[i][j].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isPlayerWinner(Player player, String[][] t) {
        int count = 0;
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (t[i][j].equals(player.getSign())) {
                    ++count;
                }
            }
            if (count == 3) {
                return true;
            }
            count = 0;
        }

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (t[j][i].equals(player.getSign())) {
                    ++count;
                }
            }
            if (count == 3) {
                return true;
            }
            count = 0;
        }
        
        for (int i = 0; i < t.length; i++) {
            if (t[i][i].equals(player.getSign())) {
                ++count;
            }
        }

        if (count == 3) {
            return true;
        }

        count = 0;
        for (int i = t.length - 1; i >= 0; i--) {
            if (t[i][t.length - 1 - i].equals(player.getSign())) {
                ++count;
            }
        }

        if (count == 3) {
            return true;
        }

        return false;
    }

    private static void clearTable(String[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                t[i][j] = " ";
            }
        }
    }

    private static void setTable(String userInput) {
        String[] ui = userInput.split("");
        int row = 0;
        int col = 0;
        for (String singleSymbol : ui) {
            if (singleSymbol.equals(Main.x) || singleSymbol.equals(Main.o)) {
                Main.setCellValue(row, col, singleSymbol);
            } else if(singleSymbol.equals(Main.empty)) {
                Main.setCellValue(row, col, " ");
            }
            ++col;
            if (col > 2) {
                ++row;
                col = 0;
            }

        }
    }

    public static boolean setCellValue(int row, int col, String val) {
        if (table[row][col] == null || table[row][col].equals(" ")) {
            table[row][col] = val;
            return true;
        }

        return false;
    }

    public static boolean setEmptyCell(int row, int col, String[][] t) {
        t[row][col] = " ";
        return true;
    }

    public static boolean setCellValue(int row, int col, String val, String[][] t) {
        if (t[row][col] == null || t[row][col].equals(" ")) {
            t[row][col] = val;
            return true;
        }

        return false;
    }

    public static void outputTable(String[][] t) {
        System.out.println("---------");
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (j == 0) System.out.print("|");

                System.out.print(" " + t[i][j]);

                if (j == t[i].length - 1) System.out.println(" |");
            }
        }
        System.out.println("---------");
    }

    public static int[] stringToIntArr(String[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = Integer.parseInt(arr[i]);
        }
        return newArr;
    }

    public static boolean coordsAreCorrectRange(int[] coords) {
        return coords[0] > 0 && coords[0] <= 3 && coords[1] > 0 && coords[1] <= 3;
    }
}
