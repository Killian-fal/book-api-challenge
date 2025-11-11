package fr.killiandev.book.supabase.spi.mapper;

import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import fr.killiandev.book.supabase.domain.dto.SupabaseAccessTokenResponseDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupabaseMapper {

    SupabaseMapper INSTANCE = Mappers.getMapper(SupabaseMapper.class);

    @NotNull
    AccessTokenResponseDto of(@NotNull SupabaseAccessTokenResponseDto supabaseAccessTokenResponseDto);
}
