package com.everis.mscurrentaccount.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCardTransaction {
    private String id;

    private Credit credit;
    
    private DebitCard debitCard;

    private String transactionCode;

    private Double transactionAmount;
    
    private TypeTransactionDebitCard typeTransactionDebitCard;

    private LocalDateTime transactionDateTime;
}
