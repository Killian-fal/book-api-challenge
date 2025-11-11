package fr.killiandev.book.supabase.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SupabaseConstant {

    public static final String SUPABASE_OTP_URL = "%s/auth/v1/otp";

    public static final String SUPABASE_VERIFY_OTP_URL = "%s/auth/v1/verify";

    public static final String SUPABASE_AUTHENTICATE = "%s/auth/v1/token?grant_type=password";

    public static final String SUPABASE_REFRESH = "%s/auth/v1/token?grant_type=refresh_token";
}
