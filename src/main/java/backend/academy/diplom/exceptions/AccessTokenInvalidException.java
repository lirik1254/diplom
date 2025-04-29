package backend.academy.diplom.exceptions;

public class AccessTokenInvalidException extends RuntimeException {
    public AccessTokenInvalidException(String message) {
        super(message);
    }
}
