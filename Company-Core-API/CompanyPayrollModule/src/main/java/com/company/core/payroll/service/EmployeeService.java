package com.company.core.payroll.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.company.core.payroll.dto.EmployeeSearchRequestBody;
import com.company.core.payroll.dto.EmployeeSearchResponse;
import com.company.core.payroll.dto.EmployeeSearchResult;
import com.company.core.payroll.dto.EmployeeUpdateRequestBody;
import com.company.core.payroll.dto.SuccessResponse;
import com.company.core.payroll.repo.EmployeeRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;
	
	public EmployeeSearchResponse searchEmployee(EmployeeSearchRequestBody requestBody, Integer limit, Integer offset) {

		EmployeeSearchResponse response = new EmployeeSearchResponse();
		int count = 0;
		List<EmployeeSearchResult> employeesList = employeeRepo.searchEmployee(requestBody, limit, offset);
		
		if(!employeesList.isEmpty()) {
			count = employeesList.stream().findFirst().get().getTotalRecords();
		}
		response.setEmployees(employeesList);
		response.setTotalRecords(count);
		return response;
	}
	
	public SuccessResponse addEmployee(EmployeeUpdateRequestBody requestBody) {
		SuccessResponse successResponse = new SuccessResponse();
		
		employeeRepo.addEmployee(requestBody);
		
		successResponse.setStatus(HttpStatus.CREATED.value());
		successResponse.setMessage(HttpStatus.CREATED.getReasonPhrase());
		successResponse.setTimestamp(LocalDateTime.now());
		return successResponse;
	}
	

}