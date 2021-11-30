package py.com.fi.prototipo.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import py.com.fi.prototipo.configurations.security.dtos.TokenDTO;
import py.com.fi.prototipo.configurations.security.service.JWTService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
@Service
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    /*@Autowired
    private TokenDAO tokenDAO;*/

    @Autowired
    private JWTService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // TODO Auto-generated method stub
        try {
            String token = request.getHeader("user");
            if (token != null) {
                TokenDTO user = jwtService.getTokenByUsername(token);
                //int x = tokenDAO.deleteToken(user.getUuid());
                int x = 0;
                if (x != 0) {
                    log.info("Logout exitoso");
                } else {
                    log.error("No fue posible ejecutar el logout");
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

}

