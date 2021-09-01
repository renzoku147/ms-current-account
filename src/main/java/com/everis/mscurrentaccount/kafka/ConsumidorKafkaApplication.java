package com.everis.mscurrentaccount.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import com.everis.mscurrentaccount.entity.BootCoinRequest;
import com.everis.mscurrentaccount.entity.BootCoinTransfer;
import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.service.CurrentAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Configuration
public class ConsumidorKafkaApplication {
	@Autowired
	CurrentAccountService currentAccountService;  
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Bean
    public NewTopic topic(){
        return TopicBuilder.name("topico-everis3")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id="myId", topics = "topico-everis3")
    public void listen(String message) throws Exception{
    	System.out.println(">>>>> topico-everis3 @KafkaListener <<<<<");
    	CurrentAccount ca = objectMapper.readValue(message, CurrentAccount.class);
    	System.out.println(">>> CurrentAccount <<<");
    	System.out.println(ca);
    	
    	currentAccountService.update(ca).subscribe();
        	
    }
    
    @KafkaListener(id="myId2", topics = "topico-everis7")
    public void listen2(String message) throws Exception{
    	System.out.println(">>>>> topico-everis7 @KafkaListener <<<<<");
    	BootCoinRequest bcr = objectMapper.readValue(message, BootCoinRequest.class);
    	System.out.println(">>> CurrentAccount <<< " + bcr.getAccountNumber());
    	
    	currentAccountService.findByAccountNumber(bcr.getAccountNumber())
    		.flatMap(currentAccount -> {
    					currentAccount.setBalance(currentAccount.getBalance()-bcr.getAmount()*bcr.getExchangeRate());
    					return currentAccountService.update(currentAccount);
    				})
    		.subscribe();
        	
    }
    
    @KafkaListener(id="myId3", topics = "topico-everis8")
    public void listen3(String message) throws Exception{
    	System.out.println(">>>>> topico-everis8 @KafkaListener <<<<<");
    	BootCoinTransfer bcr = objectMapper.readValue(message, BootCoinTransfer.class);
    	System.out.println(">>> CurrentAccount <<< " + bcr.getAccountNumber());
    	
    	currentAccountService.findByAccountNumber(bcr.getAccountNumber())
    		.flatMap(currentAccount -> {
    					currentAccount.setBalance(currentAccount.getBalance()+bcr.getBuyer().getAmount()*bcr.getBuyer().getExchangeRate());
    					return currentAccountService.update(currentAccount);
    				})
    		.subscribe();
        	
    }
}
