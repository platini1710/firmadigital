package cl.fonasa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	   @Override   
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(JwtDemoApplication.class);   }
}
