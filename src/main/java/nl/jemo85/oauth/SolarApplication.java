package nl.jemo85.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication()
public class SolarApplication extends SpringBootServletInitializer{

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(SolarApplication.class);

  }

  public static void main(String[] args) throws Exception {
      SpringApplication.run(SolarApplication.class, args);
  }

}
