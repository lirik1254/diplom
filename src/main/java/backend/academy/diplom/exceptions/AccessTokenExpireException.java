package backend.academy.diplom.exceptions;

public class AccessTokenExpireException extends RuntimeException {
    public AccessTokenExpireException(String message) {
        super(message);
    }
}
