package com.ag.coronahospital.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	private static final String TOPIC_NAME = "corona-world";

	@Value("${project-id}")
	private String projectName;

	@Value("${hospital-name}")
	private String hospitalName;

	@GetMapping("/declare/{count}")
	public String declare(@PathVariable String count) {
		Publisher publisher = null;
		try {
			ProjectTopicName topicName = ProjectTopicName.of(projectName, TOPIC_NAME);

			publisher = Publisher.newBuilder(topicName).build();

			ByteString data = ByteString.copyFromUtf8(count);
			PubsubMessage pubsubMessage =
					PubsubMessage.newBuilder()
							.setData(data)
							.putAllAttributes(ImmutableMap.of("hospital-name", hospitalName))
							.build();

			ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
			String messageId = messageIdFuture.get();
			System.out.println("Published message ID: " + messageId);
			return messageId;
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch(ExecutionException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(publisher != null) {
				publisher.shutdown();
				try {
					publisher.awaitTermination(1, TimeUnit.MINUTES);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return "failed";
	}
}
