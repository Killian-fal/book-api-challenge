package fr.killiandev.book.supabase;

import static org.assertj.core.api.Assertions.assertThat;

import fr.killiandev.book.supabase.domain.filter.RateLimitFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RateLimitFilterTest {

    @Test
    void rejectsRequestsBeyondCapacity() throws ServletException, IOException {
        RateLimitFilter filter = new RateLimitFilter();
        AtomicInteger downstreamCount = new AtomicInteger();
        FilterChain chain = (req, res) -> {
            downstreamCount.incrementAndGet();
            ((HttpServletResponse) res).setStatus(204);
        };

        for (int i = 0; i < 100; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/auth/send");
            request.setRemoteAddr("127.0.0.1");
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, chain);
            assertThat(response.getStatus()).isEqualTo(204);
        }

        MockHttpServletRequest blockedRequest = new MockHttpServletRequest("GET", "/api/v1/auth/send");
        blockedRequest.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();

        filter.doFilter(blockedRequest, blockedResponse, chain);

        assertThat(blockedResponse.getStatus()).isEqualTo(429);
        assertThat(blockedResponse.getContentAsString()).isEqualTo("Too Many Requests");
        assertThat(downstreamCount.get()).isEqualTo(100);
    }
}
