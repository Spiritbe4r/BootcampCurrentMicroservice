package com.bootcamp.currentaccount.config;

import com.bootcamp.currentaccount.handlers.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AccountRouter {

  /**
   * Routes router function.
   *
   * @param accountHandler the account handler
   * @return the router function
   */
  @Bean
  public RouterFunction<ServerResponse> routes(AccountHandler accountHandler) {
    return route(GET("/api/currentAccount"), accountHandler::findAll)
          .andRoute(GET("/api/currentAccount/{clientIdNumber}"),accountHandler::findAllByClientIdNumber)
          .andRoute(GET("/api/currentAccount/client/{clientIdNumber}"),accountHandler::findByClientIdNumber)
          .andRoute(GET("/api/currentAccount/account/{accountNumber}"), accountHandler::findByAccountNumber)
          .andRoute(PUT("/api/currentAccount/holder/{accountNumber}"), accountHandler::addCardHolder)
          .andRoute(POST("/api/currentAccount"), accountHandler::newCurrentAccount)
          .andRoute(PUT("/api/currentAccount/{id}"), accountHandler::updateCurrentAccount)
          .andRoute(DELETE("/api/currentAccount/{id}"), accountHandler::deleteCurrentAccount);
  }
}
