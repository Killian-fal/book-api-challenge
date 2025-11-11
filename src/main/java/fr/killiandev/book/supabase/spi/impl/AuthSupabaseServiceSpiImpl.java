package fr.killiandev.book.supabase.spi.impl;

import fr.killiandev.book.auth.spi.AuthSupabaseServiceSpi;
import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.spi.mapper.SupabaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
public class AuthSupabaseServiceSpiImpl implements AuthSupabaseServiceSpi {

    private final SupabaseService supabaseService;

    @Override
    public HttpStatusCode otp(String phoneNumber) throws SupabaseException {
        return supabaseService.otp(phoneNumber);
    }

    @Override
    public AccessTokenResponseDto verifyOtp(String phoneNumber, String code) throws SupabaseException {
        return SupabaseMapper.INSTANCE.of(supabaseService.verifyOtp(phoneNumber, code));
    }

    @Override
    public AccessTokenResponseDto refresh(String refreshToken) throws SupabaseException {
        return SupabaseMapper.INSTANCE.of(supabaseService.refresh(refreshToken));
    }
}
