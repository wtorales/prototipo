package py.com.fi.prototipo.services;

import py.com.fi.prototipo.dtos.health.ComposedHealthCheckStatus;

/**
 * Comprueba todos los beans del tipo IHealthChecker
 * @author wtorales
 * @fecha 30/11/2021
 */
public interface IHealthCheckManager {
    public ComposedHealthCheckStatus check();
}
