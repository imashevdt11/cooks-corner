package kg.neobis.cookscorner.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message, Integer status) {
        super(message, status);
    }
}