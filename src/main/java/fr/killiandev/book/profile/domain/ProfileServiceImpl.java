package fr.killiandev.book.profile.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.*;

import fr.killiandev.book.observability.api.exception.type.ProfileException;
import fr.killiandev.book.profile.domain.dao.ProfileDao;
import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdateProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdatedProfileDto;
import fr.killiandev.book.profile.domain.dto.UploadedAvatarDto;
import fr.killiandev.book.profile.domain.entity.Profile;
import fr.killiandev.book.profile.domain.mapper.ProfileMapper;
import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDao profileDao;
    private final ProfileStorageServiceSpi profileStorageServiceSpi;

    @NewSpan("ProfileService - uploadAvatar")
    @Override
    public UploadedAvatarDto uploadAvatar(String phoneNumber, MultipartFile file) {
        log.info("- Uploading avatar for user with phone number: {}", phoneNumber);
        Profile profile = profileDao.findByPhoneNumber(phoneNumber);

        try {
            String contentType = file.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ProfileException(PROFILE_INVALID_AVATAR_FILE_TYPE_ERROR, "contentType", contentType);
            }

            byte[] bytes = file.getBytes();
            log.info("- Uploading avatar with size {}", bytes.length);
            String avatarId =
                    profileStorageServiceSpi.storeProfileAvatar(String.valueOf(profile.getId()), bytes, contentType);

            profile.setProfilePicture(avatarId);
            profileDao.save(profile);

            return ProfileMapper.INSTANCE.of(avatarId);
        } catch (Exception e) {
            throw new ProfileException(PROFILE_INVALID_AVATAR_FILE_ERROR, e);
        }
    }

    @NewSpan("ProfileService - updateProfile")
    @Override
    public UpdatedProfileDto updateProfile(String phoneNumber, UpdateProfileDto updateProfileDto) {
        log.info("- Updating profile for user with phone number: {}", phoneNumber);
        Profile profile = profileDao.findByPhoneNumber(phoneNumber);

        //        profile.setEmail(updateProfileDto.getEmail()); TODO: OTP sendgrid
        profile.setFullName(updateProfileDto.getFullName());
        Profile updatedProfile = profileDao.save(profile);

        return ProfileMapper.INSTANCE.ofUpdate(updatedProfile);
    }

    @Override
    public GetProfileDto getProfile(String phoneNumber) {
        log.info("- Getting profile for user with phone number: {}", phoneNumber);
        return ProfileMapper.INSTANCE.of(profileDao.findByPhoneNumber(phoneNumber));
    }

    @Override
    public boolean profileExists(String phoneNumber) {
        log.info("- Check if profile exists for user with phone number: {}", phoneNumber);
        return profileDao.findByPhoneNumberOptional(phoneNumber).isPresent();
    }

    @NewSpan("ProfileService - createProfile")
    @Override
    public GetProfileDto createProfile(String phoneNumber) {
        log.info("- Creating profile for user with phone number: {}", phoneNumber);
        if (this.profileExists(phoneNumber)) {
            throw new ProfileException(PROFILE_ALREADY_EXISTS_ERROR, "phoneNumber", phoneNumber);
        }

        Profile profile = ProfileMapper.INSTANCE.create(phoneNumber);
        Profile savedProfile = profileDao.save(profile);
        return ProfileMapper.INSTANCE.of(savedProfile);
    }
}
