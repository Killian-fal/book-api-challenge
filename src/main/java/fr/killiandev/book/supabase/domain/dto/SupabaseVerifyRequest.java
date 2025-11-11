package fr.killiandev.book.supabase.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupabaseVerifyRequest {

    @NotNull
    private String phone;

    @NotNull
    private String token;

    @NotNull
    private String type;
}
