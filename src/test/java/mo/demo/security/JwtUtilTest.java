package mo.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKeyForJwtTokenGeneration123456789");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400L);
    }

    @Test
    void testGenerateToken() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void testExtractUsername() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();

        String token = jwtUtil.generateToken(userDetails);
        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    void testValidateToken_Valid() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();

        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Invalid() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();

        String invalidToken = "invalid.token.here";
        boolean isValid = jwtUtil.validateToken(invalidToken, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testExtractExpiration() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();

        String token = jwtUtil.generateToken(userDetails);
        java.util.Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new java.util.Date()));
    }
}
