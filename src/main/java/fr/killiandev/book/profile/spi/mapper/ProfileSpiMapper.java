package fr.killiandev.book.profile.spi.mapper;

import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.profile.domain.dto.GetProfileDto;
import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileSpiMapper {

    ProfileSpiMapper INSTANCE = Mappers.getMapper(ProfileSpiMapper.class);

    GuideProfileDto ofGuide(GetProfileDto profile);

    SupabaseProfileDto ofSupabase(GetProfileDto profile);
}
