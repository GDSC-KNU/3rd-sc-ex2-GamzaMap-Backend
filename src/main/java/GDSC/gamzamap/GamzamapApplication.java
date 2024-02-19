package GDSC.gamzamap;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://gamzamap.store", description = "gamzamap")})
@SpringBootApplication
public class GamzamapApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamzamapApplication.class, args);
	}

}
