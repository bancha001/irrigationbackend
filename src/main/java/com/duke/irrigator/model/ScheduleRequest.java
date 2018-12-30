package com.duke.irrigator.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleRequest {
	@NotNull
	private LocalDateTime dateTime;

	@NotNull
	private ZoneId timeZone;

	private String action;

	private boolean isRepeatable;

	private int irrigationZone;

}
