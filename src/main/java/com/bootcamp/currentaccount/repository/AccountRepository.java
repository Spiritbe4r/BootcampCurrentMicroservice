package com.bootcamp.currentaccount.repository;

import com.bootcamp.currentaccount.models.bean.Account;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {


  Flux<Account> findAllByClientIdNumber(String accountNumber);
  Mono<Account> findByAccountNumber(String accountNumber);

  Mono<Account> findByClientIdNumber(String clientIdNumber);
}
