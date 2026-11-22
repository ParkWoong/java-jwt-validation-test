package io.github.parkwoong.demo.spec.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenUtils {

    private static String getAlgorithm(String tokenString) {

        if (tokenString == null || "".equals(tokenString)) return null;

        String algorithm = null;
        try {

            final String[] tokens = tokenString.split("\\.");

            if (tokens.length == 3) {

                final String headerString = new String(Base64.getUrlDecoder().decode(tokens[0]));

                final Map<String, Object> header = JsonUtils.getDataMapFromJsonString(headerString);

                algorithm = (String) header.get("alg");
            } else log.warn("Token processing failed or input is invalid");

        } catch (Exception e) {

            log.error("Token processing failed or input is invalid");
        }

        return algorithm;
    }

    public static Claims parseJWTClaims(String token, String rsaPubKey, String hs256Secret) {

        if (token == null
                || "".equals(token)
                || rsaPubKey == null
                || "".equals(rsaPubKey)
                || hs256Secret == null
                || "".equals(hs256Secret)) {

            log.warn("Token processing failed or input is invalid");
            return null;
        }

        token = token.replace("Bearer", "").replaceAll(" ", "");

        final String algorithm = getAlgorithm(token);

        if (algorithm == null) {

            log.warn("Token processing failed or input is invalid");
            return null;
        }

        Claims claims = null;
        try {

            log.debug("algorithm type: {}", algorithm);
            if ("HS256".equals(algorithm)) {

                final byte[] decoded = Base64.getUrlDecoder().decode(hs256Secret);

                claims = Jwts.parser().setSigningKey(decoded).parseClaimsJws(token).getBody();
            } else {

                final byte[] decoded = Base64.getDecoder().decode(rsaPubKey);
                final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                final RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

                claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
            }

        } catch (ExpiredJwtException e) {

            claims = e.getClaims();
            claims.put("expired", true);

            log.warn("Token processing failed or input is invalid");

        } catch (Exception e) {

            log.error("Token processing failed or input is invalid");
        }

        return claims;
    }

    public static boolean isNotExpired(Claims claims, Date pivot) {

        return claims != null && !claims.isEmpty() && claims.getExpiration().after(pivot);
    }

    public static boolean isValidType(Claims claims, String tokenType) {

        if (claims == null || claims.isEmpty() || tokenType == null || "".equals(tokenType)) {

            log.warn("Token processing failed or input is invalid");
            return false;
        }

        final String typ = (String) claims.get("typ");

        if (!tokenType.equals(typ)) {

            log.warn("Token processing failed or input is invalid");
            return false;
        }

        return true;
    }

    public static String decodePayload(String token) {

        if (token == null || "".equals(token)) {

            log.warn("Token processing failed or input is invalid");
            return null;
        }

        final String[] splited = token.split("\\.");

        if (splited.length != 3) {

            log.warn("Token processing failed or input is invalid");
            return null;
        }

        String decoded = null;
        try {

            decoded = new String(Base64.getUrlDecoder().decode(splited[1]));

        } catch (Exception e) {

            log.warn("Token processing failed or input is invalid");
        }
        return decoded;
    }

    public static String[] getUserInfo(String jsonString) {

        if (jsonString == null || "".equals(jsonString)) {

            log.warn("Token processing failed or input is invalid");
            return null;
        }

        String[] info = null;
        try {

            final JSONObject json = JsonUtils.getJson(jsonString);

            final String userId = json.getString("preferred_username");
            final String clientId = json.getString("azp");

            info = new String[] {userId, clientId};

        } catch (Exception e) {

            log.warn("Token processing failed or input is invalid");
        }

        return info;
    }
}
