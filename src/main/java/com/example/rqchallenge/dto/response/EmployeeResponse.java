package com.example.rqchallenge.dto.response;

import com.example.rqchallenge.dto.Employee;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class EmployeeResponse {
    private List<Employee> data;
}
