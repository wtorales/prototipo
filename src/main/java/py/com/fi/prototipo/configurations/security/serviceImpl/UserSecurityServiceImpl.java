package py.com.fi.prototipo.configurations.security.serviceImpl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import py.com.fi.prototipo.configurations.security.dtos.AtributosBasicos;
import py.com.fi.prototipo.configurations.security.dtos.PerfilSecurityDTO;
import py.com.fi.prototipo.configurations.security.dtos.RolSecurityDTO;
import py.com.fi.prototipo.configurations.security.dtos.UserSecurityDTO;
import py.com.fi.prototipo.configurations.security.service.UserSecurityService;
import py.com.fi.prototipo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Clase donde obtenemos la informacion del usuario a loguear
 * @author wtorales
 * @fecha 29/11/2021
 */

@Service
@Slf4j
public class UserSecurityServiceImpl implements UserSecurityService {

    /**
     * Funcion en la cual obtenemos los datos del usuario verificando en las bases definidas para el caso
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     * @see UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // UserSecurityDTO user = userMapper.userByNameNew(username);
        UserSecurityDTO user = null;
        log.info("Metodo: loadUserByUsername()");
        log.info("Parametro: "+username);
        if (CommonUtils.nonEmpty(username)) {
            if (username.equals("admin"))
                user = new UserSecurityDTO();
                user.setUser("admin");
                user.setPass("123");

        }


        if(user == null) {
            log.info("El usuario "+username+ " no existe");
            throw new UsernameNotFoundException("El usuario " + username + " no existe");
        }


        log.info("Obtenemos los roles del usuario");
        //List<RolSecurityDTO> roles = this.rolDAO.buscarPorUsuarioId(username);
        List<RolSecurityDTO> roles = new ArrayList<>();
        AtributosBasicos atr1 = new AtributosBasicos("0","PERFIL_1","Perfil prueba",true);
        PerfilSecurityDTO pef = new PerfilSecurityDTO("F_ACCESO_WEB,F_PORTAL");
        pef.setId(atr1.getId());
        pef.setNombre(atr1.getNombre());
        pef.setDescripcion(atr1.getDescripcion());
        pef.setActivo(atr1.isActivo());

        List<PerfilSecurityDTO> atrlis = new ArrayList<>();
        atrlis.add(pef);

        AtributosBasicos rol1 = new AtributosBasicos("0","ROL_1","Rol prueba",true);
        RolSecurityDTO ro1 = new RolSecurityDTO(atrlis);
        ro1.setId(rol1.getId());
        ro1.setNombre(rol1.getNombre());
        ro1.setDescripcion(rol1.getDescripcion());
        ro1.setActivo(rol1.isActivo());

        roles.add(ro1);
        return buildUser(user, buildAuthorities(username, roles));

    }

    /**
     * Funcion donde se mapea la clase User de spring security y la clase generada para manejo del usuario
     * @param user (UserSecurityDTO)
     * @param authorities (List<GrantedAuthority>)
     * @return UserSecurityDTO
     */
    @Override
    public UserSecurityDTO buildUser(UserSecurityDTO user, List<GrantedAuthority> authorities) {
        UserSecurityDTO nuevo = new UserSecurityDTO(user.getUser(), user.getPass(), true, true, true, true, authorities);
        nuevo.setUser(user.getUser());
        return nuevo;
    }

    /**
     * Funcion que realiza el mapeo con los atributos de spring para que la libreria pueda manejar los roles
     * @param username (String)
     * @param userRoles (List<RolSecurityDTO>)
     * @return List<GrantedAuthority>
     * @throws UsernameNotFoundException
     */
    @Override
    public List<GrantedAuthority> buildAuthorities(String username, List<RolSecurityDTO> userRoles) throws UsernameNotFoundException {
        if (CommonUtils.isEmpty(userRoles) || userRoles.size() == 0) {
            log.info("El usuario "+username+" no tiene roles asignados");
            throw new UsernameNotFoundException("El usuario " + username + " no tiene roles asignados");
        }

        List<GrantedAuthority> auths = new ArrayList<>();
        for (RolSecurityDTO userRole : userRoles){

            log.info("Usuario: {}, Rol: {} - ", username, userRole.getId(), userRole.getNombre());

            for (PerfilSecurityDTO perfil : userRole.getPerfiles()) {
                if (perfil.getFuncionalidades() != null) {
                    JsonObject jsonObject = JsonParser.parseString(perfil.getFuncionalidades()).getAsJsonObject();
                    Set<String> da = JsonParser.parseString(perfil.getFuncionalidades()).getAsJsonObject().keySet();
                    for (String key : da) {
                        String dato = jsonObject.get(key).getAsString();
                        auths.add(new SimpleGrantedAuthority(dato));
                    }
                }
            }
        }

        return auths;
    }
}
