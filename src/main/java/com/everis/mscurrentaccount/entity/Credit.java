package com.everis.mscurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Document("Credit")
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    private String id;

    @NotNull
    private CreditCard creditCard;

    @NotNull
    private Double amount;
    
    @NotNull
    private Integer numberQuota;

    private LocalDateTime date;

}
