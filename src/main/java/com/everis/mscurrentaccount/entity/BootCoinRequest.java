package com.everis.mscurrentaccount.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BootCoinRequest {
	private String id;
	
	private BootCoin bootCoin;
	
	private Double amount;
	
	private TypePaid typePaid;
	
	private Double exchangeRate;
	
	private String accountNumber;
	
	private BootCoinState bootCoinState;
}
