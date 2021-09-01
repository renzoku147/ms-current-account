package com.everis.mscurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard implements Card{

    private String id;

    private String cardNumber;
    
    private Customer customer;
    
    private List<Accounts> accounts;

    @JsonDeserialize(using=LocalDateDeserializer.class)
    @JsonSerialize(using=LocalDateSerializer.class)
    private LocalDate expirationDate;

    @JsonDeserialize(using=LocalDateTimeDeserializer.class)
    @JsonSerialize(using=LocalDateTimeSerializer.class)
    private LocalDateTime date;
    
    private Double amountPurseTransaction;
}
