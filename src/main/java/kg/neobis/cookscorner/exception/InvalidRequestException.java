package kg.neobis.cookscorner.exception;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(String message, Integer status) {
        super(message, status);
    }
}