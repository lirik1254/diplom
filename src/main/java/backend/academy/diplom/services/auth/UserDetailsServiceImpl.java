package backend.academy.diplom.services.auth;

import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).getFirst();
        if (user != null) {
            return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());
        }
        throw new RuntimeException("Не найден пользователь");
    }
}
