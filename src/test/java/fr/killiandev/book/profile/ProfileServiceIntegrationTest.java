package fr.killiandev.book.profile;

import static fr.killiandev.book.observability.api.exception.ExceptionType.PROFILE_ALREADY_EXISTS_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.PROFILE_INVALID_AVATAR_FILE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import fr.killiandev.book.observability.api.exception.type.ProfileException;
import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.profile.domain.configuration.ProfileConfiguration;
import fr.killiandev.book.profile.domain.dao.ProfileDaoImpl;
import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdateProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdatedProfileDto;
import fr.killiandev.book.profile.domain.dto.UploadedAvatarDto;
import fr.killiandev.book.profile.domain.entity.Profile;
import fr.killiandev.book.profile.domain.entity.RoleType;
import fr.killiandev.book.profile.domain.repository.ProfileRepository;
import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@Import({ProfileConfiguration.class, ProfileDaoImpl.class})
class ProfileServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @MockitoBean
    private ProfileStorageServiceSpi profileStorageServiceSpi;

    @BeforeEach
    void resetMocks() {
        reset(profileStorageServiceSpi);
    }

    @Test
    void uploadAvatarStoresReturnedKeyAndPersistsProfile() {
        Profile profile = persistProfile("+33600000000");
        MockMultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", "binary".getBytes());
        when(profileStorageServiceSpi.storeProfileAvatar(
                        eq(String.valueOf(profile.getId())), any(byte[].class), eq("image/png")))
                .thenReturn("avatar-key");

        UploadedAvatarDto dto = profileService.uploadAvatar(profile.getPhoneNumber(), file);

        assertThat(dto.getProfilePictureKey()).isEqualTo("avatar-key");
        Profile reloaded =
                profileRepository.findByPhoneNumber(profile.getPhoneNumber()).orElseThrow();
        assertThat(reloaded.getProfilePicture()).isEqualTo("avatar-key");

        ArgumentCaptor<byte[]> dataCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(profileStorageServiceSpi)
                .storeProfileAvatar(eq(String.valueOf(profile.getId())), dataCaptor.capture(), eq("image/png"));
        assertThat(new String(dataCaptor.getValue())).isEqualTo("binary");
    }

    @Test
    void uploadAvatarRejectsNonImageContent() {
        Profile profile = persistProfile("+33600000001");
        MockMultipartFile file = new MockMultipartFile("avatar", "test.pdf", "application/pdf", "pdf".getBytes());

        assertThatThrownBy(() -> profileService.uploadAvatar(profile.getPhoneNumber(), file))
                .isInstanceOf(ProfileException.class)
                .satisfies(ex -> assertThat(((ProfileException) ex).getExceptionType())
                        .isEqualTo(PROFILE_INVALID_AVATAR_FILE_ERROR));

        verifyNoInteractions(profileStorageServiceSpi);
        Profile reloaded =
                profileRepository.findByPhoneNumber(profile.getPhoneNumber()).orElseThrow();
        assertThat(reloaded.getProfilePicture()).isNull();
    }

    @Test
    void createProfilePreventsDuplicates() {
        String phone = "+33600000002";

        GetProfileDto first = profileService.createProfile(phone);
        assertThat(first.getPhoneNumber()).isEqualTo(phone);
        assertThat(first.getRole()).isEqualTo("USER");

        assertThatThrownBy(() -> profileService.createProfile(phone))
                .isInstanceOf(ProfileException.class)
                .satisfies(ex ->
                        assertThat(((ProfileException) ex).getExceptionType()).isEqualTo(PROFILE_ALREADY_EXISTS_ERROR));
    }

    @Test
    void updateProfileUpdatesFullNameOnly() {
        Profile profile = persistProfile("+33600000003");
        profile.setEmail("test@test.fr");
        profile.setFullName("test");
        profileRepository.save(profile);
        UpdatedProfileDto updated =
                profileService.updateProfile(profile.getPhoneNumber(), new UpdateProfileDto("Test testeeeeee"));

        assertThat(updated.getFullName()).isEqualTo("Test testeeeeee");
        Profile reloaded =
                profileRepository.findByPhoneNumber(profile.getPhoneNumber()).orElseThrow();
        assertThat(reloaded.getFullName()).isEqualTo("Test testeeeeee");
        assertThat(reloaded.getEmail()).isEqualTo("test@test.fr");
    }

    private Profile persistProfile(String phoneNumber) {
        Profile profile = new Profile();
        profile.setPhoneNumber(phoneNumber);
        profile.setEmail(null);
        profile.setFullName(null);
        profile.setProfilePicture(null);
        profile.setRole(RoleType.USER);
        return profileRepository.save(profile);
    }
}
