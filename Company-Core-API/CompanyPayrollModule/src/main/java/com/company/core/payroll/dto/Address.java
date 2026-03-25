package com.company.core.payroll.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Address {

	@JsonProperty("addressId")
	@JsonIgnore
	public Integer addressId;
	
	@JsonProperty("addressType")
	public AddressType addressType;
	
	@JsonProperty("addressLine1")
	public String addressLine1;
	
	@JsonProperty("addressLine2")
	public String addressLine2;
	
	@JsonProperty("city")
	public String city;
	
	@JsonProperty("thaluk")
	public String thaluk;
	
	@JsonProperty("state")
	public String state;
	
	@JsonProperty("country")
	public String country;
	
	@JsonProperty("pincode")
	public String pincode;
	
}
