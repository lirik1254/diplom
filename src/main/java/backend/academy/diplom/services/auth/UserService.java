package backend.academy.diplom.services.auth;

import backend.academy.diplom.AuthTokenFilter;
import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.DTO.auth.RefreshResponse;
import backend.academy.diplom.DTO.auth.ResetPasswordDTO;
import backend.academy.diplom.entities.PasswordResetToken;
import backend.academy.diplom.entities.RefreshToken;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.exceptions.InvalidPasswordException;
import backend.academy.diplom.exceptions.InvalidPasswordResetToken;
import backend.academy.diplom.exceptions.RefreshTokenExpireException;
import backend.academy.diplom.exceptions.UserNotFoundException;
import backend.academy.diplom.repositories.auth.PasswordResetTokenRepository;
import backend.academy.diplom.repositories.auth.RefreshTokenRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.CreateAccessToken;
import backend.academy.diplom.utils.PasswordUtils;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final CreateAccessToken createAccessToken;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthTokenFilter authTokenFilter;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EntityManager entityManager;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Transactional
    public void saveUser(User user) throws InterruptedException {
        String generatedPassword = passwordUtils.generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        user.setPassword(encodedPassword);
        userRepository.createUser(user);

        CompletableFuture.runAsync(() -> emailService.sendPasswordEmail(user.getEmail(),
                generatedPassword, user.getPhoneNumber()));
        Thread.sleep(2000);
    }

    @Transactional
    public RefreshResponse signIn(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = createAccessToken.generateJwtToken(loginRequest.email());

            User user = userRepository.findByEmail(loginRequest.email()).getFirst();

            refreshTokenRepository.deleteByUserId(user.getId());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

            return new RefreshResponse(accessToken, refreshToken.getToken());
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidPasswordException("Неверный логин или пароль");
        }
    }

//    @Transactional
//    public void logout(HttpServletRequest httpServletRequest) {
//        String jwt = authTokenFilter.parseJwt(httpServletRequest);
//        String email = createAccessToken.getEmailFromJwtToken(jwt);
//        userRepository.deleteTokenByUserMail(email);
//    }

    @Transactional
    public void requestReset(String email) throws InterruptedException {
        List<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new UserNotFoundException("");
        }
        User user = userByEmail.getFirst();

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setUserId(user.getId());
        passwordResetToken.setExpireDate(Instant.now().plusMillis(jwtExpirationMs));
        passwordResetToken.setToken(UUID.randomUUID().toString());

        passwordResetTokenRepository.deleteTokenByEmail(email);
        passwordResetTokenRepository.saveToken(passwordResetToken);


        CompletableFuture.runAsync(() ->
                emailService.sendResetPasswordToken(user.getEmail(), passwordResetToken.getToken()));
        Thread.sleep(2500);
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        List<PasswordResetToken> token = passwordResetTokenRepository.getTokenByToken(resetPasswordDTO.token());
        if (token.isEmpty()) {
            throw new InvalidPasswordResetToken("");
        }
        PasswordResetToken extractedToken = token.getFirst();

        if (extractedToken.isExpire()) {
            passwordResetTokenRepository.deleteByToken(extractedToken);
            entityManager.flush();
            throw new RefreshTokenExpireException("");
        }

        User user = userRepository.findById(extractedToken.getUserId()).getFirst();
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
        userRepository.updatePassword(user);
        passwordResetTokenRepository.deleteByToken(extractedToken);
    }
}
