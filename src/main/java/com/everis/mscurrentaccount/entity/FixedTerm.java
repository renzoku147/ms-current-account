package com.everis.mscurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixedTerm implements BankAccount{
    private String id;

    private Customer customer;

    private String accountNumber;

    private List<Person> holders;

    private List<Person> signers;

    private Double balance;

    private Integer limitDeposits;

    private Integer limitDraft;

    private LocalDate allowDateTransaction;

    private Integer freeTransactions;

    private Double commissionTransactions;
    
    private DebitCard debitCard;

    private LocalDateTime date;

}
