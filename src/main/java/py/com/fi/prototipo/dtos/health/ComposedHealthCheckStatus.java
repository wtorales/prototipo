package py.com.fi.prototipo.dtos.health;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  <p>
 *  * Informacion compuesta del estado de un servicio.
 *  * </p>
 *  * <p>
 *  * Como el servicio puede depender de varios componentes, el estado de
 *  * estos componentes está registrada en el mapa {@link #components}. Si todos
 *  * los servicios están arriba, entonces el servicio concluirá en OK.
 *  * </p>
 * @author wtorales
 * @fecha 30/11/2021
 */
public class ComposedHealthCheckStatus {

    public enum COMPOSED_HEALTH_STATUS {OK, FAIL};

    private COMPOSED_HEALTH_STATUS status;
    private Date testDate;

    private Map<String, HealthCheckDTO> components;
    private long totalElapsedTime;

    public ComposedHealthCheckStatus() {
        components = new HashMap<String, HealthCheckDTO>();
        testDate=new Date();
    }

    public COMPOSED_HEALTH_STATUS getStatus() {
        return status;
    }

    public void setStatus(COMPOSED_HEALTH_STATUS pStatus) {
        status = pStatus;
    }

    public Map<String, HealthCheckDTO> getComponents() {
        return components;
    }

    public void setComponents(Map<String, HealthCheckDTO> pComponents) {
        components = pComponents;
    }

    public void addComponentStatus(HealthCheckDTO dto) {
        components.put(dto.getComponentName(), dto);
    }

    public long getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public void setTotalElapsedTime(long pTotalElapsedTime) {
        totalElapsedTime = pTotalElapsedTime;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date pTestDate) {
        testDate = pTestDate;
    }
}
