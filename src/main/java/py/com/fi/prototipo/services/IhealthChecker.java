package py.com.fi.prototipo.services;

import py.com.fi.prototipo.dtos.health.HealthCheckDTO;

/**
 * Interfaz para implementar la comprobación de un recurso en particular
 * @author wtorales
 * @fecha 30/11/2021
 */
interface IHealthChecker {
    /**
     * Realiza el chequeo del servicio y retorna la informacion que indica si
     * este servicio en particular funciona o no
     */
    public HealthCheckDTO.HEALTH_STATUS check();

    public String getComponentName();
}

