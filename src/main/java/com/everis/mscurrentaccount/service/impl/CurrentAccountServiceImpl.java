package com.everis.mscurrentaccount.service.impl;

import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.repository.CurrentAccountRepository;
import com.everis.mscurrentaccount.service.CurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrentAccountServiceImpl implements CurrentAccountService {
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


}
