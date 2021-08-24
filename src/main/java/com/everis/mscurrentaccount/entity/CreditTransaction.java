package com.everis.mscurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@Document("CreditTransaction")
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransaction {
    @Id
    private String id;

    @NotNull
    private Credit credit;
    
    @NotNull
    private DebitCard debitCard;

    @NotBlank
    private String transactionCode;

    @NotNull
    private Double transactionAmount;

    private LocalDateTime transactionDateTime;
}
