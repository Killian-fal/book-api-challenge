package fr.killiandev.book.profile.domain.mapper;

import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.profile.domain.dto.UpdatedProfileDto;
import fr.killiandev.book.profile.domain.dto.UploadedAvatarDto;
import fr.killiandev.book.profile.domain.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    GetProfileDto of(Profile profile);

    UpdatedProfileDto ofUpdate(Profile profile);

    @Mapping(target = "profilePictureKey", source = "avatarId")
    UploadedAvatarDto of(String avatarId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Profile create(String phoneNumber);
}
