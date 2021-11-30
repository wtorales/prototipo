package py.com.fi.prototipo.configurations.security.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import py.com.fi.prototipo.configurations.security.SimpleGrantedAuthorityMixin;
import py.com.fi.prototipo.configurations.security.dtos.TokenDTO;
import py.com.fi.prototipo.configurations.security.service.JWTService;
import py.com.fi.prototipo.utils.Constants;
import py.com.fi.prototipo.utils.TXUUIDGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
@Slf4j
@Service
public class JWTServiceImpl implements JWTService {

    private String accessTokenSecret;
    private long jwtExpirationDateInMs;
    private long refreshExpirationDateInMs;

    @Value("${jwt.accessTokenSecret}")
    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = Base64Utils.encodeToString(accessTokenSecret.getBytes());
    }

    @Value("${jwt.accessTokenExpirationInMilliseconds}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationDateInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshTokenExpirationInMilliseconds}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    /*@Autowired
    private TokenDAO tokenDAO;*/

    @Override
    public String createToken(Authentication authentication) throws IOException {

        String username = ((User) authentication.getPrincipal()).getUsername();
        /*String phone = ((UsuarioDTO) authentication.getPrincipal()).getTelefono();
        String status = ((UsuarioDTO) authentication.getPrincipal()).getEstado();
        String email = ((UsuarioDTO) authentication.getPrincipal()).getEmail();
        String id = (((UsuarioDTO) authentication.getPrincipal()).getUsername());*/
        String uuid = new TXUUIDGenerator().generate();

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        Claims accessTokenclaims = Jwts.claims();
        accessTokenclaims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        /*accessTokenclaims.put("phone", new ObjectMapper().writeValueAsString(phone));
        accessTokenclaims.put("status", new ObjectMapper().writeValueAsString(status));
        accessTokenclaims.put("email", new ObjectMapper().writeValueAsString(email));
        accessTokenclaims.put("id", new ObjectMapper().writeValueAsString(id));*/
        accessTokenclaims.put("uuid", new ObjectMapper().writeValueAsString(uuid));

        String token = Jwts.builder().setClaims(accessTokenclaims).setSubject(username)
                .signWith(SignatureAlgorithm.HS512, this.accessTokenSecret.getBytes()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationDateInMs)).compact();

        try {
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setUuid(uuid);
            tokenDTO.setExpiration((new Date(System.currentTimeMillis() + this.refreshExpirationDateInMs)));
            tokenDTO.setIssuedAt(new Date());
            tokenDTO.setRemoteIp("");
            tokenDTO.setTokenType(token);
            tokenDTO.setUserAgent("");
            tokenDTO.setUsername(username);
            tokenDTO.setSecurityProfileId("P5-Web");
            tokenDTO.setAppId("w_enterprise");
            tokenDTO.setValid(true);

            //TokenDTO previousToken = tokenDAO.tokenByUsername(username);
            TokenDTO previousToken = null;

            if (previousToken != null) {
                previousToken.setExpiration(previousToken.getIssuedAt());
                //tokenDAO.updateTokenForExpiration(previousToken);
            }
            //tokenDAO.insertToken(tokenDTO);

        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

        return token;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return (Claims) Jwts.parser().setSigningKey(this.accessTokenSecret.getBytes()).parseClaimsJws(resolve(token))
                .getBody();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {

        Object roles = getClaims(token).get("authorities");

        Collection<? extends GrantedAuthority> authorities = Arrays
                .asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

        return authorities;
    }

    @Override
    public String resolve(String token) {
        if (token != null && token.startsWith(Constants.TOKEN_PREFIX)) {
            return token.replace(Constants.TOKEN_PREFIX, "");
        }
        return null;
    }

    @Override
    public TokenDTO getTokenByUUID(String uuid) {
        try {
            //TokenDTO tokenDTO = tokenDAO.tokenByValue(uuid);
            TokenDTO tokenDTO = null;
            return tokenDTO;
        } catch (Exception e) {
            log.error("getTokenDTOByUuidException: {}", e.getMessage());
            return null;
        }

    }

    @Override
    public TokenDTO getTokenByUsername(String username) {
        try {
            //TokenDTO tokenDTO = tokenDAO.tokenByUsername(username);
            TokenDTO tokenDTO = null;
            return tokenDTO;
        } catch (Exception e) {
            log.error("getTokenDTOByUsernameException: {}", e.getMessage());
            return null;
        }

    }

    @Override
    public int deleteTokenByUUID(String uuid) {
        try {
            //tokenDAO.deleteToken(uuid);
            return 1;
        } catch (Exception e) {
            log.error("deleteTokenDTOByUuidException: {}", e.getMessage());
            return 0;
        }

    }

    @Override
    public int refrescarToken(TokenDTO token) {
        try {
            Calendar calen = Calendar.getInstance();
            calen.add(Calendar.MINUTE, (int) refreshExpirationDateInMs/ Constants.DIVISECON);
            token.setExpiration(calen.getTime());
            //tokenDAO.updateTokenForExpiration(token);
            return 1;
        } catch (Exception e) {
            log.error("refrescarToken: {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public TokenDTO getToken(String token) {
        try {
            String s = resolve(token);
            if (s != null) s = s.trim().replace(" ","");
            //TokenDTO tok = tokenDAO.getToken(s);
            TokenDTO tok = null; //implementar peticion
            return tok;
        } catch (Exception e) {
            log.error("refrescarToken: {}", e.getMessage());
            return null;
        }
    }

}

