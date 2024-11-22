package com.netsurfingzone.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsurfingzone.constant.ApplicationConstant;
import com.netsurfingzone.dto.Student;

@RestController
@RequestMapping("/produce")
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@PostMapping("/message")
	public String sendMessage(@RequestBody Student message) {

		try {
			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json message sent succuessfully";
	}

/*```java
	@PostMapping("/message")
	public String sendMessage(@RequestBody Student message) {
		try {
			kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message.getId(), message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json message sent successfully";
	}
```
In this example:
- **Key**: `message.getId()` is used as the key. This ensures that all messages tied to a specific student ID
 will be routed to the same partition, maintaining order relative to that student.
- **Efficiency and Consistency**: With a key, you can achieve consistent processing of related messages and potentially
 optimize your application's performance by ensuring related messages are handled together.
*/

}
