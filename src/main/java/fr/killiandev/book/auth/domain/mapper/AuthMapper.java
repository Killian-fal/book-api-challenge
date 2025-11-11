package fr.killiandev.book.auth.domain.mapper;

import fr.killiandev.book.auth.domain.dto.AuthTokenResponseDto;
import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    @NotNull
    AuthTokenResponseDto of(@NotNull AccessTokenResponseDto response);
}
