package com.everis.mscurrentaccount.service;

import java.util.Optional;

import com.everis.mscurrentaccount.entity.BankAccount;
import com.everis.mscurrentaccount.entity.CreditCard;
import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrentAccountService {
    Mono<CurrentAccount> create(CurrentAccount t);

    Flux<CurrentAccount> findAll();

    Mono<CurrentAccount> findById(String id);

    Mono<CurrentAccount> update(CurrentAccount t);

    Mono<Boolean> delete(String t);

    Mono<Long> countAccountBank(String id);

    Mono<Customer> findCustomerById(String id);
    
    Flux<CurrentAccount> findAccountByCustomerId(String idcustomer);

    Flux<CreditCard> findCreditCardByCustomerId(String id);

    Mono<CurrentAccount> findByAccountNumber(String number);
    
    Mono<Optional<BankAccount>> verifyAccountNumber(String accountNumber);
    
    Mono<Boolean> verifyExpiredDebt(String idcustomer);
}
