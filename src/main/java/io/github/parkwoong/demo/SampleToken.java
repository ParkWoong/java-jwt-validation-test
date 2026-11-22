package io.github.parkwoong.demo;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.*;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
public class SampleToken {
 public static String sign(SecretKey key, Instant expires) throws JOSEException {
  var token = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), new JWTClaimsSet.Builder()
   .issuer("demo-issuer").subject("sample-user").audience("demo-api").expirationTime(Date.from(expires)).build());
  token.sign(new MACSigner(key));
  return token.serialize();
 }
}
