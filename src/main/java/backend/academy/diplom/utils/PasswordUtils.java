package backend.academy.diplom.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordUtils {
    public String generateRandomPassword(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes).substring(0, length);
    }
}
