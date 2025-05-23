package backend.academy.diplom.utils;

import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final CreateAccessToken createAccessToken;
    private final UserRepository userRepository;

    public User getUserByAuthHeader(String header) {
        String token = createAccessToken.getEmailFromJwtToken(header.substring(7));
        return userRepository.findByEmail(token).getFirst();
    }
}
