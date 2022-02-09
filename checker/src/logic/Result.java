package checker.src.logic;

public class Result {
    public boolean returnCode;
    public String msg;

    public Result(boolean rs, String msg) {
        this.returnCode = rs;
        this.msg = msg;
    }
}
