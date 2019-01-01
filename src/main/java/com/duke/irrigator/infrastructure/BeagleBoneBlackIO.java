package com.duke.irrigator.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
@Component
public class BeagleBoneBlackIO {
	private static final Logger logger = LoggerFactory.getLogger(BeagleBoneBlackIO.class);
	@Autowired
	private Environment environment;

	final static String OUTPUT = "out";

	@Value("${gpio.path}")
	private String gpioPath;

	public void setPin(int zoneId, String action){
		FileInputStream in = null;
		String pinNo = environment.getProperty("zone."+zoneId);
		String state = environment.getProperty("state."+action.toUpperCase());
		try {
			in = new FileInputStream(gpioPath+"gpio" + pinNo + "/value");
			in.close();
		} catch (FileNotFoundException e) {
			initializePin(pinNo,OUTPUT);
			logger.info("Initialize pin "+pinNo);
		}catch (IOException ioe){
			logger.info("Error setting pin "+pinNo+" : "+ioe.toString());
		}

		writeFile(gpioPath+"gpio" + pinNo + "/value",state);
	}
	public String getPin(String zoneId)
	{
		String pinNo = environment.getProperty("zone."+zoneId);
		FileInputStream in = null;
		//Set default value as high
		int value = 1;
		try {
			in = new FileInputStream(gpioPath+"gpio" + pinNo + "/value");
			value = in.read() - 48;
			in.close();
		} catch (FileNotFoundException e) {

			initializePin(pinNo, OUTPUT);
			logger.info("Initialize pin "+pinNo);

		}catch(IOException ioe){
			logger.info("Error getting pin "+pinNo+" : "+ioe.toString());
		}

		return Integer.toString(value);
	}
	public void initializePin(String pinNo, String accessType) {
		writeFile(gpioPath+"export", pinNo);
		writeFile(gpioPath+"gpio" + pinNo + "/direction", accessType);

	}
	private void writeFile(String fileName,String content)
	{
		try{
			FileOutputStream fileOutputStream = new FileOutputStream(fileName);
			PrintWriter printWritter = new PrintWriter(fileOutputStream);
			printWritter.print(content);
			printWritter.close();
			fileOutputStream.close();
		}catch(Exception e)
		{
			logger.error("Error writing file "+fileName+" : "+e.toString());
		}

	}
}
