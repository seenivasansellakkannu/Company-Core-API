package com.company.core.payroll.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SalaryCalculationEmployeeDate {
	
	@JsonProperty("date")
	public Integer date; 
	
	@JsonProperty("days")
	public BigDecimal days;
	
	@JsonProperty("salary")
	public BigDecimal salary;
	
	
}
