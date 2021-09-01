package com.everis.mscurrentaccount.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

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
}
