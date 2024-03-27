package exceptions;

public class MissingColumnException extends Exception {
    public MissingColumnException(String msg) {
        super(msg);
    }
}
