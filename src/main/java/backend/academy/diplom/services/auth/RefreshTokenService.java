package backend.academy.diplom.services.auth;

import backend.academy.diplom.DTO.auth.RefreshRequest;
import backend.academy.diplom.DTO.auth.RefreshResponse;
import backend.academy.diplom.entities.RefreshToken;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.exceptions.RefreshTokenExpireException;
import backend.academy.diplom.exceptions.RefreshTokenInvalidException;
import backend.academy.diplom.repositories.auth.RefreshTokenRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.CreateAccessToken;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository usersRepository;
    private final CreateAccessToken createAccessToken;
    private final EntityManager entityManager;


    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(userId);
        refreshToken.setExpireDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpireException("");
        }

        return token;
    }

    @Transactional(dontRollbackOn = RefreshTokenExpireException.class)
    public RefreshResponse refresh(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();

        RefreshToken refreshTokenOld;
        try {
            refreshTokenOld = refreshTokenRepository.findRefreshTokenByToken(refreshToken).getFirst();
        } catch (Exception e) {
            throw new RefreshTokenInvalidException("");
        }

        verifyExpiration(refreshTokenOld);

        User users = usersRepository.findById(refreshTokenOld.getUserId()).getFirst();

        refreshTokenRepository.delete(refreshTokenOld);
        entityManager.flush();

        String accessToken = createAccessToken.generateJwtToken(users.getEmail());
        RefreshToken newRefreshToken = createRefreshToken(users.getId());



        return new RefreshResponse(accessToken, newRefreshToken.getToken());
    }
}
