package fr.killiandev.book.profile.domain;

import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdateProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdatedProfileDto;
import fr.killiandev.book.profile.domain.dto.UploadedAvatarDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Upload or update an avatar for the authenticated user")
    @PostMapping("/avatar")
    public UploadedAvatarDto uploadAvatar(Authentication authentication, @RequestParam("file") MultipartFile file) {
        log.info("Received avatar upload request for user: {}", authentication.getName());
        return profileService.uploadAvatar(authentication.getName(), file);
    }

    @Operation(summary = "Update the profile of the authenticated user")
    @PutMapping("/")
    public UpdatedProfileDto updateProfile(
            Authentication authentication, @Valid @RequestBody UpdateProfileDto updateProfileDto) {
        log.info("Received profile update request for user: {}", authentication.getName());
        return profileService.updateProfile(authentication.getName(), updateProfileDto);
    }

    @Operation(summary = "Get the profile of the authenticated user")
    @GetMapping("/")
    public GetProfileDto getProfile(Authentication authentication) {
        log.info("Received profile get request for user: {}", authentication.getName());
        return profileService.getProfile(authentication.getName());
    }
}
