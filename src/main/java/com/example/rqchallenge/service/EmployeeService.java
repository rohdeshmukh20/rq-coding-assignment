package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.request.CreateEmployeeRequest;
import com.example.rqchallenge.dto.response.EmployeeResponse;
import com.example.rqchallenge.util.DataGeneratorUtils;
import com.example.rqchallenge.util.EmployeeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.rqchallenge.util.EmployeeUtils.parseSalary;

@Service
@Slf4j
public class EmployeeService {
    @Value("${api.url}")
    private String apiUrl;

    @Value(("${api.url.getEmployees.path}"))
    private String apiGetEmployeesPath;

    @Value(("${api.url.getEmployee.path}"))
    private String apiGetEmployeePath;

    @Value(("${api.url.getEmployee.paramName}"))
    private String apiGetEmployeeParam;

    @Value(("${api.url.createEmployee.path}"))
    private String apiCreateEmployeePath;

    @Value(("${api.url.deleteEmployee.param}"))
    private String apiDeleteEmployeeParam;

    @Value(("${api.url.deleteEmployee.path}"))
    private String apiDeleteEmployeePath;
    private static final RestTemplate restTemplate = new RestTemplate();


    public ResponseEntity<List<Employee>> getEmployees() {
        ResponseEntity<EmployeeResponse> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(
                    apiUrl + apiGetEmployeesPath, EmployeeResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            responseEntity = ResponseEntity.ok(DataGeneratorUtils.getEmployees());
        } catch (Exception exception) {
            log.error("Get all employees operation failed {}",exception.toString());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(responseEntity.getBody().getData());
    }

    public ResponseEntity<List<Employee>> getEmployeesByName(String searchString) {
        ResponseEntity<EmployeeResponse> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(
                    apiUrl + apiGetEmployeesPath, EmployeeResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            responseEntity = ResponseEntity.ok(DataGeneratorUtils.getEmployees());
        } catch (Exception exception) {
            log.error("Search operation failed {}",exception.toString());
            return ResponseEntity.internalServerError().build();
        }
        try {
            List<Employee> employeeList = new ArrayList<>();
            if (!responseEntity.getBody().getData().isEmpty()) {

                employeeList = responseEntity.getBody().getData().stream().
                        filter(
                                employee -> employee.getEmployee_name().contains(searchString)
                        ).collect(Collectors.toList());
            }
            return new ResponseEntity<List<Employee>>(employeeList,
                    responseEntity.getHeaders(), responseEntity.getStatusCode());
        } catch (Exception e) {
            log.error("Error while searching employees with string {} : {}",
                    searchString,
                    e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        ResponseEntity<Employee> responseEntity;
        try {
            Map<String, String> params = new HashMap<>();
            params.put(apiGetEmployeeParam, id);
            responseEntity = restTemplate.exchange(
                    apiUrl + apiGetEmployeePath,
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    Employee.class,
                    params
            );
            return responseEntity;
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            EmployeeResponse employeeResponse = DataGeneratorUtils.getEmployees();
            List<Employee> searchedEmployees = employeeResponse.getData().stream().filter(
                    employee -> employee.getId().toString().equals(id))
                    .collect(Collectors.toList());
            if(searchedEmployees.size() == 1) {
                return ResponseEntity.ok(searchedEmployees.get(0));
            }
            log.error("Found two employees with same ID {}",
                    id);
            return ResponseEntity.internalServerError().build();
        } catch (Exception exception) {
            log.error("Error while searching employee from ID {} : {}",
                    id, exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer max = 0;
        ResponseEntity<EmployeeResponse>  responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(
                    apiUrl + apiGetEmployeesPath, EmployeeResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            responseEntity = ResponseEntity.ok(DataGeneratorUtils.getEmployees());
        } catch (Exception exception) {
            log.error("Error while processing highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
        try {
            List<Employee> employeeList = responseEntity.getBody().getData();
            if (!employeeList.isEmpty()) {
                Optional<Employee> maxEmployee = employeeList.stream().
                        max(Comparator.comparingInt(EmployeeUtils::parseSalary));
                max = parseSalary(maxEmployee.get());
            }
            return new ResponseEntity<>(max,
                    responseEntity.getHeaders(), responseEntity.getStatusCode());
        } catch (Exception exception) {
            log.error("Error while processing highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        ResponseEntity<EmployeeResponse>  responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(
                    apiUrl + apiGetEmployeesPath, EmployeeResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            responseEntity = ResponseEntity.ok(DataGeneratorUtils.getEmployees());
        } catch (Exception exception) {
            log.error("Error while processing top 10 highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
        try {
            List<Employee> employeeList = responseEntity.getBody().getData();
            List<String> employeeNames = new ArrayList<>();
            if (!employeeList.isEmpty()) {
                employeeNames = employeeList.stream()
                        .sorted(Comparator.comparingInt(
                                employee -> parseSalary((Employee) employee)).reversed())
                        .limit(10)
                        .map(Employee::getEmployee_name)
                        .collect(Collectors.toList());
            }
            return new ResponseEntity<>(employeeNames,
                    responseEntity.getHeaders(), responseEntity.getStatusCode());
        } catch (Exception e) {
            log.error("Error while processing top 10 highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Employee> createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateEmployeeRequest> requestEntity = new HttpEntity<>(createEmployeeRequest, headers);
            ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(
                    apiUrl + apiCreateEmployeePath,
                    requestEntity,
                    Employee.class);
            return responseEntity;
        } catch (HttpClientErrorException  httpClientErrorException) {
            Employee employee = new Employee(createEmployeeRequest.getName(),createEmployeeRequest.getSalary(),
                    createEmployeeRequest.getSalary(),new Random().nextLong());
            return new ResponseEntity<Employee>(employee, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Error while creating employee with name {}: {}",createEmployeeRequest.getName(),
                    exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<String> deleteEmployeeById(String id) throws HttpClientErrorException.TooManyRequests {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(apiDeleteEmployeeParam, id);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + apiDeleteEmployeePath,
                    HttpMethod.DELETE,
                    null,
                    String.class,
                    uriVariables);
            return responseEntity;
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.warn("TOO many requests exception, building mock data {} ",tooManyRequests.getMessage());
            EmployeeResponse employeeResponse = DataGeneratorUtils.getEmployees();
            List<Employee> searchedEmployees = employeeResponse.getData().stream().filter(
                            employee -> employee.getId().toString().equals(id))
                    .collect(Collectors.toList());

            if(searchedEmployees.size() == 1) {
                return ResponseEntity.ok(searchedEmployees.get(0).getEmployee_name());
            }
            log.error("Found two employees with same ID {}",
                    id);
            return ResponseEntity.internalServerError().build();
        }
        catch (HttpClientErrorException httpClientErrorException) {
            log.warn("Delete operation not allowed, trying to get employee with ID: {} ", id);
            ResponseEntity<Employee> employee = getEmployeeById(id);
            if (employee.getBody() != null) {
                log.warn("Employee found, hence it can be deleted whenever operation is allowed by server");
                return new ResponseEntity<>(employee.getBody().getEmployee_name(),
                        HttpStatus.OK);
            }
            log.error("Employee with id {} not found. User is trying to delete non-existent employee",id);
            return ResponseEntity.badRequest().build();
        }

        catch (Exception exception) {
            log.error("Error while deleting employee with id {} :{}",id,exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }
}
