package adrhc.go.ro.kafkaoffsets;

import adrhc.go.ro.kafkaoffsets.config.TopicsProperties;
import adrhc.go.ro.kafkaoffsets.messages.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.Stream;

@SpringBootTest
@EnabledIfSystemProperty(named = "integration", matches = "true")
public class OrderProducerIT {
	@Autowired
	private TopicsProperties properties;
	@Autowired
	private KafkaTemplate<String, Order> producer;

	@Test
	void produce() {
		Stream
				.iterate(0, it -> it < 1000, it -> it + 1)
				.forEach(it -> producer.send(properties.getOrders(), new Order(it)));
	}
}
