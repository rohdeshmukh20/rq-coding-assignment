package com.example.rqchallenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Employee implements Serializable {
    @JsonProperty("employee_name")
    private String employee_name;
    @JsonProperty("employee_salary")
    private String employee_salary;
    @JsonProperty("employee_age")
    private String employee_age;
    @JsonProperty("id")
    private Long id;
}
