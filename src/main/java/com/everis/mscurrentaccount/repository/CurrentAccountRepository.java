package com.everis.mscurrentaccount.repository;


import com.everis.mscurrentaccount.entity.CurrentAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CurrentAccountRepository extends ReactiveMongoRepository<CurrentAccount, String> {

    public Flux<CurrentAccount> findByCustomerId(String id);
}
