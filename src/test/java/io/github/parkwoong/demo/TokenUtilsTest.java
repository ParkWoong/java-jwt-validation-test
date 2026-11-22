package io.github.parkwoong.demo;

import static org.junit.jupiter.api.Assertions.*;

import io.github.parkwoong.demo.spec.util.TokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.junit.jupiter.api.Test;

import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.Date;

class TokenUtilsTest {
    @Test
    void originalHmacAndExpiredClaims() {
        byte[] key = new byte[32];
        new java.security.SecureRandom().nextBytes(key);
        String secret = Base64.getUrlEncoder().encodeToString(key);
        String token =
                Jwts.builder()
                        .claim("preferred_username", "sample-user")
                        .claim("azp", "sample-client")
                        .claim("typ", "Bearer")
                        .setExpiration(new Date(System.currentTimeMillis() + 60000))
                        .signWith(SignatureAlgorithm.HS256, key)
                        .compact();
        var claims = TokenUtils.parseJWTClaims(token, "unused-for-hmac", secret);
        assertNotNull(claims);
        assertTrue(TokenUtils.isValidType(claims, "Bearer"));
        assertTrue(TokenUtils.isNotExpired(claims, new Date()));
        assertArrayEquals(
                new String[] {"sample-user", "sample-client"},
                TokenUtils.getUserInfo(TokenUtils.decodePayload(token)));
        String expired =
                Jwts.builder()
                        .setSubject("sample-user")
                        .setExpiration(new Date(System.currentTimeMillis() - 120000))
                        .signWith(SignatureAlgorithm.HS256, key)
                        .compact();
        assertEquals(
                true, TokenUtils.parseJWTClaims(expired, "unused-for-hmac", secret).get("expired"));
        assertNull(TokenUtils.parseJWTClaims("broken", "unused-for-hmac", secret));
        byte[] wrong = new byte[32];
        new java.security.SecureRandom().nextBytes(wrong);
        assertNull(
                TokenUtils.parseJWTClaims(
                        token, "unused-for-hmac", Base64.getUrlEncoder().encodeToString(wrong)));
    }

    @Test
    void originalRsaBranch() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        var pair = generator.generateKeyPair();
        String token =
                Jwts.builder()
                        .setSubject("sample-user")
                        .setExpiration(new Date(System.currentTimeMillis() + 60000))
                        .signWith(SignatureAlgorithm.RS256, pair.getPrivate())
                        .compact();
        assertEquals(
                "sample-user",
                TokenUtils.parseJWTClaims(
                                token,
                                Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()),
                                "unused-for-rsa")
                        .getSubject());
    }
}
