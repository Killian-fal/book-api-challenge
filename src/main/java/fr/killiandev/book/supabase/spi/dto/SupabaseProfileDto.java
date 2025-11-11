package fr.killiandev.book.supabase.spi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupabaseProfileDto {

    private Long id;

    private RoleType role;

    public enum RoleType {
        USER,
        ADMIN;

        public String asAuthority() {
            return "ROLE_" + name();
        }
    }
}
