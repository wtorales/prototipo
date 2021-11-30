package py.com.fi.prototipo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wtorales
 * @fecha 29/11/2021
 */

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class PrototipoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PrototipoApplication.class, args);
	}

}
