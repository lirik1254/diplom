package backend.academy.diplom.controllers;


import backend.academy.diplom.DTO.profile.*;
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

    @GetMapping("/profile-preview")
    public ProfilePreviewDTO getProfilePreview(@RequestHeader("Authorization") String authHeader) {
        return profileService.getProfilePreview(authHeader);
    }

    @GetMapping("/profile-info")
    public ProfileInfoDTO getProfileInfo(@RequestHeader("Authorization") String authHeader) {
        return profileService.getProfileInfo(authHeader);
    }

    @PostMapping("/upload-profile")
    public void uploadProfile(@RequestHeader("Authorization") String authHeader,
                              @RequestBody UploadProfileDTO uploadProfileDTO) {
        profileService.updateProfile(authHeader, uploadProfileDTO);
    }

    @GetMapping("/profile-setting-info")
    public ProfileSettingInfoDTO getProfileSettingInfo(@RequestHeader("Authorization") String authHeader) {
        return profileService.getProfileSettingInfo(authHeader);
    }

    @PostMapping("/upload-profile-setting")
    public void uploadProfileSetting(@RequestBody ProfileSettingInfoDTO profileSettingInfoDTO,
                                     @RequestHeader("Authorization") String authHeader) {
        profileService.uploadProfileSetting(profileSettingInfoDTO, authHeader);
    }
}
