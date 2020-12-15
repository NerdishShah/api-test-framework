package ai.saal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "classpath:resource.properties" })
@PropertySource(value = { "classpath:/${env:prod}.properties" })
public class ApplicationConfig {

	@Value("${base.uri}")
	private String baseUri;
	@Value("${resource.color.path}")
	private String colorPath;

	public String getBaseUri() {
		return this.baseUri;
	}

	public String getResource(String name) {
		String resource = null;
		switch (name.toUpperCase()) {
		case "GETCOLORBYID":
			resource = this.colorPath;
			break;
		default:
			break;
		}
		return resource;
	}
}
