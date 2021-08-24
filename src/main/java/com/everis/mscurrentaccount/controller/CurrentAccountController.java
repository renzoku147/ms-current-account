package com.everis.mscurrentaccount.controller;

import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.service.CurrentAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RefreshScope
@RestController
@RequestMapping("/currentAccount")
@Slf4j
public class CurrentAccountController {

    @Autowired
    CurrentAccountService currentAccountService;

    @GetMapping("list")
    public Flux<CurrentAccount> findAll(){
        return currentAccountService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<CurrentAccount> findById(@PathVariable String id){
        return currentAccountService.findById(id);
    }
    
    @GetMapping("/findAccountByCustomerId/{idcustomer}")
    public Flux<CurrentAccount> findAccountByCustomerId(@PathVariable String idcustomer){
        return currentAccountService.findAccountByCustomerId(idcustomer);
    }
    

    @PostMapping("/create")
    public Mono<ResponseEntity<CurrentAccount>> create(@Valid @RequestBody CurrentAccount currentAccount){
        // VERIFICAMOS SI EXISTE EL CLIENTE
    	log.info("Esta entrando a este metodo create");
    	return currentAccountService.findCustomerById(currentAccount.getCustomer().getId())
    			.flatMap(cst -> currentAccountService.verifyExpiredDebt(currentAccount.getCustomer().getId())
    							.filter(expired -> expired)
		    					.flatMap(expired -> {
		    	        			log.info("Encontro el customer " + cst.getName());
		    	        			return currentAccountService.verifyAccountNumber(currentAccount.getAccountNumber())
		    	    					.filter(optional -> {
		    	    						log.info("El optional debe estar vacio " + optional.isPresent());
		    	    						return !optional.isPresent();
		    	    					})
		    	        				.flatMap(bankAccount -> {
		    	        						log.info("El accountNumber es unico!");
		    		                            return currentAccountService.countAccountBank(currentAccount.getCustomer().getId()) // Mono<Long> # Cuentas bancarias del Cliente
		    		                                .filter(count -> {
		    		                                    switch (cst.getTypeCustomer().getValue()){
		    		                                        case PERSONAL:
		    		                                            return count < 1; // max 1 Cuenta por Cliente PERSONAL
		    		
		    		                                        case EMPRESARIAL:
		    		                                            return currentAccount.getHolders() != null & currentAccount.getHolders().size() > 0; // Cliente EMPRESARIAL debe tener 1 o mas titulares
		    		
		    		                                        default: return false;
		    		                                    }
		    		                                })
		    		                                .flatMap(c -> {
		    		                                    switch (cst.getTypeCustomer().getValue()){
		    		                                        case EMPRESARIAL:
		    		                                            switch (cst.getTypeCustomer().getSubType().getValue()){
		    		                                                case PYME: return currentAccountService.findCreditCardByCustomerId(cst.getId())
		    		                                                        .count()
		    		                                                        .filter(cntCCard -> cntCCard > 0)
		    		                                                        .flatMap(cntCCard -> {
		    		                                                            currentAccount.setCustomer(cst);
		    		                                                            currentAccount.setDate(LocalDateTime.now());
		    		                                                            currentAccount.setCommissionMaintenance(0.0);
		    		                                                            return currentAccountService.create(currentAccount);
		    		                                                        });
		    		                                            }
		    		
		    		                                        default: currentAccount.setCustomer(cst);
		    		                                                currentAccount.setDate(LocalDateTime.now());
		    		                                                return currentAccountService.create(currentAccount); // Mono<CurrentAccount>
		    		                                    }
		    		
		    		                                });
		    		                        })
		    		                        .map(ca -> new ResponseEntity<>(ca, HttpStatus.CREATED));
		    	    		})
    					)
    					.defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    			
    			
        		

    }

    @PutMapping("/update")
    public Mono<ResponseEntity<CurrentAccount>> update(@RequestBody CurrentAccount currentAccount) {
        return currentAccountService.findCustomerById(currentAccount.getCustomer().getId())
                .filter(customer -> currentAccount.getBalance() >= 0)
                .flatMap(customer -> {
                    switch (customer.getTypeCustomer().getValue()){
                        case EMPRESARIAL:
                            switch (customer.getTypeCustomer().getSubType().getValue()){
                                case PYME: return currentAccountService.findCreditCardByCustomerId(customer.getId())
                                        .count()
                                        .filter(cntCCard -> cntCCard > 0)
                                        .flatMap(cntCCard -> {
                                            currentAccount.setCustomer(customer);
                                            currentAccount.setDate(LocalDateTime.now());
                                            currentAccount.setCommissionMaintenance(0.0);
                                            return currentAccountService.create(currentAccount);
                                        });
                            }

                        default: currentAccount.setCustomer(customer);
                            currentAccount.setDate(LocalDateTime.now());
                            return currentAccountService.create(currentAccount); // Mono<CurrentAccount>
                    }
                })
                .map(ca -> new ResponseEntity<>(ca, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return currentAccountService.delete(id)
                .filter(deleteCustomer -> deleteCustomer)
                .map(deleteCustomer -> new ResponseEntity<>("Customer Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findByAccountNumber/{numberAccount}")
    public Mono<CurrentAccount> findByAccountNumber(@PathVariable String numberAccount){
        return currentAccountService.findByAccountNumber(numberAccount);
    }

    @PutMapping("/updateTransference")
    public Mono<ResponseEntity<CurrentAccount>> updateForTransference(@Valid @RequestBody CurrentAccount currentAccount) {
        return currentAccountService.create(currentAccount)
                .filter(customer -> currentAccount.getBalance() >= 0)
                .map(ft -> new ResponseEntity<>(ft, HttpStatus.CREATED));
    }
}
