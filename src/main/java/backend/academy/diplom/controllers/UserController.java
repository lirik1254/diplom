package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.auth.*;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.services.auth.RefreshTokenService;
import backend.academy.diplom.services.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PostMapping("/sign-in")
    public RefreshResponse loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.signIn(loginRequest);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestBody RefreshRequest refreshRequest) {
        return refreshTokenService.refresh(refreshRequest);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        userService.logout(request);
    }

    @PostMapping("/request-reset")
    public void requestReset(@RequestBody MailDTO mailDTO) {
        userService.requestReset(mailDTO.email());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
    }

    @GetMapping("/check")
    public void test() {}
}
