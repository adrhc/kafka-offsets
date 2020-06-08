package adrhc.go.ro.kafkaoffsets;

import adrhc.go.ro.kafkaoffsets.messages.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfSystemProperty(named = "integration", matches = "true")
@Import(OrderListenerIT.Config.class)
@Slf4j
public class OrderListenerIT {
	private static final CountDownLatch latch = new CountDownLatch(1);

	@Test
	void handle() throws InterruptedException {
		latch.await(3, TimeUnit.SECONDS);
		log.debug("\nend");
	}

	@TestConfiguration
	static class Config {
		private int stopOffset;

		Config(KafkaProperties kafkaProperties) {
			stopOffset = kafkaProperties.getConsumer().getMaxPollRecords() / 2;
		}

		/**
		 * by default:
		 * KafkaMessageListenerContainer.ListenerConsumer.errorHandler = SeekToCurrentErrorHandler
		 */
		@Component
		public class OrderHandler {
			@KafkaListener(topics = "${topic.orders}")
			public void consume(@Payload Order order) {
				log.debug("\n{}", order);
				assertThat(order.getId()).isLessThanOrEqualTo(stopOffset);

				if (order.getId() >= stopOffset) {
					latch.countDown();
					System.exit(1);
				}
			}
		}
	}
}
