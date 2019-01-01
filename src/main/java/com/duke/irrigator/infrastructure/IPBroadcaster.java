package com.duke.irrigator.infrastructure;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.util.concurrent.Callable;


public class IPBroadcaster implements Callable<Void> {

	private static final Logger log = LoggerFactory.getLogger(IPBroadcaster.class);
	private IMqttClient client;
	private String topic;

	public IPBroadcaster(IMqttClient client,String topic) {
		this.client = client;
		this.topic = topic;
	}

	@Override
	public Void call() throws Exception {

		if ( !client.isConnected()) {
			log.info("[I31] Client not connected.");
			return null;
		}
		String localIP = InetAddress.getLocalHost().getHostAddress();
		MqttMessage msg = new MqttMessage(localIP.getBytes());
		msg.setQos(0);
		msg.setRetained(true);

		client.publish(topic,msg);

		return null;
	}
}