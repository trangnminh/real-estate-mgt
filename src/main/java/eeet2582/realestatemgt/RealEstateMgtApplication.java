package eeet2582.realestatemgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RealEstateMgtApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateMgtApplication.class, args);
	}

}
