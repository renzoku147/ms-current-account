package com.everis.mscurrentaccount.service.impl;

import com.everis.mscurrentaccount.entity.BankAccount;
import com.everis.mscurrentaccount.entity.Credit;
import com.everis.mscurrentaccount.entity.CreditCard;
import com.everis.mscurrentaccount.entity.CreditTransaction;
import com.everis.mscurrentaccount.entity.CurrentAccount;
import com.everis.mscurrentaccount.entity.Customer;
import com.everis.mscurrentaccount.entity.DebitCardTransaction;
import com.everis.mscurrentaccount.entity.FixedTerm;
import com.everis.mscurrentaccount.entity.SavingAccount;
import com.everis.mscurrentaccount.repository.CurrentAccountRepository;
import com.everis.mscurrentaccount.service.CurrentAccountService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrentAccountServiceImpl implements CurrentAccountService {

    WebClient webClientCustomer = WebClient.create("http://localhost:8887/ms-customer/customer");

    WebClient webClientCreditCard = WebClient.create("http://localhost:8887/ms-creditcard/creditCard");
    
    WebClient webClientCreditCharge = WebClient.create("http://localhost:8887/ms-credit-charge/creditCharge");
    
    WebClient webClientCreditPay = WebClient.create("http://localhost:8887/ms-credit-pay/creditPaid");
    
    WebClient webClientDebitCardTransaction = WebClient.create("http://localhost:8887/ms-debitcard-transaction/debitCardTransaction");
    
    WebClient webClientFixed = WebClient.create("http://localhost:8887/ms-fixed-term/fixedTerm");

    WebClient webClientSaving = WebClient.create("http://localhost:8887/ms-saving-account/savingAccount");

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
    public Flux<CurrentAccount> findAccountByCustomerId(String id) {
        return currentAccountRepository.findByCustomerId(id);
    }
    
    @Override
    public Mono<CurrentAccount> update(CurrentAccount t) {
        return currentAccountRepository.save(t)
        		.filter(ca -> ca.getBalance()>0);
    }

    @Override
    public Mono<Boolean> delete(String t) {
        return currentAccountRepository.findById(t)
                .flatMap(ca -> currentAccountRepository.delete(ca).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Mono<Long> countAccountBank(String id) {
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
    public Mono<CurrentAccount> findByAccountNumber(String number) {
        return currentAccountRepository.findByAccountNumber(number);
    }

	@Override
	public Mono<Optional<BankAccount>> verifyAccountNumber(String accountNumber) {
		// TODO Auto-generated method stub
		return  currentAccountRepository.findByAccountNumber(accountNumber)
				.map(current -> {
                    System.out.println("Encontro fixedTerm > " + current.getId());
                    return Optional.of((BankAccount)current);
                })
				.switchIfEmpty(webClientFixed.get().uri("/findByAccountNumber/{id}", accountNumber)
		                        .accept(MediaType.APPLICATION_JSON)
		                        .retrieve()
		                        .bodyToMono(FixedTerm.class)
		                        .map(fixedTerm -> {
		                            System.out.println("Encontro fixedTerm > " + fixedTerm.getId());
		                            return Optional.of((BankAccount)fixedTerm);
		                        })
		                        .switchIfEmpty(webClientSaving.get().uri("/findByAccountNumber/{id}", accountNumber)
		                                        .accept(MediaType.APPLICATION_JSON)
		                                        .retrieve()
		                                        .bodyToMono(SavingAccount.class)
		                                        .map(savingAccount -> {
		                                            System.out.println("Encontro savingAccount > " + savingAccount.getId());
		                                            return Optional.of((BankAccount)savingAccount);
		                                        }))
				)
				.defaultIfEmpty(Optional.empty());
				
				
	}

	@Override
	public Mono<Boolean> verifyExpiredDebt(String idcustomer) {
		return webClientCreditCharge.get().uri("/verifyExpiredDebt/{idcustomer}", idcustomer)
											        .accept(MediaType.APPLICATION_JSON)
											        .retrieve()
											        .bodyToMono(Boolean.class);
	}


}
