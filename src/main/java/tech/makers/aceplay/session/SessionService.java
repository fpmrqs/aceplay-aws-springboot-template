package tech.makers.aceplay.session;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
  public static final String TOKEN_PREFIX = "Bearer ";

  @Autowired private AuthenticationProvider authenticationManager;

  public String login(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
      return generateToken(username);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  public String generateToken(String username) {
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    String token =
        Jwts.builder()
            .setSubject(username)
            .claim("authorities", grantedAuthorities)
            .signWith(SessionSecret.getKey())
            .compact();

    return TOKEN_PREFIX + token;
  }
}
