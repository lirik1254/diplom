package backend.academy.diplom.exceptions;

public class RefreshTokenExpireException extends RuntimeException {
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
