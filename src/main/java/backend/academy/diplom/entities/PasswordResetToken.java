package backend.academy.diplom.entities;

import lombok.Data;

import java.time.Instant;

@Data
public class PasswordResetToken {
    private long id;
    private String token;
    private Instant expireDate;
    private long userId;

    public boolean isExpire() {
        return expireDate.compareTo(Instant.now()) < 0;
    }
}
