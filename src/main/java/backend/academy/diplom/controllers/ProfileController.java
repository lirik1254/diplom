package backend.academy.diplom.controllers;


import backend.academy.diplom.DTO.profile.ProfileData;
import backend.academy.diplom.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Map<String, String> fields
    ) throws IOException {
        profileService.upload(authHeader, fields);

        return ResponseEntity.ok("Профиль обновлен");
    }

    @GetMapping(value = "/get")
    public ProfileData getProfile(@RequestHeader("Authorization") String authHeader) throws IOException {
        return profileService.getProfile(authHeader);
    }
}
