package io.github.parkwoong.demo;

import io.github.parkwoong.demo.spec.util.TokenUtils;
import io.jsonwebtoken.Claims;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class JwtController {
    @PostMapping("/api/jwt/validate")
    public Map<String, Object> validate(@RequestBody Input input) {
        Claims claims =
                TokenUtils.parseJWTClaims(input.token, input.rsaPublicKey, input.hs256Secret);
        boolean valid =
                claims != null
                        && claims.getExpiration() != null
                        && TokenUtils.isNotExpired(claims, new Date())
                        && !Boolean.TRUE.equals(claims.get("expired"));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("valid", valid);
        result.put("subject", valid ? claims.getSubject() : null);
        return result;
    }

    public static class Input {
        public String token;
        public String rsaPublicKey;
        public String hs256Secret;
    }
}
