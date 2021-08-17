package com.everis.mscurrentaccount.service.impl;

import com.everis.mscurrentaccount.entity.CreditCard;
import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.entity.Customer;
import com.everis.mscurrentaccount.repository.CurrentAccountRepository;
import com.everis.mscurrentaccount.service.CurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrentAccountServiceImpl implements CurrentAccountService {

    WebClient webClientCustomer = WebClient.create("http://localhost:8887/ms-customer/customer/customer");

    WebClient webClientCreditCard = WebClient.create("http://localhost:8887/ms-creditcard/creditcard/creditcard");

    @Autowired
    CurrentAccountRepository currentAccountRepository;

    @Override
    public Mono<CurrentAccount> create(CurrentAccount t) {
        return currentAccountRepository.save(t);
    }

    @Override
    public Flux<CurrentAccount> findAll() {
        return currentAccountRepository.findAll();
    }

    @Override
    public Mono<CurrentAccount> findById(String id) {
        return currentAccountRepository.findById(id);
    }

    @Override
    public Mono<CurrentAccount> update(CurrentAccount t) {
        return currentAccountRepository.save(t);
    }

    @Override
    public Mono<Boolean> delete(String t) {
        return currentAccountRepository.findById(t)
                .flatMap(ca -> currentAccountRepository.delete(ca).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Mono<Long> countCustomerAccountBank(String id) {
        return currentAccountRepository.findByCustomerId(id).count();
    }

    @Override
    public Mono<Customer> findCustomerById(String id) {
        return webClientCustomer.get().uri("/find/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    @Override
    public Flux<CreditCard> findCreditCardByCustomerId(String id) {
        return webClientCreditCard.get().uri("/findCreditCards/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CreditCard.class);
    }

    @Override
    public Mono<CurrentAccount> findByCardNumber(String number) {
        return currentAccountRepository.findByCardNumber(number);
    }


}
