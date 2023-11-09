package com.example.rqchallenge.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NotNull
@NotEmpty
public class CreateEmployeeRequest {
    @NotNull(message = "name cannot be empty")
    private String name;

    @NotNull(message = "salary cannot be empty")

    private String salary;

    @NotNull(message = "age cannot be empty")
    private String age;
}
