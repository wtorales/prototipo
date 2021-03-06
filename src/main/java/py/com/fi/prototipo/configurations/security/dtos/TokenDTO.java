package py.com.fi.prototipo.configurations.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private Integer id;
    private String uuid;
    private Date expiration;
    private Date issuedAt;
    private String remoteIp;
    private String tokenType;
    private String userAgent;
    private String username;
    private String securityProfileId;
    private String appId;
    private Boolean valid;
}
