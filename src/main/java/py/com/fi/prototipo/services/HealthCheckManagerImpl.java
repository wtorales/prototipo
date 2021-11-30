package py.com.fi.prototipo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import py.com.fi.prototipo.dtos.health.ComposedHealthCheckStatus;
import py.com.fi.prototipo.dtos.health.HealthCheckDTO;

import java.util.Map;
import java.util.Set;

/**
 * Created by danicricco on 7/10/18.
 * @author wtorales
 * @fecha 30/11/2021
 */
@Component
public class HealthCheckManagerImpl implements IHealthCheckManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckManagerImpl.class);

    private final ApplicationContext context;

    public HealthCheckManagerImpl(ApplicationContext pContext) {
        context = pContext;
    }

    @Override
    public ComposedHealthCheckStatus check() {
        LOGGER.trace("Checking component status");
        //1) Lista todos los beans del tipo IHealthChecker
        Map<String, IHealthChecker> maps = context.getBeansOfType(IHealthChecker.class);
        ComposedHealthCheckStatus composedStatus = new
                ComposedHealthCheckStatus();
        ComposedHealthCheckStatus.COMPOSED_HEALTH_STATUS globalStatus =
                ComposedHealthCheckStatus
                        .COMPOSED_HEALTH_STATUS.OK;

        Set<String> keys = maps.keySet();
        StopWatch globalTime = new StopWatch();
        globalTime.start();
        for (String key : keys) {
            IHealthChecker component = maps.get(key);
            LOGGER.trace("Checking component {} ", component.getComponentName
                    ());
            //Comprueba un componente en particular
            HealthCheckDTO cs = checkService(component);

            if (!HealthCheckDTO.HEALTH_STATUS.UP.equals(cs
                    .getStatus())) {
                globalStatus = ComposedHealthCheckStatus.COMPOSED_HEALTH_STATUS.FAIL;
            }

            LOGGER.debug("Component {} is : {} . Checked in: {} ms", component.getComponentName(),
                    cs.getStatus(), cs.getElapsedTime());
            composedStatus.addComponentStatus(cs);
        }
        globalTime.stop();
        composedStatus.setTotalElapsedTime(globalTime.getTotalTimeMillis());
        composedStatus.setStatus(globalStatus);
        return composedStatus;
    }

    /**
     * Comprueba un componente, computa el tiempo que tomo y devuelve el
     * objeto que resume la ejeucion
     *
     * @param pComponent
     * @return
     */
    private HealthCheckDTO checkService(IHealthChecker pComponent) {
        StopWatch componentTime = new StopWatch();
        componentTime.start();
        HealthCheckDTO cs = new HealthCheckDTO(pComponent.getComponentName());
        try {
            HealthCheckDTO.HEALTH_STATUS status = pComponent.check();
            cs.setStatus(status);
        } catch (Throwable e) {
            //Cualquier error que ocurra en el driver se declara como fallido
            cs.setLastErrorMessage(e.getMessage());
            cs.setLastException(e.getClass().getName());
            cs.setStatus(HealthCheckDTO.HEALTH_STATUS.DOWN);
            LOGGER.debug("Component {} is : {} . Error Message: {}", pComponent.getComponentName(),
                    cs.getStatus(), cs.getLastErrorMessage());
        }

        componentTime.stop();
        cs.setElapsedTime(componentTime.getTotalTimeMillis());
        return cs;
    }
}
