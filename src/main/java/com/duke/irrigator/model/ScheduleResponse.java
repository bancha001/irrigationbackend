package com.duke.irrigator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponse {
    private boolean success;
    private String message;

    public ScheduleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
