package com.company.core.payroll.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Attendance {
	
	@JsonProperty("attendanceId")
	@JsonIgnore
	public Integer attendanceId;
	
	@JsonProperty("projectId")
	public Integer projectId;

	@JsonProperty("employeeId")
	public Integer employeeId;
	
	@JsonProperty("date")
	public Integer date;
	
	@JsonProperty("days")
	public Integer days;
	
	
}
