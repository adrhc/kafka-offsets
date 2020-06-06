package adrhc.go.ro.kafkaoffsets;

import adrhc.go.ro.kafkaoffsets.config.TopicsProperties;
import adrhc.go.ro.kafkaoffsets.messages.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfSystemProperty(named = "integration", matches = "true")
@Slf4j
public class OrderConsumerIT {
	@Autowired
	private TopicsProperties properties;
	@Autowired
	private ConsumerFactory<String, Order> consumerFactory;
	@Value("${spring.kafka.consumer.max-poll-records}")
	private int maxPollRecords;

	@Test
	void consume() {
		try (Consumer<String, Order> consumer = consumerFactory.createConsumer()) {
			consumer.subscribe(List.of(properties.getOrders()));
			consumer.poll(Duration.ofSeconds(5))
					.forEach(it -> {
						log.debug("\n{}", it.value());
						assertThat(it.offset()).withFailMessage("offset exceeded").isLessThan(maxPollRecords);
					});
			log.debug("skipping consumer tidy auto closure");
			ReflectionTestUtils.setField(consumer, "closed", Boolean.TRUE);
		}
	}
}
