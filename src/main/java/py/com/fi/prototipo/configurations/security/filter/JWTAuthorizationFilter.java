package py.com.fi.prototipo.configurations.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import py.com.fi.prototipo.configurations.security.dtos.TokenDTO;
import py.com.fi.prototipo.configurations.security.service.JWTService;
import py.com.fi.prototipo.exceptions.ErrorCode;
import py.com.fi.prototipo.exceptions.ServiceException;
import py.com.fi.prototipo.utils.Constants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String header = request.getHeader(Constants.HEADER_STRING);

            if (!requiresAuthentication(header)) {
                chain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = null;

            //if (jwtService.validateToken(header)) {
            if (jwtService.validateToken(header)) {
                authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null,
                        jwtService.getRoles(header));
            }
            TokenDTO tokenDTO = jwtService.getToken(header);
            //TokenDTO tokenDTO = jwtService.getTokenByUsername(authentication.getName());
            if (tokenDTO != null) {
                // Si el campo valid es true y expiration menor a la fecha y hora actual se refresca el token
                if (tokenDTO.getValid() && tokenDTO.getExpiration().compareTo(new Date()) > 0) {
                    jwtService.refrescarToken(tokenDTO);
                } else {
                    //jwtService.deleteTokenByUUID(tokenDTO.getUuid());
                    throw new ServiceException(ErrorCode.FORBIDDEN);
                }
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException ex) {

            // Se obtiene tokenDTO de la tabla core.tokens de wally-backoffice
            //TokenDTO tokenDTO = jwtService.getTokenByUsername(ex.getClaims().getSubject());
            TokenDTO tokenDTO = jwtService.getTokenByUUID(String.valueOf(ex.getClaims().get("uuid")).replaceAll("\"",""));
            if (tokenDTO != null) {
                // Si el campo valid es true y expiration menor a la fecha y hora actual se refresca el token
                if (tokenDTO.getValid() && tokenDTO.getExpiration().compareTo(new Date()) > 0) {
                    jwtService.refrescarToken(tokenDTO);
                    allowForRefreshToken(ex, request);
                } else {
                    // Eliminar token expirado o invalido
                    //jwtService.deleteTokenByUUID(ex.getClaims().get("uuid").toString().replace("\"", ""));
                    request.setAttribute("exception", ex);
                    //throw new ServiceException(ErrorCode.FORBIDDEN);
                }
            }
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        chain.doFilter(request, response);
    }

    protected boolean requiresAuthentication(String header) {

        if (header == null || !header.startsWith(Constants.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWTnew AuthenticationResponse(
        request.setAttribute("claims", ex.getClaims());

    }

}

