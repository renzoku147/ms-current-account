package com.everis.mscurrentaccount.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Customer {

    @Id
    String id;

    @NotEmpty
    String name;

    @NotEmpty
    String lastName;

    @Valid
    TypeCustomer typeCustomer;

    @NotNull
    String dni;

    @NotNull
    Integer age;

    @NotNull
    String gender;

}
