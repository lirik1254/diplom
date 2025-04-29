package backend.academy.diplom.entities;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class RefreshToken {
    private long id;
    private long userId;
    private String token;
    private Instant expireDate;
}
