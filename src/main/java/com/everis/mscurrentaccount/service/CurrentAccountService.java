package com.everis.mscurrentaccount.service;

import com.everis.mscurrentaccount.entity.CurrentAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrentAccountService {
    Mono<CurrentAccount> create(CurrentAccount t);

    Flux<CurrentAccount> findAll();

    Mono<CurrentAccount> findById(String id);

    Mono<CurrentAccount> update(CurrentAccount t);

    Mono<Boolean> delete(String t);

    Mono<Long> countCustomerAccountBank(String id);
}
