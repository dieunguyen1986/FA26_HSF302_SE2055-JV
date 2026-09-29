package fu.hr.exception;

public class JobInvalidException extends RuntimeException {
    private int errorCode;

    public JobInvalidException(String message) {
        super(message);
    }

    public JobInvalidException(String message, int code) {
        this.errorCode = code;

    }
}
