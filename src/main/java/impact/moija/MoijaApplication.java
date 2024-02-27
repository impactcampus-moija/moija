package impact.moija;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoijaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoijaApplication.class, args);
	}

}
