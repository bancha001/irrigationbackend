package com.duke.irrigator.api;

import com.duke.irrigator.business.ScheduleBusiness;
import com.duke.irrigator.model.ScheduleDetail;
import com.duke.irrigator.model.ScheduleRequest;
import com.duke.irrigator.model.ScheduleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SchedulerApi {
	private static final Logger logger = LoggerFactory.getLogger(SchedulerApi.class);

	@Autowired
	private ScheduleBusiness scheduleBusiness;

	@PostMapping("/schedule")
	public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
		ScheduleResponse scheduleResponse = scheduleBusiness.createSchedule(scheduleRequest);
		return ResponseEntity.ok(scheduleResponse);
	}


	@GetMapping("/listSchedule")
	public ResponseEntity<List<ScheduleDetail>> listSchedule() {
		return ResponseEntity.ok(scheduleBusiness.listSchedule());
	}

	@DeleteMapping("/deleteSchedule")
	public ResponseEntity<ScheduleResponse> deleteSchedule(@RequestBody ScheduleDetail scheduleDetail) {
		ScheduleResponse scheduleResponse = scheduleBusiness.deleteSchedule(scheduleDetail);
		return ResponseEntity.ok(scheduleResponse);
	}
}
