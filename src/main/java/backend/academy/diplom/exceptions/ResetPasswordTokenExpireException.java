package backend.academy.diplom.exceptions;

public class ResetPasswordTokenExpireException extends RuntimeException {
    public ResetPasswordTokenExpireException(String message) {
        super(message);
    }
}
