package backend.academy.diplom.exceptions;

public class InvalidPasswordResetToken extends RuntimeException {
    public InvalidPasswordResetToken(String message) {
        super(message);
    }
}
