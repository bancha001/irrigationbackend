package com.duke.irrigator.model;

import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@JsonComponent
public class ScheduleRequest {
	@NotNull
	//private LocalDateTime dateTime;
	private String dateTime;

	@NotNull
	//private ZoneId timeZone;
	private String timeZone;

	private String action;

	private boolean isRepeatable;

	private int irrigationZone;

}
