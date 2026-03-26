package com.company.core.payroll.repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.company.core.payroll.dto.Address;
import com.company.core.payroll.dto.AddressType;
import com.company.core.payroll.dto.Employee;
import com.company.core.payroll.dto.EmployeeSearchRequestBody;
import com.company.core.payroll.dto.EmployeeSearchResult;
import com.company.core.payroll.dto.EmployeeUpdateBasic;
import com.company.core.payroll.dto.EmployeeUpdateRequestBody;
import com.company.core.payroll.dto.Salary;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class EmployeeRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<EmployeeSearchResult> searchEmployee(EmployeeSearchRequestBody requestBody, Integer limit, Integer offset) {
		log.info("searchEmployee method starts");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT COUNT(*) OVER() AS totalRecords, ");
		strSql.append("e.EMPLOYEE_ID as employeeId, e.NAME as employeeName, e.LEVEL as level,");
		strSql.append(" e.PRIMARY_PHONE_NUMBER as primaryPhoneNumber, e.SECONDARY_PHONE_NUMBER as secondaryPhoneNumber,");
		strSql.append("e.DATE_OF_JOINING as dateOfJoining, e.DATE_OF_RESIGN as dateOfResign, e.STATUS as status, e.EMAIL as email,");
		strSql.append("a.ADDRESS_TYPE as addressType, a.ADDRESS_LINE1 as addressLine1, a.ADDRESS_LINE2 as addressLine2, a.CITY as city,");
		strSql.append("a.THALUK as thaluk, a.STATE as state, a.COUNTRY as country, a.PINCODE as pincode ");
		strSql.append(" FROM EMPLOYEE e LEFT JOIN ADDRESS a ON e.ADDRESS_ID = a.ADDRESS_ID");
		strSql.append(searchCondition(requestBody));
		strSql.append(" ORDER BY EMPLOYEE_ID ");
		strSql.append(" LIMIT ");strSql.append(limit);
		strSql.append(" OFFSET ");strSql.append(offset);
		
	    return jdbcTemplate.query(strSql.toString(), (rs, rowNum) -> getEmployeeMapper(rs, rowNum));
	}


	private EmployeeSearchResult getEmployeeMapper(ResultSet rs, int rowNum) throws SQLException {
		
		EmployeeSearchResult result = new EmployeeSearchResult();
		
		result.setTotalRecords(rs.getInt("totalRecords"));
		
		Employee employee = new Employee();
		employee.setEmployeeId(rs.getInt("employeeId"));
		employee.setEmployeeName(rs.getString("employeeName"));
		employee.setLevel(rs.getString("level"));
		employee.setPrimaryPhoneNumber(rs.getInt("primaryPhoneNumber"));
		employee.setSecondaryPhoneNumber(rs.getInt("secondaryPhoneNumber"));
		employee.setDateOfJoining(rs.getInt("dateOfJoining"));
		employee.setDateOfResign(rs.getInt("dateOfResign"));
		employee.setStatus(rs.getString("status"));
		employee.setEmail(rs.getString("email"));
		
		Address address = new Address();
		address.setAddressType(AddressType.valueOf(rs.getString("addressType")));
		address.setAddressLine1(rs.getString("addressLine1"));
		address.setAddressLine2(rs.getString("addressLine2"));
		address.setCity(rs.getString("city"));
		address.setThaluk(rs.getString("thaluk"));
		address.setState(rs.getString("state"));
		address.setCountry(rs.getString("country"));
		address.setPincode(rs.getString("pincode"));
		
		result.setAddress(address);
		result.setEmployee(employee);
		return result;
	}


	private String searchCondition(EmployeeSearchRequestBody requestBody) {
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE 1 = 1 ");
		
		if(Objects.nonNull(requestBody.getPayload())) {
			if(Objects.nonNull(requestBody.getPayload().getEmployeeId())) {
				condition.append("e.EMPLOYEE_ID = ");condition.append(requestBody.getPayload().getEmployeeId());
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getName())) {
				condition.append("UPPER(e.name) = '");
				condition.append(requestBody.getPayload().getName().toUpperCase());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getLevel())) {
				condition.append("UPPER(e.level) = '");
				condition.append(requestBody.getPayload().getLevel().toUpperCase());
				condition.append("'");
			}
			if(Objects.nonNull(requestBody.getPayload().getPrimaryPhoneNumber())) {
				condition.append("e.primary_phone_number = ");condition.append(requestBody.getPayload().getPrimaryPhoneNumber());
			}
			if(Objects.nonNull(requestBody.getPayload().getSecondaryPhoneNumber())) {
				condition.append("e.secondary_phone_number = ");condition.append(requestBody.getPayload().getSecondaryPhoneNumber());
			}
			if(Objects.nonNull(requestBody.getPayload().getDateOfJoining())) {
				condition.append("e.date_of_joining = ");condition.append(requestBody.getPayload().getDateOfJoining());
			}
			if(Objects.nonNull(requestBody.getPayload().getDateOfResign())) {
				condition.append("e.date_of_resign = ");condition.append(requestBody.getPayload().getDateOfResign());
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getStatus())) {
				condition.append("UPPER(e.status) = '");
				condition.append(requestBody.getPayload().getStatus().toUpperCase());
				condition.append("'");
			}
			if(StringUtils.isNotEmpty(requestBody.getPayload().getEmail())) {
				condition.append("UPPER(e.email) = '");
				condition.append(requestBody.getPayload().getEmail().toUpperCase());
				condition.append("'");
			}
		}
		return condition.toString();
	}

	@Transactional(rollbackFor = Exception.class)
	public void addEmployee(EmployeeUpdateRequestBody requestBody) {

		if (Objects.nonNull(requestBody.getPayload())
				&& Objects.nonNull(requestBody.getPayload().getEmployeesDetails())) {

			List<EmployeeUpdateBasic> employeesDetailsList = requestBody.getPayload().getEmployeesDetails();

			StringBuilder employeeSql = new StringBuilder();
			employeeSql.append(
					" INSERT INTO EMPLOYEE (NAME, LEVEL, ADDRESS_ID, PRIMARY_PHONE_NUMBER, SECONDARY_PHONE_NUMBER, ");
			employeeSql.append(" DATE_OF_JOINING, STATUS, EMAIL) VALUES(?,?,?,?,?,?,?,?)");

			StringBuilder addressSql = new StringBuilder();
			addressSql.append(" INSERT INTO ADDRESS (ADDRESS_TYPE, ADDRESS_LINE1, ADDRESS_LINE2, CITY, THALUK, ");
			addressSql.append(" STATE, COUNTRY, PINCODE) VALUES(?,?,?,?,?,?,?,?)");
			
			StringBuilder salarySql = new StringBuilder();
			salarySql.append(" INSERT INTO SALARY (EMPLOYEE_ID, LEVEL, SALARY_PER_HOUR ");
			salarySql.append(" ) VALUES(?,?,?)");

			for (EmployeeUpdateBasic employeesDetails : employeesDetailsList) {
				Employee employee = employeesDetails.getEmployee();
				Address address = employeesDetails.getAddress();
				Salary salary = employeesDetails.getSalary();
				GeneratedKeyHolder addressKeyHolder = new GeneratedKeyHolder();
				GeneratedKeyHolder employeeKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(addressSql.toString(),
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, address.getAddressType().name());
					ps.setString(2, address.getAddressLine1());
					ps.setString(3, address.getAddressLine2());
					ps.setString(4, address.getCity());
					ps.setString(5, address.getThaluk());
					ps.setString(6, address.getState());
					ps.setString(7, address.getCountry());
					ps.setString(8, address.getPincode());
					return ps;
				}, addressKeyHolder);

				Map<String, Object> keys = addressKeyHolder.getKeys();

				long addressId = ((Long) keys.get("ADDRESS_ID"));

				
				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(employeeSql.toString(),
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, employee.getEmployeeName());
					ps.setString(2, employee.getLevel());
					ps.setInt(3, Integer.parseInt(String.valueOf(addressId)));
					ps.setInt(4, employee.getPrimaryPhoneNumber());
					ps.setInt(5, employee.getSecondaryPhoneNumber());
					ps.setInt(6, employee.getDateOfJoining());
					ps.setString(7, employee.getStatus());
					ps.setString(8, employee.getEmail());
					return ps;
				}, employeeKeyHolder);

				Map<String, Object> keyss = employeeKeyHolder.getKeys();

				long employeeId = ((Long) keyss.get("EMPLOYEE_ID"));
				
				jdbcTemplate.update(salarySql.toString(), Integer.parseInt(String.valueOf(employeeId)), employee.getLevel(), salary.getSalaryPerHour());
			}
		}
	}
}
