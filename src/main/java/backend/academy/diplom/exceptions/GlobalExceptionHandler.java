package backend.academy.diplom.exceptions;

import backend.academy.diplom.DTO.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessTokenExpireException.class)
    public ResponseEntity<ExceptionMessage> accessTokenExpire() {
        return new ResponseEntity<>(new ExceptionMessage("access token истёк"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenExpireException.class)
    public ResponseEntity<ExceptionMessage> refreshTokenExpire() {
        return new ResponseEntity<>(new ExceptionMessage("refresh token истёк"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessTokenInvalidException.class)
    public ResponseEntity<ExceptionMessage> accessTokenInvalid() {
        return new ResponseEntity<>(new ExceptionMessage("access token невалиден"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ExceptionMessage> refreshTokenInvalid() {
        return new ResponseEntity<>(new ExceptionMessage("refresh token невалиден"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionMessage> invalidPassword() {
        return new ResponseEntity<>(new ExceptionMessage("Неверный email или пароль"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPasswordResetToken.class)
    public ResponseEntity<ExceptionMessage> invalidPasswordResetToken() {
        return new ResponseEntity<>(new ExceptionMessage("Срок действия токена истёк"), HttpStatus.UNAUTHORIZED);
    }
}
