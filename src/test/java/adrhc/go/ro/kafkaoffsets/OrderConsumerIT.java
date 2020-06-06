package adrhc.go.ro.kafkaoffsets;

import adrhc.go.ro.kafkaoffsets.config.TopicsProperties;
import adrhc.go.ro.kafkaoffsets.messages.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Use OrderProducerIT to create some records.
 */
@SpringBootTest
@EnabledIfSystemProperty(named = "integration", matches = "true")
@Slf4j
public class OrderConsumerIT {
	@Autowired
	private TopicsProperties properties;
	@Autowired
	private ConsumerFactory<String, Order> consumerFactory;
	@Autowired
	private KafkaProperties kafkaProperties;

	/**
	 * Forcing the consumer to believe that is already closed.
	 */
	private static void disableGracefulClosure(Consumer<?, ?> consumer) {
		log.debug("\nskipping consumer tidy auto closure");
		ReflectionTestUtils.setField(consumer, "closed", Boolean.TRUE);
	}

	/**
	 * Reset the offset before 1th run of the test.
	 * In order to reset the offset you'll have to
	 * remove the "order" topic before running the test.
	 * Subsequent test executions will pool the same records as 1th run.
	 * Careful with the debugger - the delay might be enough to determine an auto commit!
	 */
	@Test
	void failToAutoCommit() {
		try (Consumer<String, Order> consumer = consumerFactory.createConsumer()) {
			consumer.subscribe(List.of(properties.getOrders()));
			consumer.poll(Duration.ofSeconds(5))
					.forEach(it -> log.debug("\n{}", it.value()));
			// no auto commit on next pool for a large auto commit period
			consumer.poll(Duration.ofSeconds(5))
					.forEach(it -> {
						log.debug("\n{}", it.value());
						assertThat(it.offset()).withFailMessage("offset exceeded")
								.isLessThan(2 * kafkaProperties.getConsumer().getMaxPollRecords());
					});
			disableGracefulClosure(consumer);
		}
	}

	@Test
	void succeedToAutoCommit() throws InterruptedException {
		try (Consumer<String, Order> consumer = consumerFactory.createConsumer()) {
			consumer.subscribe(List.of(properties.getOrders()));
			consumer.poll(Duration.ofSeconds(5))
					.forEach(it -> log.debug("\n{}", it.value()));
			consumer.poll(Duration.ofSeconds(5))
					.forEach(it -> log.debug("\n{}", it.value()));
			waitForAutoCommit(); // next poll shall auto commit
			consumer.poll(Duration.ofSeconds(5)) // pool executed after auto commit period
					.forEach(it -> log.debug("\n{}", it.value()));
			waitForAutoCommit(); // there's no next poll to determine an auto commit
			disableGracefulClosure(consumer);
		}
	}

	private void waitForAutoCommit() throws InterruptedException {
		log.debug("\nwaiting for the auto commit interval to pass");
		Thread.sleep(kafkaProperties.getConsumer().getAutoCommitInterval().toMillis() + 1000L);
	}
}
