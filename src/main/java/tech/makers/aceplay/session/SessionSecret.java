package tech.makers.aceplay.session;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Objects;

@Configuration
public class SessionSecret {
  private static String secretKeyBase64Encoded;
  private static String activeProfile;
  private static SecretKey generatedSecretKey;

  public SessionSecret(
      @Value("${spring.profiles.active:unknown}") String activeProfile,
      @Value("${jwt.token.secret:@null}") String secretKeyBase64Encoded) {
        this.activeProfile = activeProfile;
        this.secretKeyBase64Encoded = secretKeyBase64Encoded;
  }

  public static SecretKey getKey() {
    if (Objects.equals(activeProfile, "prod")) {
      if (secretKeyBase64Encoded == null) {
        throw new SessionSecretNotProvidedException();
      }
      byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64Encoded);
      return Keys.hmacShaKeyFor(keyBytes);
    } else {
      if (generatedSecretKey == null) {
        generatedSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
      }
      return generatedSecretKey;
    }
  }
}
