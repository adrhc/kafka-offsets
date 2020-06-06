package adrhc.go.ro.kafkaoffsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class KafkaOffsetsApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaOffsetsApplication.class, args);
	}
}
