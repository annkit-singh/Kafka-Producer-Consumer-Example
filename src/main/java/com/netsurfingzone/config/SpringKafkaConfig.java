package com.netsurfingzone.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.netsurfingzone.constant.ApplicationConstant;
import com.netsurfingzone.dto.Student;

@Configuration
@EnableKafka
public class SpringKafkaConfig {

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);
		configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto");
		//defines from which package object can be deserialized , this is intended for safety purposes and to avoid
		// deserialization attacks
		return new DefaultKafkaProducerFactory<String, Object>(configMap);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, Student> consumerFactory() {
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ApplicationConstant.KAFKA_LOCAL_SERVER_CONFIG);
		configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configMap.put(ConsumerConfig.GROUP_ID_CONFIG, ApplicationConstant.GROUP_ID_JSON);
//		All consumers with the same group ID share the load of handling messages. `ApplicationConstant.GROUP_ID_JSON` is likely a constant defining your consumer group's name.
		configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "com.netsurfingzone.dto");
		return new DefaultKafkaConsumerFactory<>(configMap);
	}




	/*The `ConcurrentKafkaListenerContainerFactory` bean configuration is a crucial part of setting up a Kafka consumer in a Spring application.
	This factory is used to create instances of message listener containers that manage the consumption of messages from Kafka topics.*/
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Student> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Student> factory = new ConcurrentKafkaListenerContainerFactory<String, Student>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

/*	- **`ConcurrentKafkaListenerContainerFactory` Configuration**:
			- Instantiates a new `ConcurrentKafkaListenerContainerFactory` that uses the specified generic types `<String, Order>` for message keys and values.
			- Calls `factory.setConsumerFactory(consumerFactory())` to associate the consumer factory that provides configuration details for creating Kafka consumers (including deserializers, group ID, and server configurations).

			- **Activation of Listeners**:
			- By providing this bean, Spring is able to automatically connect `@KafkaListener` annotated methods to Kafka topics using the settings you defined.*/
}
