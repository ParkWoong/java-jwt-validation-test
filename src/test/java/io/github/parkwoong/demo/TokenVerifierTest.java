package io.github.parkwoong.demo;

import org.junit.jupiter.api.Test;
import javax.crypto.KeyGenerator;
import java.time.Instant;
import java.security.KeyPairGenerator;
import java.security.interfaces.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
class TokenVerifierTest {
 @Test void hmacRejectsExpiredWrongKeyAndMalformed() throws Exception {
  var generator = KeyGenerator.getInstance("HmacSHA256"); generator.init(256);
  var key = generator.generateKey(); var verifier = TokenVerifier.hmac(key);
  assertEquals("sample-user", verifier.decode(SampleToken.sign(key, Instant.now().plusSeconds(60))).getSubject());
  assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> verifier.decode(SampleToken.sign(key, Instant.now().minusSeconds(120))));
  assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> verifier.decode(SampleToken.sign(generator.generateKey(), Instant.now().plusSeconds(60))));
  assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> verifier.decode("broken"));
 }
 @Test void rsaChecksSignatureAndRequiredClaims() throws Exception {
  var generator = KeyPairGenerator.getInstance("RSA"); generator.initialize(2048); var pair = generator.generateKeyPair();
  var verifier = TokenVerifier.rsa((RSAPublicKey) pair.getPublic());
  var claims = new JWTClaimsSet.Builder().issuer("demo-issuer").subject("sample-user").audience("demo-api").expirationTime(Date.from(Instant.now().plusSeconds(60))).build();
  var token = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
  token.sign(new RSASSASigner((RSAPrivateKey) pair.getPrivate()));
  assertEquals("sample-user", verifier.decode(token.serialize()).getSubject());
  var missing = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), new JWTClaimsSet.Builder().issuer("demo-issuer").build());
  missing.sign(new RSASSASigner((RSAPrivateKey) pair.getPrivate()));
  assertThrows(org.springframework.security.oauth2.jwt.JwtException.class, () -> verifier.decode(missing.serialize()));
 }
}
