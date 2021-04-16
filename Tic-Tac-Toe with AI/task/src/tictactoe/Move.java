package tictactoe;

public class Move {
    private int result;
    private int[] nextMove = new int[2];

    Move() {

    }

    Move(int result) {
        this.result = result;
    }

    public int getNextMoveRow() {
        return this.nextMove[0];
    }

    public int getNextMoveCol() {
        return this.nextMove[1];
    }

    public void setNextMove(int[] nextMove) {
        this.nextMove = nextMove;
    }

    public int[] getNextMove() {
        return this.nextMove;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return this.result;
    }
}
