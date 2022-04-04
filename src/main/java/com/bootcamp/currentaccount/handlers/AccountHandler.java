package com.bootcamp.currentaccount.handlers;

import com.bootcamp.currentaccount.models.bean.Account;
import com.bootcamp.currentaccount.models.dto.ClientDTO;
import com.bootcamp.currentaccount.models.dto.Holder;
import com.bootcamp.currentaccount.service.AccountService;
import com.bootcamp.currentaccount.service.ClientDTOService;
import com.bootcamp.currentaccount.service.CreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountHandler.class);
  @Autowired
  private AccountService accountService;

  @Autowired
  private CreditService creditService;

  @Autowired
  private ClientDTOService clientService;

  /**
   * Find all mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> findAll(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(accountService.findAll(), Account.class);
  }

  /**
   * Find account by accountNumber.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> findByAccountNumber(ServerRequest request) {
    String accountNumber = request.pathVariable("accountNumber");
    LOGGER.info("The AccountNumber is " + accountNumber);
    return accountService.findByAccountNumber(accountNumber).flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
          .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> findByClientIdNumber(ServerRequest request) {
    String clientIdNumber = request.pathVariable("clientIdNumber");
    return accountService.findByClientIdNumber(clientIdNumber)
          .flatMap(c -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
          .switchIfEmpty(ServerResponse.notFound().build());
  }

  /**
   * Add card holder mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> addCardHolder(ServerRequest request) {
    String accountNumber = request.pathVariable("accountNumber");
    Mono<Holder> holderMono = request.bodyToMono(Holder.class);
    LOGGER.info("El AccountNumber es " + accountNumber);
    return holderMono
          .flatMap(titular -> clientService.getClient(titular.getDni())
                .flatMap(cardHolder -> accountService.findByAccountNumber(accountNumber))
                .filter(currentAccount -> currentAccount.getClient().getCode().equals("2001")
                      || currentAccount.getClient().getCode().equals("2002"))
                .flatMap(currentAccount -> {
                  currentAccount.getHolders().add(titular);
                  return accountService.update(currentAccount);
                }))
          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c))
          ).switchIfEmpty(ServerResponse.notFound().build());
  }

  /**
   * New current account mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> newCurrentAccount(ServerRequest request) {

    Mono<Account> accountMono = request.bodyToMono(Account.class);


    return accountMono.flatMap(account -> clientService.getClient(account.getClientIdNumber())
                .flatMap(customer -> clientService.getClient(account.getHolders().get(0).getDni()))
                .flatMap(customer ->
                {
                  account.getHolders().get(0).setClientName(customer.getName());

                  return clientService.getClient(account.getClientIdNumber());
                })
                .flatMap(titular -> clientService.getClient(account.getClientIdNumber())
                )
                .flatMap(customer -> {
                  account.setTypeOfAccount("CURRENT_ACCOUNT");
                  account.setMaxLimitMovementPerMonth(account.getMaxLimitMovementPerMonth());
                  account.setMovementPerMonth(0);
                  LOGGER.info("El customer type es:{}" + customer.getClientType().getCode());
                  account.setClient(ClientDTO.builder()
                        .name(customer.getName()).code(customer.getClientType().getCode())
                        .clientIdNumber(customer.getClientIdNumber()).build());
                  if (customer.getClientType().getCode().equals("2001")
                        || customer.getClientType().getCode().equals("2002")) {
                    return accountService.create(account);
                  } else {
                    return creditService.validateDebtorCredit(account.getClient().getClientIdNumber())
                          .flatMap(debitor -> {
                            if (debitor == true) {
                              return Mono.empty();
                            } else {
                              return
                                    accountService.validateClientIdNumber(account.getClient().getClientIdNumber());
                            }

                          })
                          .flatMap(accountFound -> {
                            account.setHolders(null);
                            if (accountFound.getClientIdNumber() != null) {
                              LOGGER.info("La cuenta encontrada es: " + accountFound.getClientIdNumber());
                              return Mono.error(new RuntimeException("THERE IS AN ACCOUNT WITH THIS CUSTOMER ALREADY"));
                            } else if (!account.getClient().getClientIdNumber()
                                  .equals(account.getHolders().get(0).getDni())) {
                              LOGGER.info("El dni del titular y el cliente son diferentes cuando el customer es personal ");
                              return Mono.error(new RuntimeException("THERE IS AN ACCOUNT WITH THIS CUSTOMER ALREADY"));
                            } else {
                              LOGGER.info("No encontró nada: ");
                              return accountService.create(account);
                            }
                          });
                  }
                }))
          .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c))
          ).switchIfEmpty(ServerResponse.badRequest().build());
  }

  public Mono<Account> validateClientCredit(Account account) {
    return accountService.validateClientIdNumber(account.getClientIdNumber())
          .flatMap(accountFound -> {
            account.setHolders(null);
            if (accountFound.getClientIdNumber() != null) {
              LOGGER.info("La cuenta encontrada es: " + accountFound.getClientIdNumber());
              return Mono.error(new RuntimeException("THERE IS AN ACCOUNT WITH THIS CUSTOMER ALREADY"));
            } else if (!account.getClient().getClientIdNumber()
                  .equals(account.getHolders().get(0).getDni())) {
              LOGGER.info("El dni del titular y el cliente son diferentes cuando el customer es personal ");
              return Mono.error(new RuntimeException("THERE IS AN ACCOUNT WITH THIS CUSTOMER ALREADY"));
            } else {
              LOGGER.info("No encontró nada: ");
              return accountService.create(account);
            }
          });

  }

  /**
   * Find by customer identity number mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> findAllByClientIdNumber(ServerRequest request) {
    String customerIdentityNumber = request.pathVariable("clientIdNumber");
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(accountService.findAllByClientIdNumber(customerIdentityNumber), Account.class);
  }

  /**
   * save account mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> deleteCurrentAccount(ServerRequest request) {

    String id = request.pathVariable("id");

    Mono<Account> accountMono = accountService.findById(id);
    return accountMono
          .doOnNext(c -> LOGGER.info("deleteConsumption: consumptonId={}", c.getId()))
          .flatMap(c -> accountService.delete(id).then(ServerResponse.noContent().build()))
          .switchIfEmpty(ServerResponse.notFound().build());
  }

  /**
   * Update saving account mono.
   *
   * @param request the request
   * @return the mono
   */
  public Mono<ServerResponse> updateCurrentAccount(ServerRequest request) {
    Mono<Account> accountMono = request.bodyToMono(Account.class);
    String id = request.pathVariable("id");
    return accountService.findById(id).zipWith(accountMono, (db, req) -> {
            db.setAmount(req.getAmount());
            db.setMovementPerMonth((req.getMovementPerMonth()));
            return db;
          }).flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountService.update(c), Account.class))
          .switchIfEmpty(ServerResponse.notFound().build());
  }
}
