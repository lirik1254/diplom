package backend.academy.diplom.users;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import backend.academy.diplom.DTO.auth.ResetPasswordDTO;
import backend.academy.diplom.entities.PasswordResetToken;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.exceptions.InvalidPasswordResetToken;
import backend.academy.diplom.exceptions.RefreshTokenExpireException;
import backend.academy.diplom.repositories.auth.PasswordResetTokenRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.services.auth.UserService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;

@DirtiesContext
class UserServiceTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenTokenNotFound_thenThrowsInvalidPasswordResetToken() {
        when(passwordResetTokenRepository.getTokenByToken("missing")).thenReturn(List.of());
        ResetPasswordDTO dto = new ResetPasswordDTO("missing", "newPass");
        assertThrows(InvalidPasswordResetToken.class, () -> userService.resetPassword(dto));
        verify(passwordResetTokenRepository, never()).deleteByToken(any());
    }

    @Test
    void whenTokenExpired_thenDeletesTokenAndThrowsExpiry() {
        PasswordResetToken expired = new PasswordResetToken();
        expired.setToken("t");
        expired.setExpireDate(Instant.now().minusSeconds(60)); // поле expireDate
        expired.setUserId(5L);

        when(passwordResetTokenRepository.getTokenByToken("t")).thenReturn(List.of(expired));
        ResetPasswordDTO dto = new ResetPasswordDTO("p", "t");

        assertThrows(RefreshTokenExpireException.class, () -> userService.resetPassword(dto));
        verify(passwordResetTokenRepository).deleteByToken(expired);
        verify(entityManager).flush();
        verifyNoInteractions(userRepository);
    }

    @Test
    void whenTokenValid_thenUpdatesPasswordAndDeletesToken() {
        PasswordResetToken valid = new PasswordResetToken();
        valid.setToken("ok");
        valid.setExpireDate(Instant.now().plusSeconds(3600)); // не просрочен
        valid.setUserId(7L);

        User user = new User();
        user.setId(7L);
        user.setPassword("oldHash");

        when(passwordResetTokenRepository.getTokenByToken("newSecret")).thenReturn(List.of(valid));
        when(userRepository.findById(7L)).thenReturn(List.of(user));
        when(passwordEncoder.encode("ok")).thenReturn("newHash");

        ResetPasswordDTO dto = new ResetPasswordDTO("ok", "newSecret");
        userService.resetPassword(dto);

        assertEquals("newHash", user.getPassword());

        InOrder inOrder = inOrder(passwordResetTokenRepository, userRepository, passwordEncoder);
        inOrder.verify(passwordResetTokenRepository).getTokenByToken("newSecret");
        inOrder.verify(userRepository).findById(7L);
        inOrder.verify(passwordEncoder).encode("ok");
        inOrder.verify(userRepository).updatePassword(user);
        inOrder.verify(passwordResetTokenRepository).deleteByToken(valid);
    }
}

