package com.company.core.payroll.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SalaryCalculationSuccessResponse {
	
	@JsonProperty("project")
	public SalaryCalculationProject SalaryCalculationProject;
	
	@JsonProperty("totalRecords")
	public Integer totalRecords;
	
}
