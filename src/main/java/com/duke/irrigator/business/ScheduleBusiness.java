package com.duke.irrigator.business;

import com.duke.irrigator.model.ScheduleDetail;
import com.duke.irrigator.model.ScheduleRequest;
import com.duke.irrigator.model.ScheduleResponse;

import java.util.List;

public interface ScheduleBusiness {
	ScheduleResponse createSchedule(ScheduleRequest scheduleRequest);
	List<ScheduleDetail> listSchedule();
	ScheduleResponse deleteSchedule(ScheduleDetail scheduleDetail);
}
