package car.selling.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayApplication {

	@Value("${notifications.api.url}")
	private String notificationsApiUrl;

	@Value("${pruebas.api.url}")
	private String pruebasApiUrl;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(p -> p
				.path("/pruebas/**")
				.uri(pruebasApiUrl))
			.route( p-> p
				.path("/incidencias/**")
				.uri(pruebasApiUrl)
			)
			.route( p-> p
				.path("/vehiculos/**")
				.uri(pruebasApiUrl)
			)
			.route( p-> p
				.path("/interesados/**")
				.uri(pruebasApiUrl)
			)
			.route( p-> p
				.path("/modelos/**")
				.uri(pruebasApiUrl)
			)
			.route( p-> p
				.path("/empleados/**")
				.uri(pruebasApiUrl)
			)
			.route(p -> p
				.path("/auth/**")
				.uri(pruebasApiUrl))
			.route(p -> p
				.path("/notificaciones/**")
				.uri(notificationsApiUrl))
			.build();
	}

}
