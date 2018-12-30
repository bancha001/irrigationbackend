package com.duke.irrigator.model;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleDetail {

	private Date previousFireTime;
	private Date nextFireTime;
	private String jobName;
	private String groupName;
	private int irrigationZone;

}
