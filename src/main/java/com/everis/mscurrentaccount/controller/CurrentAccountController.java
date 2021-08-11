package com.everis.mscurrentaccount.controller;

import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.entity.Customer;
import com.everis.mscurrentaccount.entity.TypeCustomer;
import com.everis.mscurrentaccount.service.CurrentAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RefreshScope
@RestController
@RequestMapping("/currentAccount")
@Slf4j
public class CurrentAccountController {

    WebClient webClient = WebClient.create("http://localhost:8013/customer");

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

    @PostMapping("/create")
    public Mono<ResponseEntity<CurrentAccount>> create(@RequestBody CurrentAccount currentAccount){
        // VERIFICAMOS SI EXISTE EL CLIENTE
        Mono<Customer> customer = webClient.get().uri("/find/{id}", currentAccount.getCustomer().getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Customer.class);
                    //Mono<Customer>
        return customer.flatMap(cst -> {
                    return currentAccountService.countCustomerAccountBank(currentAccount.getCustomer().getId()) // Mono<Long>
                        .filter(count -> {
                            if(cst.getTypeCustomer().equals(TypeCustomer.PERSONAL)) return count < 1; // MAX 1
                            else {
                                return currentAccount != null & currentAccount.getHolders().size() > 0;
                            }
                        })
                        .flatMap(c -> {
                            currentAccount.setCustomer(cst);
                            currentAccount.setDate(LocalDateTime.now());
                            return currentAccountService.create(currentAccount); // Mono<CurrentAccount>
                        });
                })
                .map(ca -> new ResponseEntity<>(ca, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @PutMapping("/update")
    public Mono<ResponseEntity<CurrentAccount>> update(@RequestBody CurrentAccount c) {
        return currentAccountService.update(c)
                .filter(ca -> ca.getBalance() >= 0)
                .map(savedCustomer -> new ResponseEntity<>(savedCustomer, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return currentAccountService.delete(id)
                .filter(deleteCustomer -> deleteCustomer)
                .map(deleteCustomer -> new ResponseEntity<>("Customer Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
