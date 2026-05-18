package cl.duoc.msPago2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPago2Application {

	public static void main(String[] args) {
		SpringApplication.run(MsPago2Application.class, args);
	}

}
