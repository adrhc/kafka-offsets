package adrhc.go.ro.kafkaoffsets.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("topic")
@Getter
@Setter
public class TopicsProperties {
	private String orders;
}
