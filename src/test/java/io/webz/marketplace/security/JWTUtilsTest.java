package io.webz.marketplace.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JWTUtilsTest {

    private JWTUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JWTUtils();

        Field privateKeyField = JWTUtils.class.getDeclaredField("privateKeyBase64");
        privateKeyField.setAccessible(true);
        privateKeyField.set(jwtUtils, "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCCHvnTQBfsDjAPEDejdn7iydNMBVHt3+0vgPAVuwYXNGA==");

        Field publicKeyField = JWTUtils.class.getDeclaredField("publicKeyBase64");
        publicKeyField.setAccessible(true);
        publicKeyField.set(jwtUtils, "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEfq4CMjq/TR7CZuyUFl7u2fB9CX8ZdWgYKznlnaBj8Fj5GlRMbLt3ipQKSxiFjroSHritjb121YKT9VTn5V2GoA==");

        jwtUtils.init();
    }


    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("PUBLISHER")
                .build();

        String token = jwtUtils.generateToken(userDetails);
        boolean isValid = jwtUtils.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }


    @Test
    void testInvalidToken() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("PUBLISHER")
                .build();

        String token = jwtUtils.generateToken(userDetails);

        UserDetails invalidUserDetails = User.builder()
                .username("invalidUser")
                .password("password")
                .roles("PUBLISHER")
                .build();

        boolean isValid = jwtUtils.isTokenValid(token, invalidUserDetails);
        assertFalse(isValid);
    }

}
