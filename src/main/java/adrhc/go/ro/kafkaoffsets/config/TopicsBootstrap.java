package adrhc.go.ro.kafkaoffsets.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicsBootstrap {
	private final TopicsProperties properties;

	public TopicsBootstrap(TopicsProperties properties) {this.properties = properties;}

	@Bean
	public NewTopic initialOrdersTopic() {
		return TopicBuilder.name(properties.getOrders()).build();
	}
}
