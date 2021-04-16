package tictactoe;

public class Player {
    private String status;
    private String sign;

    Player(String status, String sign) {
        this.status = status;
        this.sign = sign;
    }

    public String getStatus() {
        return this.status;
    }

    public String getSign() {
        return this.sign;
    }
}
