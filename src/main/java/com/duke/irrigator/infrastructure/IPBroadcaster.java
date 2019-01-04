package com.duke.irrigator.infrastructure;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
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
		NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
		if(networkInterface == null){
			networkInterface = NetworkInterface.getByName("en0");
		}

		String localIP = "";
		List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
		for(InterfaceAddress interfaceAddress: addressList){
			if(interfaceAddress.getBroadcast() != null){
				localIP = interfaceAddress.getAddress().getHostAddress();
				break;
			}
		}

		MqttMessage msg = new MqttMessage(localIP.getBytes());
		msg.setQos(0);
		msg.setRetained(true);

		client.publish(topic,msg);

		return null;
	}

	private String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException ex)
		{

		}
		return null;
	}
}

