package fr.killiandev.book.profile.domain;

import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdateProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdatedProfileDto;
import fr.killiandev.book.profile.domain.dto.UploadedAvatarDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UploadedAvatarDto uploadAvatar(String phoneNumber, MultipartFile file);

    UpdatedProfileDto updateProfile(String phoneNumber, UpdateProfileDto updateProfileDto);

    GetProfileDto getProfile(String phoneNumber);

    boolean profileExists(String phoneNumber);

    GetProfileDto createProfile(String phoneNumber);
}
