package com.everis.mscurrentaccount.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document("BootCoinTransfer")
@AllArgsConstructor
@NoArgsConstructor
public class BootCoinTransfer {
	@Id
	private String id;
	
	// COMPRADOR
	private BootCoinRequest buyer; // SOLICITUD DE COMPRA
	
	// VENDEDOR
	private BootCoin seller; //SALE LOS BOOTCOINS
	
	private TypePaid typePaid; // A DONDE RECIBE EL DINERO
	
	private String accountNumber;// NUMERO DE CUENTA DONDE RECIBE DINERO

	private String numberTransaction;

}
