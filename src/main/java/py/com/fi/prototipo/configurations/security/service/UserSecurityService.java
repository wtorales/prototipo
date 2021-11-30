package py.com.fi.prototipo.configurations.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import py.com.fi.prototipo.configurations.security.dtos.RolSecurityDTO;
import py.com.fi.prototipo.configurations.security.dtos.UserSecurityDTO;

import java.util.List;

/**
 * @author wtorales
 * @fecha 29/11/2021
 */
public interface UserSecurityService extends UserDetailsService {
    UserSecurityDTO buildUser(UserSecurityDTO user, List<GrantedAuthority> authorities);
    List<GrantedAuthority> buildAuthorities(String username, List<RolSecurityDTO> userRoles) throws UsernameNotFoundException;
}
