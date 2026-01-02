package mo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "mo.demo")
public class MoDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoDemoApplication.class, args);
	}

}
