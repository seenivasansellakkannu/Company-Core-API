package com.company.core.payroll.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmployeeSearchPayloadRequestBody {
	
	@JsonProperty("employeeId")
	@JsonIgnore
	public Integer employeeId;

	@JsonProperty("name")
	public String name;
	
	@JsonProperty("level")
	public String level;
	
	@JsonProperty("addressId")
	@JsonIgnore
	public String addressId;
	
	@JsonProperty("primaryPhoneNumber")
	public Integer primaryPhoneNumber;
	
	@JsonProperty("secondaryPhoneNumber")
	public Integer secondaryPhoneNumber;
	
	@JsonProperty("dateOfJoining")
	public Integer dateOfJoining;
	
	@JsonProperty("dateOfResign")
	public Integer dateOfResign;
	
	@JsonProperty("status")
	public String status;
	
	@JsonProperty("email")
	public String email;
	
}
