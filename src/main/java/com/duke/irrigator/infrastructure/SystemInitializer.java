package com.duke.irrigator.infrastructure;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.Callable;

@Component
public class SystemInitializer {

	@Value("${ip.req.topic}")
	private String reqTopic;
	@Value("${ip.res.topic}")
	private String resTopic;

	private static final Logger logger = LoggerFactory.getLogger(SystemInitializer.class);

	@PostConstruct
	public void registerIP(){

		try {

			String publisherId = UUID.randomUUID().toString();
			MqttClient publisher = new MqttClient("tcp://iot.eclipse.org:1883", publisherId);

			String subscriberId = UUID.randomUUID().toString();
			MqttClient subscriber = new MqttClient("tcp://iot.eclipse.org:1883", subscriberId);


			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);

			subscriber.connect(options);
			publisher.connect(options);
			subscriber.subscribe(reqTopic, (topic, msg) -> {
				Callable<Void> target = new IPBroadcaster(publisher,resTopic);
				target.call();
			});

		}catch(Exception e){
			logger.error(e.toString());
		}

	}
}
