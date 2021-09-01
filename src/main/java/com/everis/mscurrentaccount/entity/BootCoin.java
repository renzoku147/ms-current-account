package com.everis.mscurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BootCoin {
	private String id;
	
	private String dni;
	
	private Integer phoneNumber;
	
	private String email;
	
	private Double balance;
}
