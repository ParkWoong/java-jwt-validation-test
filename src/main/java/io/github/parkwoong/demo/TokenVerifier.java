package io.github.parkwoong.demo;

import java.security.interfaces.RSAPublicKey;
import javax.crypto.SecretKey;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.*;
import org.springframework.security.oauth2.core.*;
public class TokenVerifier {
 public static JwtDecoder hmac(SecretKey key) {
  return validate(NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build());
 }
 public static JwtDecoder rsa(RSAPublicKey key) {
  return validate(NimbusJwtDecoder.withPublicKey(key).signatureAlgorithm(SignatureAlgorithm.RS256).build());
 }
 private static JwtDecoder validate(NimbusJwtDecoder decoder) {
  decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefaultWithIssuer("demo-issuer"),
   jwt -> jwt.getExpiresAt() != null && jwt.getSubject() != null && !jwt.getSubject().isBlank() && jwt.getAudience().contains("demo-api")
    ? OAuth2TokenValidatorResult.success()
    : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token"))));
  return decoder;
 }
}
