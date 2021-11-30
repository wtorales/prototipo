package py.com.fi.prototipo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import py.com.fi.prototipo.dtos.health.ComposedHealthCheckStatus;
import py.com.fi.prototipo.services.IHealthCheckManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase utilizada para manejo de las peticiones realizadas por kubernetes
 * @author wtorales
 * @fecha 30/11/2021
 */
@RestController
@RequiredArgsConstructor
public class InitController {

    private final IHealthCheckManager healthCheckManager;


    @RequestMapping(value = "/health", method = RequestMethod
            .GET)
    public ResponseEntity<ComposedHealthCheckStatus> get()  {

        ComposedHealthCheckStatus check = healthCheckManager.check();
        return ResponseEntity.ok(check);
    }

    @RequestMapping(value = "/healthSinBD", method = RequestMethod
            .GET)
    public ResponseEntity<Map<String, Object>> getSinDb()  {

        //ComposedHealthCheckStatus check = healthCheckManager.check();
        Map<String, Object> re = new HashMap<>();
        re.put("status", "OK");
        return ResponseEntity.ok(re);
    }

}

