package fr.killiandev.book.supabase.spi;

import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;

public interface SupabaseProfileServiceSpi {

    SupabaseProfileDto getProfile(String phoneNumber);
}
