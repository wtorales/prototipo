package py.com.fi.prototipo.configurations.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import py.com.fi.prototipo.configurations.security.dtos.TokenDTO;

import java.io.IOException;
import java.util.Collection;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
public interface JWTService {
    String createToken(Authentication authentication) throws IOException;
    boolean validateToken(String token);
    Claims getClaims(String token);
    String getUsername(String token);
    Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
    String resolve(String token);
    TokenDTO getTokenByUUID(String uuid);
    TokenDTO getTokenByUsername(String username);
    int deleteTokenByUUID(String uuid);
    int refrescarToken(TokenDTO token);
    TokenDTO getToken(String token);
}
