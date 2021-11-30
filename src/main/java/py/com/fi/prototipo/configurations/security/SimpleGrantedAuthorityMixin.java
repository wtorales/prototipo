package py.com.fi.prototipo.configurations.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase alterna utilizada en la configuracion de spring
 * @author wtorales
 * @fecha 30/11/2021
 */
public abstract class SimpleGrantedAuthorityMixin {
    @JsonCreator
    public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {}
}
