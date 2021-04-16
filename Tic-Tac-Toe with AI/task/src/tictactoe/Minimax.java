package tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Minimax {
    private Player ai;
    private Player opponent;

    //assume that pl1 always ai hard lvl
    Minimax(Player pl1, Player pl2) {
        this.ai = pl1;
        this.opponent = pl2;
    }


    public Move minimax(String[][] newBoard) {
        return this.minimax(newBoard, this.ai);
    }

    private Move minimax(String[][] newBoard, Player pl) {
        int[][] availableSpots = availableSpots(newBoard);

        if (Main.isPlayerWinner(this.opponent, newBoard)) {
            return new Move(-10);
        } else if(Main.isPlayerWinner(this.ai, newBoard)) {
            return new Move(10);
        } else if (availableSpots.length == 0) {
            return new Move(0);
        }

        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < availableSpots.length; i++) {
            Move move = new Move();
            move.setNextMove(availableSpots[i]);
            Main.setCellValue(move.getNextMoveRow(), move.getNextMoveCol(), pl.getSign(), newBoard);
            if (pl == this.ai) {
                Move result = minimax(newBoard, this.opponent);
                move.setResult(result.getResult());
            } else {
                Move result = minimax(newBoard, this.ai);
                move.setResult(result.getResult());
            }
            Main.setEmptyCell(move.getNextMoveRow(), move.getNextMoveCol(), newBoard);
            moves.add(move);
        }

        Move bestMove = null;
        if (pl == this.ai) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).getResult() > bestScore) {
                    bestScore = moves.get(i).getResult();
                    bestMove = moves.get(i);
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).getResult() < bestScore) {
                    bestScore = moves.get(i).getResult();
                    bestMove = moves.get(i);
                }
            }
        }

        return bestMove;
    }

    public int[][] availableSpots(String[][] newBoard) {
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                if (newBoard[i][j].equals(" ")) {
                    int[] coords = new int[]{i, j};
                    list.add(coords);
                }
            }
        }

        if (list.size() == 0) {
            return new int[0][0];
        }

        int[][] listInts = new int[list.size()][list.get(0).length];
        for (int i = 0; i < list.size(); i++) {
            listInts[i] = list.get(i);
        }
        return listInts;
    }

}
