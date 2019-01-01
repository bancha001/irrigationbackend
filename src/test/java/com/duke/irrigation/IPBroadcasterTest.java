package com.duke.irrigation;

import com.duke.irrigator.infrastructure.IPBroadcaster;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class IPBroadcasterTest {

	@Test
	public void testBroadcast() throws Exception{

			String publisherId = UUID.randomUUID().toString();
			MqttClient publisher = new MqttClient("tcp://iot.eclipse.org:1883",publisherId);

			String subscriberId = UUID.randomUUID().toString();
			MqttClient subscriber = new MqttClient("tcp://iot.eclipse.org:1883",subscriberId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);


			subscriber.connect(options);
			publisher.connect(options);

			CountDownLatch receivedSignal = new CountDownLatch(1);

			subscriber.subscribe("duke/device00001/irrigation/ipres", (topic, msg) -> {
				String result = msg.toString();
				assertTrue(result.startsWith("192.168.1."));
				receivedSignal.countDown();
			});


			Callable<Void> target = new IPBroadcaster(publisher,"duke/device00001/irrigation/ipreq");
			target.call();

			receivedSignal.await(1, TimeUnit.MINUTES);

	}

}
