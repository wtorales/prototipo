package py.com.fi.prototipo.endpoints;

/**
 * Clase donde configuramos los path de acuerdo al servicio proveido
 * @author wtorales
 * @fecha 29/11/2021
 */
public interface ApiPaths {
    final static String LOGIN = "/login";
    final static String EXAMPLE_BASE = "/example";

    final static String PRUEBA = EXAMPLE_BASE.concat("/nuevoEndpoint");
}
